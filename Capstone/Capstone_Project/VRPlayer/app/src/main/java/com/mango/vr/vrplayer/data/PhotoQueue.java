package com.mango.vr.vrplayer.data;

import android.graphics.Bitmap;

import java.util.LinkedList;
import java.util.Queue;

public class PhotoQueue
{


    private Queue<PhotoData> mQueue = new LinkedList<>();

    synchronized public boolean isEmpty()
    {
        return mQueue.isEmpty();
    }

    synchronized public void push(float angle, Bitmap bitmap)
    {
        mQueue.add(new PhotoData(angle, bitmap));
    }

    synchronized public PhotoData pop()
    {
        return mQueue.poll();
    }
}
