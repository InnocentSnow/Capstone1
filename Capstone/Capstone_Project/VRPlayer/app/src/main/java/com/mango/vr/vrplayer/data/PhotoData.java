package com.mango.vr.vrplayer.data;

import android.graphics.Bitmap;

public class PhotoData
{
    public float angle;
    public Bitmap bitmap;

    public PhotoData(float angle, Bitmap bitmap)
    {
        this.angle = angle;
        this.bitmap = bitmap;
    }
};
