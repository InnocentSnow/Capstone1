package com.mango.vr.vrplayer.graphic;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.mango.vr.vrplayer.resource.AssetMgr;
import com.mango.vr.vrplayer.resource.ShaderMgr;
import com.mango.vr.vrplayer.resource.TextureMgr;
import com.mango.vr.vrplayer.vr.VRRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class CylinderTexture
{
    private RenderTarget renderTargetFront;
    private RenderTarget renderTargetBack;

    private FloatBuffer positionBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer texCoordBuffer;
    private int numIndices;

    public CylinderTexture(float hAngle)
    {
        setBuffer(hAngle);
        renderTargetFront = new RenderTarget();
        renderTargetBack = new RenderTarget();
    }

    public void applyTexture()
    {
        renderTargetFront.applyTexture(0);
    }

    private int applyPictureToTexture(Bitmap bitmap)
    {
        final int u_texture = GLES20.glGetUniformLocation(ShaderMgr.cylinder_add, "u_texture");
        int texture = TextureMgr.loadTextureFromBitmap(bitmap);

        GLES20.glUniform1i(u_texture, 0);
        TextureMgr.applyTexture(texture, 0);

        return texture;
    }

    private void applyOriginal()
    {
        final int u_origin = GLES20.glGetUniformLocation(ShaderMgr.cylinder_add, "u_origin");

        GLES20.glUniform1i(u_origin, 1);
        renderTargetBack.applyTexture(1);
    }

    private void applyAttributes()
    {
        final int a_position = GLES20.glGetAttribLocation(ShaderMgr.cylinder_add, "a_position");
        final int a_texCoord = GLES20.glGetAttribLocation(ShaderMgr.cylinder_add, "a_texCoord");

        GLES20.glUseProgram(ShaderMgr.cylinder_add);

        GLES20.glVertexAttribPointer(a_position, 2, GLES20.GL_FLOAT, false, 2 * 4, positionBuffer);
        GLES20.glEnableVertexAttribArray(a_position);

        GLES20.glVertexAttribPointer(a_texCoord, 2, GLES20.GL_FLOAT, false, 2 * 4, texCoordBuffer);
        GLES20.glEnableVertexAttribArray(a_texCoord);
    }

    private void addPictureAUX(float angle)
    {
        final int u_dx = GLES20.glGetUniformLocation(ShaderMgr.cylinder_add, "u_dx");
        GLES20.glUniform1f(u_dx, angle);

        positionBuffer.position(0);
        texCoordBuffer.position(0);
        indexBuffer.position(0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numIndices, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
    }

    private void swapRenderTarget()
    {
        RenderTarget temp = renderTargetBack;
        renderTargetBack = renderTargetFront;
        renderTargetFront = temp;
    }

    public void addPicture(Bitmap bitmap, float angle)
    {
        final float dAngle[] = {-2, 0, 2};

        swapRenderTarget();
        renderTargetFront.applyRenderTarget();
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

        int texture[] = new int[1];
        applyAttributes();
        applyOriginal();
        texture[0] = applyPictureToTexture(bitmap);

        for (float d : dAngle) addPictureAUX(angle + d);

        GLES20.glDeleteTextures(1, texture, 0);
        renderTargetFront.cancelRenderTarget();
    }

    private void setBuffer(float hAngle)
    {
        final float X[] = {0, 1, 0, 1};
        final float Y[] = {0, 0, 1, 1};

        float position[] = new float[4 * 2];
        float texCoord[] = new float[4 * 2];
        short indices[] = {0, 1, 2, 1, 2, 3};

        for (int i = 0; i < 4; ++i)
        {
            position[2 * i] = (2 * X[i] - 1) * hAngle;
            position[2 * i + 1] = (2 * Y[i] - 1);

            texCoord[2 * i] = X[i];
            texCoord[2 * i + 1] = Y[i];
        }

        positionBuffer = ByteBuffer.allocateDirect(4 * position.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        positionBuffer.put(position);
        positionBuffer.position(0);

        texCoordBuffer = ByteBuffer.allocateDirect(4 * texCoord.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        texCoordBuffer.put(texCoord);
        texCoordBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(2 * indices.length).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);

        numIndices = indices.length;
    }
}
