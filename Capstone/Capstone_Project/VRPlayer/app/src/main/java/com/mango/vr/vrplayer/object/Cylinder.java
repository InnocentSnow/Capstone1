package com.mango.vr.vrplayer.object;

import android.opengl.GLES20;

import com.mango.vr.vrplayer.resource.ShaderMgr;
import com.mango.vr.vrplayer.resource.TextureMgr;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cylinder
{
    private static final int NUM_SEGMENT_H = 20;
    private static final int NUM_SEGMENT_V = 4;

    private FloatBuffer positionBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer texCoordBuffer;

    public Cylinder(float radius, float height)
    {
        final float DELTA_HEIGHT = height / NUM_SEGMENT_V;
        final double DELTA_ANGLE = 2 * Math.PI / NUM_SEGMENT_H;

        float x[] = new float[NUM_SEGMENT_H + 1];
        float z[] = new float[NUM_SEGMENT_H + 1];

        for(int i = 0; i <= NUM_SEGMENT_H; ++i)
        {
            double angle = DELTA_ANGLE * i;// - Math.PI / 2;

            x[i] = radius * (float)Math.cos(angle);
            z[i] = radius * (float)Math.sin(angle);
        }

        float position[] = new float[3 * (NUM_SEGMENT_H + 1) * (NUM_SEGMENT_V + 1)];
        float texCoord[] = new float[2 * (NUM_SEGMENT_H + 1) * (NUM_SEGMENT_V + 1)];

        int pos = 0;
        int tex = 0;

        for(int i = 0; i <= NUM_SEGMENT_V; ++i)
        {
            for(int j = 0; j <= NUM_SEGMENT_H; ++j)
            {
                position[pos++] = x[j];
                position[pos++] = DELTA_HEIGHT * i - height / 2;
                position[pos++] = z[j];

                texCoord[tex++] = (float)j / NUM_SEGMENT_H;
                texCoord[tex++] = (float)i / NUM_SEGMENT_V;
            }
        }

        positionBuffer = ByteBuffer.allocateDirect(4 * position.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        positionBuffer.put(position);
        positionBuffer.position(0);

        texCoordBuffer = ByteBuffer.allocateDirect(4 * texCoord.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
        texCoordBuffer.put(texCoord);
        texCoordBuffer.position(0);

        short indices[]	= new short[2 * NUM_SEGMENT_V * (NUM_SEGMENT_H + 1)];
        int idx = 0;

        for(int i = 0; i < NUM_SEGMENT_V; ++i)
        {
            for(int j = 0; j <= NUM_SEGMENT_H; ++j)
            {
                indices[idx++] = (short)(i * (NUM_SEGMENT_H + 1) + j);
                indices[idx++] = (short)((i + 1) * (NUM_SEGMENT_H + 1) + j);
            }
        }

        indexBuffer = ByteBuffer.allocateDirect(2 * indices.length).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    public void draw(float matMVP[])
    {
        int a_position	 = GLES20.glGetAttribLocation(ShaderMgr.texture, "a_position");
        int a_texCoord	 = GLES20.glGetAttribLocation(ShaderMgr.texture, "a_texCoord");
        int u_MVPMatrix	 = GLES20.glGetUniformLocation(ShaderMgr.texture, "u_MVPMatrix");
        int u_texture	 = GLES20.glGetUniformLocation(ShaderMgr.texture, "u_texture");

        GLES20.glUseProgram(ShaderMgr.texture);
        GLES20.glUniformMatrix4fv(u_MVPMatrix, 1, false, matMVP, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(u_texture, 0);

        positionBuffer.position(0);
        GLES20.glVertexAttribPointer(a_position, 3, GLES20.GL_FLOAT, false, 12, positionBuffer);
        GLES20.glEnableVertexAttribArray(a_position);

        texCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(a_texCoord, 2, GLES20.GL_FLOAT, false, 8, texCoordBuffer);
        GLES20.glEnableVertexAttribArray(a_texCoord);

        for(int i = 0; i < NUM_SEGMENT_V; ++i)
        {
            indexBuffer.position(2 * (NUM_SEGMENT_H + 1) * i);
            GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 2 * (NUM_SEGMENT_H + 1), GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        }

        TextureMgr.cancelTexture();
    }
}

