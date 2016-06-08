package com.mango.vr.vrplayer.resource;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mango.vr.vrplayer.vr.VRRenderer;

import java.io.InputStream;

public abstract class AssetMgr
{
    public static Bitmap getBitmap(Context context, String name)
    {
        try
        {
            AssetManager as = context.getAssets();
            InputStream id = as.open(name);

            Bitmap bitmap = BitmapFactory.decodeStream(id);
            id.close();

            return bitmap;
        }
        catch (Exception e)
        {
            VRRenderer.printError("Cannot load texture from asset : " + name);
            e.printStackTrace();

            return null;
        }
    }
}
