package com.mango.vr.vrplayer.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mango.vr.vrplayer.data.PhotoQueue;

public class Receiver extends Thread
{
    private Network mNetwork;
    private PhotoQueue mPhotoQueue;
    private boolean mIsAlive = true;

    public Receiver(Network network, PhotoQueue photoQueue)
    {
        mNetwork = network;
        mPhotoQueue = photoQueue;
    }

    public void end()
    {
        mIsAlive = false;
    }

    public void run()
    {
        while(mIsAlive)
        {
            PictureData data = mNetwork.receivePicture();

            if (data != null)
            {
                byte rawData[] = data.rawData;
                float angle = data.angle;

                if(rawData != null)
                {
                    Bitmap photo = BitmapFactory.decodeByteArray(rawData, 0, rawData.length);
                    if(photo != null) mPhotoQueue.push(angle, photo);
                }
                else break;
            }
            else break;
        }

        mNetwork.disconnect();
        mNetwork.connect();
    }
}
