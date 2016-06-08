package com.mango.vr.vrplayer.network;

import com.mango.vr.vrplayer.data.PhotoQueue;
import com.mango.vr.vrplayer.vr.VRRenderer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

class PictureData
{
    public byte[] rawData = null;
    public float angle = 0;
}

public class Network
{
    public static float angleFromNetwork(int angle)
    {
        return (float)(angle * Math.PI / 2100);
    }

    public static int angleToNetwork(float angle)
    {
        return (int)(2100 * angle / Math.PI);
    }

    private Receiver mReceiver;

    private static String IP = "52.78.21.53";
    private static int PORT = 4000;

    private Socket mSocket = null;
    private DataOutputStream writer = null;
    private DataInputStream reader = null;

    private boolean mIsAlive;

    public Network(PhotoQueue photoQueue)
    {
        mReceiver = new Receiver(this, photoQueue);
    }

    public void sendAngle(float angle)
    {
        if(writer == null) return;

        int value = angleToNetwork(angle);

        try
        {
            writer.writeInt(value);
            writer.flush();
        }
        catch(Exception e)
        {
            VRRenderer.printError("Cannot Send Angle");
            e.printStackTrace();
        }
    }

    public PictureData receivePicture()
    {
        PictureData pData = new PictureData();

        try
        {
            pData.angle = angleFromNetwork(reader.readInt());
            int size = reader.readInt();

            if(size > 0x00fffff)
            {
                VRRenderer.printLog("Wrong Size!");
                return null;
            }

            byte data[] = new byte[size];
            for(int i = 0; i < data.length; ++i)
            {
                data[i] = reader.readByte();
            }

            pData.rawData = data;
        }
        catch (Exception e)
        {
            VRRenderer.printError("Receive Error");
            e.printStackTrace();
        }

        return pData;
    }

    public void connect()
    {
        Runnable connectAux = new Runnable()
        {
            @Override
            public void run()
            {
                while (mIsAlive)
                {
                    if (mSocket == null || !mSocket.isConnected())
                    {
                        try
                        {
                            mSocket = new Socket(IP, PORT);

                            writer = new DataOutputStream(mSocket.getOutputStream());
                            reader = new DataInputStream((mSocket.getInputStream()));

                            if(!mReceiver.isAlive()) mReceiver.start();
                        }
                        catch (Exception e)
                        {
                            VRRenderer.printError("Cannot Connect to Server");
                            e.printStackTrace();
                        }
                    }

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        VRRenderer.printLog("waked up");
                    }
                }
            }
        };

        mIsAlive = true;
        new Thread(connectAux).start();
    }

    public void disconnect()
    {
        if(mSocket != null)
        {
            try
            {
                mReceiver.end();
                mSocket.close();
            }
            catch(Exception e)
            {
                VRRenderer.printError("Cannot Disconnect");
                e.printStackTrace();
            }
        }

        mSocket = null;
        writer = null;
        reader = null;
        mIsAlive = false;
    }
}
