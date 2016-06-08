#include <ESP8266WiFi.h> 
/*
const char* ssid     = "CNUwifi";
const char* password = "helloworld";
*/
/*
const char* ssid     = "cookie2";
const char* password = "0317137263";
*/
const char* ssid     = "U+Net40EB";
const char* password = "6000014817";

int PORT = 3000;
IPAddress server(52,78,21,53);
 
WiFiClient client;

void connectWifi()
{
  WiFi.begin(ssid, password);
 
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(100);
  }
}

void setup()
{  
  Serial.begin(9600);
  delay(10);

  connectWifi();
}

bool connectToServer()
{
  if(client.available()) return true;  
  
  client.stop();
  return client.connect(server, PORT);
}


long idxServer = 0;
long typeServer = 0;
long valueServer = 0;

bool receiveDataFromServer()
{
  if(client.available())
  {
    if(idxServer < 4) ((char *)&typeServer)[idxServer] = client.read();
    else ((char *)&valueServer)[idxServer - 4] = client.read();
    ++idxServer;
    
    if(idxServer == 8)
    {
      idxServer = 0;
      return true;
    }
  }

  return false;
}

long idxMotor = 0;
long valueMotor = 0;
char buffer[12];
int LEN = 12;

bool receiveDataFromMotor()
{
  if(Serial.available())
  {
     buffer[idxMotor++] = Serial.read();
    
    if(idxMotor == LEN)
    {
      idxMotor = 0;
      
      if(buffer[LEN - 1] != 0x7F || buffer[LEN - 2] != 0x7F || buffer[LEN - 3] != 0x7F || buffer[LEN - 4] != 0x7F)
      {
        if(buffer[LEN - 1] != 0x7F)
        {
          for(int i = LEN - 1; 0 <= i; --i)
          {
             if(buffer[i] == 0x7F)
             {
              for(int j = i + 1; j < LEN; ++j) buffer[idxMotor++] = buffer[j];
              break;
             }
          }
        }
        else
        {
          while(idxMotor < 4)
          {
            while(!Serial.available());
            char c = Serial.read();

            if(c != 0x7F)
            {
              buffer[idxMotor++] = c;
              break;
            }
          }
        }
        
        for(int i = idxMotor + 1; i < LEN; ++i) buffer[i] = 0;
        
        return false;
      }

      long checkSum;
      long ack;
      char *dValue = (char *)&valueMotor;
      char *dCheckSum = (char *)&checkSum;
      char *dAck = (char *)&ack;
      
      for(int i = 0; i < 4; ++i)
      {
        dValue[i] = buffer[0 + i];
        dCheckSum[i] = buffer[4 + i];
        dAck[i] = buffer[8 + i];
      }

      return (valueMotor / 65536) + (valueMotor % 65536) == checkSum;
    }
  }
  
  return false;
}

void sendDataToServer(char *buf, int len)
{
  client.write_P(buf, len);
}

void sendDataToMotor(char *buf, int len)
{
  for(int i = 0; i < len; ++i) Serial.write(buf[i]);
}

void sendACK()
{
  for(int i = 0; i < 4; ++i) Serial.write(0x7F);
}

void loop()
{
  int d;
  
  while(!connectToServer()) delay(100);
  while(true)
  {
    if(receiveDataFromServer())
    {
      sendDataToMotor((char *)&typeServer, 4);
      sendDataToMotor((char *)&valueServer, 4);

      long checkSum = (typeServer / 65536) + (typeServer % 65536) + (valueServer / 65536) + (valueServer % 65536);

      sendDataToMotor((char *)&checkSum, 4);
      sendACK();
      
      if(typeServer == 0)
      {
        client.stop();
        break;
      }
    }

    if(receiveDataFromMotor())
    {
      sendDataToServer((char *)&valueMotor, 4);
    }

    if(!client.connected())
    {
      int type = 0;
      int value = 0;
      
      sendDataToMotor((char *)&type, 4);
      sendDataToMotor((char *)&value, 4);
      
      break;
    }
  }
}

