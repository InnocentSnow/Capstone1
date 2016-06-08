package com.mango.vr.vrplayer.resource;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.mango.vr.vrplayer.vr.VRRenderer;

import java.io.InputStream;

public abstract class TextureMgr
{
    public static int check;

    public static void applyTexture(int textureID, int number)
    {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + number);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID);
    }

    public static void cancelTexture()
    {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
    public static void load(Context context)
    {
        check = loadTextureFromAsset(context, "check.png");
    }

    public static int loadTextureFromBitmap(Bitmap bitmap)
    {
        int textureBuffer[] = {0};

        GLES20.glPixelStorei(GLES20.GL_UNPACK_ALIGNMENT, 1);
        GLES20.glGenTextures(1, textureBuffer, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureBuffer[0]);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        return textureBuffer[0];
    }

    private static int loadTextureFromAsset(Context context, String textureName)
    {
        Bitmap bitmap = AssetMgr.getBitmap(context, textureName);
        if(bitmap == null) return 0;

        int result = loadTextureFromBitmap(bitmap);
        bitmap.recycle();

        return result;
    }
}
