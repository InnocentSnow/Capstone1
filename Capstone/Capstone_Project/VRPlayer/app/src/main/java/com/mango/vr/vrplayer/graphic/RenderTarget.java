package com.mango.vr.vrplayer.graphic;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.opengl.GLES20;

import com.mango.vr.vrplayer.resource.TextureMgr;
import com.mango.vr.vrplayer.vr.VRRenderer;

public class RenderTarget
{
    private static int renderTargetWidth = 256;
    private static int renderTargetHeight = 256;

    private int oldFrameBuffer[] = new int[1];
    private int renderTargetFrameBuffer;
    private int renderTargetDepthBuffer;
    private int renderTargetTexture;

    static public int getDepthBuffer()
    {
        int depthBuffer[] = new int[1];
        GLES20.glGenRenderbuffers(1, depthBuffer, 0);

        return depthBuffer[0];
    }

    public void applyTexture(int number) { TextureMgr.applyTexture(renderTargetTexture, number); }

    public void applyRenderTarget()
    {
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING, oldFrameBuffer, 0);

        GLES20.glViewport(0, 0, renderTargetWidth, renderTargetHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, renderTargetFrameBuffer);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, renderTargetTexture, 0);
        GLES20.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, renderTargetDepthBuffer);
    }

    public void cancelRenderTarget()
    {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, oldFrameBuffer[0]);
    }

    public RenderTarget()
    {
        this(0);
    }

    public RenderTarget(int depthBufferId)
    {
        int frameBuffer[] = new int[1];
        int renderedTexture[] = new int[1];
        int depthBuffer[] = new int[1];

        GLES20.glGenFramebuffers(1, frameBuffer, 0);
        GLES20.glGenTextures(1, renderedTexture, 0);

        if (depthBufferId == 0) GLES20.glGenRenderbuffers(1, depthBuffer, 0);
        else depthBuffer[0] = depthBufferId;

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, renderedTexture[0]);

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        IntBuffer texBuffer = ByteBuffer.allocateDirect(renderTargetWidth * renderTargetHeight * 4).order(ByteOrder.nativeOrder()).asIntBuffer();

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, renderTargetWidth, renderTargetHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, texBuffer);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthBuffer[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, renderTargetWidth, renderTargetHeight);

        renderTargetFrameBuffer = frameBuffer[0];
        renderTargetDepthBuffer = depthBuffer[0];
        renderTargetTexture = renderedTexture[0];

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, 0);

        int status = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if(status != GLES20.GL_FRAMEBUFFER_COMPLETE) VRRenderer.printError("Failed to create render target");

        applyRenderTarget();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        cancelRenderTarget();
    }
}