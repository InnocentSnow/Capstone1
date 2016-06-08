package com.mango.vr.vrplayer.vr;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vr.sdk.base.GvrView;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.mango.vr.vrplayer.data.PhotoData;
import com.mango.vr.vrplayer.data.PhotoQueue;
import com.mango.vr.vrplayer.graphic.CylinderTexture;
import com.mango.vr.vrplayer.math.Math3D;
import com.mango.vr.vrplayer.network.Network;
import com.mango.vr.vrplayer.object.Cylinder;
import com.mango.vr.vrplayer.resource.ShaderMgr;
import com.mango.vr.vrplayer.resource.TextureMgr;

import javax.microedition.khronos.egl.EGLConfig;

public class VRRenderer implements GvrView.StereoRenderer
{
    public static final String TAG = "MANGO";

    private static final long SEND_TICK = 100;
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 10000.0f;

    private Context mContext;
    private PhotoQueue mPhotoStack;
    private Network mNetwork;

    private float matV[] = new float[16];
    private float matVP[] = new float[16];
    private float matCamera[] = new float[16];

    private Cylinder cylinder = new Cylinder(0.7f, 1.4f);
    private CylinderTexture cylinderTexture;
    private float mAngle = 0.0f;

    private long lastTime = System.currentTimeMillis();

    VRRenderer(Context context, PhotoQueue photoStack, Network network)
    {
        mContext = context;
        mPhotoStack = photoStack;
        mNetwork = network;
    }

    public float getAngle()
    {
        synchronized (this)
        {
            return mAngle;
        }
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig)
    {
        ShaderMgr.load(mContext);
        TextureMgr.load(mContext);

        cylinderTexture = new CylinderTexture(2 * 0.1211f);

        checkGLError("SurfaceCreated");
    }

    private float getHorizontalHeadAngle(HeadTransform headTransform)
    {
        float eulerAngle[] = new float[3];
        headTransform.getEulerAngles(eulerAngle, 0);

        return -0.5f - eulerAngle[1] / (float)Math.PI;
    }

    @Override
    public void onNewFrame(HeadTransform headTransform)
    {
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        float center[] = {0, 0, 0};
        float dir[] = {0, 0, 1};
        Matrix.setLookAtM(matCamera, 0, center[0], center[1], center[2], center[0] + dir[0], center[1] + dir[1], center[2] + dir[2], 0, 1, 0);

        synchronized (this)
        {
            mAngle = getHorizontalHeadAngle(headTransform);
        }

        if(lastTime + SEND_TICK < System.currentTimeMillis())
        {
            lastTime = System.currentTimeMillis();

            float eulerAngle[] = new float[3];
            headTransform.getEulerAngles(eulerAngle, 0);
            mNetwork.sendAngle(eulerAngle[1]);
        }

        if(!mPhotoStack.isEmpty())
        {
            float angle = getHorizontalHeadAngle(headTransform); //data.angle
            PhotoData data = mPhotoStack.pop();
            cylinderTexture.addPicture(data.bitmap, angle);
            data.bitmap.recycle();
        }

        checkGLError("NewFrame");
    }

    @Override
    public void onDrawEye(Eye eye)
    {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        Matrix.multiplyMM(matV, 0, Math3D.convertEyeMatrix(eye.getEyeView()), 0, matCamera, 0);
        Matrix.multiplyMM(matVP, 0, eye.getPerspective(Z_NEAR, Z_FAR), 0, matV, 0);

        float matM[] = new float[16];
        float matMVP[] = new float[16];

        Matrix.setIdentityM(matM, 0);
        Matrix.multiplyMM(matMVP, 0, matVP, 0, matM, 0);
        cylinderTexture.applyTexture();
        cylinder.draw(matMVP);

        checkGLError("DrawEye");
    }

    @Override
    public void onFinishFrame(Viewport viewport)
    {
    }

    @Override
    public void onSurfaceChanged(int i, int i1)
    {
    }

    @Override
    public void onRendererShutdown()
    {
    }

    public static void checkGLError(String label)
    {
        int error;

        if((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR)
        {
            String msg = label + " : glError " + Integer.toHexString(error);
            printError(msg);
            throw new RuntimeException(msg);
        }
    }

    public static void printLog(String msg) { Log.i(TAG, msg); }
    public static void printError(String msg) { Log.e(TAG, msg); }
}
