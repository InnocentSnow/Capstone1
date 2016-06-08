#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <pthread.h>

enum
{
	MOTOR,
	APP,
	CAMERA,
	NUM_ELEMENT,
};

enum
{
	TYPE_DISCONNECTED = 0,
	TYPE_ANGLE = 1,
	TYPE_ROLLBACK = 2,
};

enum
{
	MOTOR_PORT = 3000,
	APP_PORT = 4000,
	CAMERA_PORT = 5000,
	BACKLOG = 5,
	IMAGE_BUFFER_SIZE = 3000000,
	LAP_ANGLE = 4200
};

bool isEnd = false;
char imageBuffer[IMAGE_BUFFER_SIZE];

pthread_mutex_t mutexList[NUM_ELEMENT] = {
	PTHREAD_MUTEX_INITIALIZER,
	PTHREAD_MUTEX_INITIALIZER,
	PTHREAD_MUTEX_INITIALIZER
};

pthread_cond_t condList[NUM_ELEMENT] = {
	PTHREAD_COND_INITIALIZER,
	PTHREAD_COND_INITIALIZER,
	PTHREAD_COND_INITIALIZER
};

const char *deviceName[NUM_ELEMENT] = {
	"Motor",
	"App",
	"Camera"
};

int currentAngle = 0;
int targetAngle = 0;
int threadOn[NUM_ELEMENT] = {0};
int socketList[NUM_ELEMENT] = {0};

struct ThreadData
{
	int clientSocket;
	int threadOn;
};

void sendMessage(int socket, void *arg, int len)
{
	if(socket) write(socket, arg, len);
}

bool receiveMessage(int socket, char *buffer, int len)
{
	int remain = len;

	while(remain)
	{
		int get = read(socket, buffer, remain);
		if(get <= 0)
		{
			printf("disconnect signal %d, socket %d\n", get, socket);
			return false;
		}

		buffer += get;
		remain -= get;
	}

	return remain == 0;
}

unsigned int convertEndian(unsigned int size)
{
	unsigned int a = (size >> 0x18) & 0xFF;
	unsigned int b = (size >> 0x10) & 0xFF;
	unsigned int c = (size >> 0x08) & 0xFF;
	unsigned int d = (size >> 0x00) & 0xFF;

	return (d << 0x18) | (c << 0x10) | (b << 0x08) | (a << 0x00);
}

void *doMotor(void *arg)
{
	int idx = MOTOR;

	pthread_mutex_lock(mutexList + idx);
	ThreadData threadData = *((ThreadData *)arg);
	socketList[idx] = threadData.clientSocket;
	pthread_cond_signal(condList + idx);
	pthread_mutex_unlock(mutexList + idx);

	printf("Motor Connected\n");

	while(!isEnd && threadData.threadOn == threadOn[idx])
	{
		bool result;
		int r;

		result = receiveMessage(threadData.clientSocket, (char *)&r, sizeof(int));
		if(!result) break;

		currentAngle = r;
	}

	int type = TYPE_DISCONNECTED;
	int value = 0;

	pthread_mutex_lock(mutexList + idx);
	if(threadData.threadOn == threadOn[idx]) socketList[idx] = 0;
	sendMessage(threadData.clientSocket, &type, sizeof(int));
	sendMessage(threadData.clientSocket, &value, sizeof(int));
	pthread_mutex_unlock(mutexList + idx);

	printf("Motor Disconnected\n");
}

int getAbs(int a)
{
	return 0 <= a ? a : -a;
}

int calcAngle(int newAngle, int oldAngle)
{
	while(oldAngle < newAngle) newAngle -= LAP_ANGLE;
	while(newAngle + LAP_ANGLE <= oldAngle) newAngle += LAP_ANGLE;

	if(getAbs(newAngle - oldAngle) < getAbs(newAngle + LAP_ANGLE - oldAngle)) return newAngle;
	else return newAngle + LAP_ANGLE;
}

void *doApp(void *arg)
{
	int idx = APP;

	pthread_mutex_lock(mutexList + idx);
	ThreadData threadData = *((ThreadData *)arg);
	socketList[idx] = threadData.clientSocket;
	pthread_cond_signal(condList + idx);
	pthread_mutex_unlock(mutexList + idx);

	printf("App Connected\n");

	while(!isEnd && threadData.threadOn == threadOn[idx])
	{
		bool result;
		int angle;

		result = receiveMessage(threadData.clientSocket, (char *)&angle, sizeof(int));
		if(!result)
		{
			pthread_mutex_lock(mutexList + idx);
			if(threadData.threadOn == threadOn[idx]) socketList[idx] = 0;
			threadData.clientSocket = 0;
			pthread_mutex_unlock(mutexList + idx);

			break;
		}

		angle = convertEndian(angle);
		targetAngle = calcAngle(angle, targetAngle);

		int type	= TYPE_ANGLE;
		int target	= MOTOR;

		if(socketList[target])
		{
			pthread_mutex_lock(mutexList + target);
			if(socketList[target])
			{
				sendMessage(socketList[target], &type, sizeof(int));
				sendMessage(socketList[target], &targetAngle, sizeof(int));
			}
			pthread_mutex_unlock(mutexList + target);
		}
	}

	if(threadData.threadOn == threadOn[idx])
	{
		int target = MOTOR;
		int type = TYPE_ROLLBACK;
		int value = 0;

		pthread_mutex_lock(mutexList + target);
		sendMessage(socketList[target], &type, sizeof(int));
		sendMessage(socketList[target], &value, sizeof(int));
		pthread_mutex_unlock(mutexList + target);
	}

	int type = TYPE_DISCONNECTED;
	int value = 0;

	if(threadData.clientSocket)
	{
		pthread_mutex_lock(mutexList + idx);
		if(threadData.threadOn == threadOn[idx]) socketList[idx] = 0;
		sendMessage(threadData.clientSocket, &type, sizeof(int));
		sendMessage(threadData.clientSocket, &value, sizeof(int));
		pthread_mutex_unlock(mutexList + idx);
	}

	printf("App Disconnected\n");
}

