#include <AccelStepper.h>
#include <SoftwareSerial.h>

#define HALFSTEP 8
 
// Motor pin definitions
#define motorPin1  3     // IN1 on the ULN2003 driver 1
#define motorPin2  4     // IN2 on the ULN2003 driver 1
#define motorPin3  5     // IN3 on the ULN2003 driver 1
#define motorPin4  6     // IN4 on the ULN2003 driver 1
 
// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper with 28BYJ-48
AccelStepper stepper1(HALFSTEP, motorPin1, motorPin3, motorPin2, motorPin4);
SoftwareSerial mySerial(8, 9);
long lastTime;
 
void setup()
{
 Serial.begin(9600);
 mySerial.begin(9600);
 delay(10);
 
 Serial.println("start");
 
 stepper1.setMaxSpeed(1000.0);
 stepper1.setAcceleration(200.0);
 stepper1.setSpeed(600);

 lastTime = millis();
}

bool CheckSum(long type, long value, long checkSum)
{
  return (type / 65536) + (type % 65536) + (value / 65536) + (value % 65536) == checkSum;
}

long idx = 0;
char buffer[16];
long type = 0;
long value = 0;
long checkSum;
long ack = 0;

int LEN = 16;

bool receiveData()
{
  if(mySerial.available())
  {
    buffer[idx++] = mySerial.read();
    
    if(idx == LEN)
    {
      idx = 0;
      
      if(buffer[LEN - 1] != 0x7F || buffer[LEN - 2] != 0x7F || buffer[LEN - 3] != 0x7F || buffer[LEN - 4] != 0x7F)
      {
        if(buffer[LEN - 1] != 0x7F)
        {
          for(int i = LEN - 1; 0 <= i; --i)
          {
             if(buffer[i] == 0x7F)
             {
              for(int j = i + 1; j < LEN; ++j) buffer[idx++] = buffer[j];
              break;
             }
          }
        }
        else
        {
          while(idx < 4)
          {
            while(!mySerial.available());
            char c = mySerial.read();

            if(c != 0x7F)
            {
              buffer[idx++] = c;
              break;
            }
          }
        }
        
        for(int i = idx + 1; i < LEN; ++i) buffer[i] = 0;
        
        return false;
      }

      char *dType = (char *)&type;
      char *dValue = (char *)&value;
      char *dCheckSum = (char *)&checkSum;
      char *dAck = (char *)&ack;
      
      for(int i = 0; i < 4; ++i)
      {
        dType[i] = buffer[0 + i];  
        dValue[i] = buffer[4 + i];
        dCheckSum[i] = buffer[8 + i];
        dAck[i] = buffer[12 + i];
      }
      
      return CheckSum(type, value, checkSum);
    }
  }

  return false;
}

void sendMessage(char *buf, int len)
{
  for(int i = 0; i < len; ++i) mySerial.write(buf[i]);
}

void sendACK()
{
  for(int i = 0; i < 4; ++i) mySerial.write(0x7F);
}

void loop()
{
  if(receiveData())
  {    
    if(type == 0) // disconnected
    {
      Serial.println("disconnected");
      stepper1.moveTo(0);
    }
    else if(type == 1) //angle
    {
      Serial.print("angle : ");
      Serial.println(value);
      stepper1.moveTo(value);
    }
    else if(type == 2) //rollback
    {
      Serial.println("rollback");
      stepper1.moveTo(0);
    }
  }

  if(lastTime + 100 < millis())
  {
    long currentAngle = stepper1.currentPosition();
    long checkSum = (currentAngle / 65536) + (currentAngle % 65536);

    sendMessage((char *)&currentAngle, 4);
    sendMessage((char *)&checkSum, 4);
    sendACK();
    
    lastTime = millis();
  }
  
  stepper1.run();
}
