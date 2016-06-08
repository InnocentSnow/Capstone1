package com.mango.vr.vrplayer.math;

import com.google.vr.sdk.base.sensors.internal.Vector3d;

public abstract class Math3D
{
    public static float[] convertEyeMatrix(float eye[])
    {
        float mat[] = new float[16];

        Vector3d right = new Vector3d(eye[0], eye[1], eye[2]);
        Vector3d front = new Vector3d(eye[8], eye[9], eye[10]);
        Vector3d up = new Vector3d(0, -1, 0);

        front.y = 0;
        front.normalize();
        Vector3d.cross(front, up, right);

        System.arraycopy(eye, 0, mat, 0, mat.length);
        mat[0] = (float)right.x;    mat[1] = (float)right.y;    mat[2] = (float)right.z;
        mat[4] = (float)up.x;       mat[5] = (float)up.y;       mat[6] = (float)up.z;
        mat[8] = (float)front.x;    mat[9] = (float)front.y;    mat[10] = (float)front.z;

        return mat;
    }
}