void *doCamera(void *arg)
{
	static int printCnt = 0;
	static int sendCnt = 0;
	int idx = CAMERA;

	pthread_mutex_lock(mutexList + idx);
	ThreadData threadData = *((ThreadData *)arg);
	socketList[idx] = threadData.clientSocket;
	pthread_cond_signal(condList + idx);
	pthread_mutex_unlock(mutexList + idx);

	printf("Camera Connected\n");

	while(!isEnd && threadData.threadOn == threadOn[idx])
	{
		int sendSize;
		bool result;

		result = receiveMessage(threadData.clientSocket, (char *)&sendSize, sizeof(int));
		if(!result) break;

		int size		= convertEndian(sendSize);
		int sendAngle	= convertEndian(currentAngle);
		result = receiveMessage(threadData.clientSocket, imageBuffer, size);
		if(!result) break;

		int target = APP;


		if(1 < sendCnt++)
		{
			pthread_mutex_lock(mutexList + target);	
			sendMessage(socketList[target], (char *)&sendAngle, sizeof(int));
			sendMessage(socketList[target], (char *)&sendSize, sizeof(int));
			sendMessage(socketList[target], imageBuffer, size);
			pthread_mutex_unlock(mutexList + target);

			sendCnt = 0;
		}

		if(10 < printCnt++)
		{
			printf("Receive From Camera\n");
			printCnt = 0;
		}
	}

	int type = TYPE_DISCONNECTED;
	int value = 0;

	pthread_mutex_lock(mutexList + idx);
	if(threadData.threadOn == threadOn[idx]) socketList[idx] = 0;
	sendMessage(threadData.clientSocket, &type, sizeof(int));
	sendMessage(threadData.clientSocket, &value, sizeof(int));
	pthread_mutex_unlock(mutexList + idx);

	printf("Camera Disconnected\n");
}

int listenClient(int port)
{
	int serverSocket = socket(AF_INET, SOCK_STREAM, 0);
	if(serverSocket == -1)
	{
		printf("Cannot open stream socket\n");
		return -1;
	}

	struct sockaddr_in serverAddr;
	memset(&serverAddr, 0, sizeof(serverAddr));
	serverAddr.sin_family = AF_INET;
	serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
	serverAddr.sin_port = htons(port);

	if(bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr)) < 0)
	{
		printf("Cannot bind local address\n");
		return -1;
	}

	if(listen(serverSocket, BACKLOG) < 0)
	{
		printf("Cannot listen\n");
		return -1;
	}

	return serverSocket;
}

void getConnection(int port, void *(*func)(void *), int idx)
{
	int serverSocket = listenClient(port);

	if(serverSocket == -1)
	{
		printf("Failed to listen\n");
		isEnd = true;
		return;
	}

	printf("Wait for %s client\n", deviceName[idx]);

	while(!isEnd)
	{
		struct sockaddr_in clientAddr;
		int clientAddrSize = sizeof(&clientAddr);
		int clientSocket = accept(serverSocket, (struct sockaddr *)&clientAddr,(socklen_t *)&clientAddrSize);

		pthread_t thread;
		ThreadData threadData;

		threadData.clientSocket	= clientSocket;
		threadData.threadOn		= ++threadOn[idx];

		pthread_mutex_lock(mutexList + idx);
		pthread_create(&thread, NULL, func, &threadData);
		pthread_cond_wait(condList + idx, mutexList + idx);
		pthread_mutex_unlock(mutexList + idx);

		printf("Client %s Connected \tIP : %s\n", deviceName[idx], inet_ntoa(clientAddr.sin_addr));
	}
}

void *connectMotor(void *)
{
	getConnection(MOTOR_PORT, doMotor, MOTOR);
}

void *connectApp(void *)
{
	getConnection(APP_PORT, doApp, APP);
}

void *connectCamera(void *)
{
	getConnection(CAMERA_PORT, doCamera, CAMERA);
}

typedef void* (*Runnable)(void *);

int main()
{
	printf("start\n");

	for(int i = 0; i < NUM_ELEMENT; ++i)
	{
		pthread_mutex_init(mutexList + i, NULL);
		pthread_cond_init(condList + i, NULL);
	}

	Runnable func[] = {
		connectMotor,
		connectApp,
		connectCamera
	};

	pthread_t thread[NUM_ELEMENT];
	for(int i = 0; i < NUM_ELEMENT; ++i) pthread_create(thread + i, NULL, func[i], NULL);
	for(int i = 0; i < NUM_ELEMENT; ++i) pthread_join(thread[i], NULL);
	for(int i = 0; i < NUM_ELEMENT; ++i)
	{
		pthread_cond_destroy(condList + i);
		pthread_mutex_destroy(mutexList + i);
	}

	printf("end\n");
	return 0;
}
