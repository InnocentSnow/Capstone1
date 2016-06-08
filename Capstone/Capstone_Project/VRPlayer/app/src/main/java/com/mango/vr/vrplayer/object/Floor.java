package com.mango.vr.vrplayer.object;

import android.opengl.GLES20;

import com.mango.vr.vrplayer.resource.ShaderMgr;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Floor
{
	private FloatBuffer positionBuffer;
	private ShortBuffer indexBuffer;
	private int numIndices;

	public Floor()
	{
		float w = 10.0f;
		float h = 10.0f;

		float position[] = {
			-w, 0.0f, -h,
			+w, 0.0f, -h,
			-w, 0.0f, +h,
			+w, 0.0f, +h
		};

		positionBuffer = ByteBuffer.allocateDirect(4 * position.length).order(ByteOrder.nativeOrder()).asFloatBuffer();
		positionBuffer.put(position);
		positionBuffer.position(0);

		short indices[]	= {0, 1, 2, 3};

		indexBuffer = ByteBuffer.allocateDirect(2 * indices.length).order(ByteOrder.nativeOrder()).asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

		numIndices	= indices.length;
	}

	public void draw(float matMVP[])
	{
		int a_position	 = GLES20.glGetAttribLocation(ShaderMgr.simple, "a_position");
		int u_MVPMatrix	 = GLES20.glGetUniformLocation(ShaderMgr.simple, "u_MVPMatrix");
		int u_color		 = GLES20.glGetUniformLocation(ShaderMgr.simple, "u_color");

		GLES20.glUseProgram(ShaderMgr.simple);
		GLES20.glUniform4f(u_color, 0.0f, 1.0f, 1.0f, 0.5f);
        GLES20.glUniformMatrix4fv(u_MVPMatrix, 1, false, matMVP, 0);

		positionBuffer.position(0);
		GLES20.glVertexAttribPointer(a_position, 3, GLES20.GL_FLOAT, false, 12, positionBuffer);
		GLES20.glEnableVertexAttribArray(a_position);

		indexBuffer.position(0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, numIndices, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
	}
}
