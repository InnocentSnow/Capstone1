package com.mango.vr.vrplayer.resource;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.mango.vr.vrplayer.R;
import com.mango.vr.vrplayer.vr.MainActivity;
import com.mango.vr.vrplayer.vr.VRRenderer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class ShaderMgr
{
	public static int simple;
	public static int texture;
	public static int cylinder_add;

	public static void load(Context context)
	{
		simple	= createProgram(context, R.raw.simple_vs,	R.raw.simple_fs);
		texture	= createProgram(context, R.raw.texture_vs,	R.raw.texture_fs);
        cylinder_add = createProgram(context, R.raw.cylinder_add_vs, R.raw.cylinder_add_fs);
	}

	private static int createProgram(Context context, int vertexShaderId, int fragmentShaderId)
	{
		int[] linked = new int[1];
		int vertexShader = loadShader(context, GLES20.GL_VERTEX_SHADER, vertexShaderId);
		int fragmentShader = loadShader(context, GLES20.GL_FRAGMENT_SHADER, fragmentShaderId);
		if(vertexShader == 0 || fragmentShader == 0) return 0;

		int program = GLES20.glCreateProgram();
		if(program == 0) return 0;

		GLES20.glAttachShader(program, vertexShader);
		GLES20.glAttachShader(program, fragmentShader);
		GLES20.glLinkProgram(program);

		// Link the program
		GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linked, 0);

		if(linked[0] == 0)
        {
            VRRenderer.printError("Error linking program <" + Integer.toString(vertexShaderId) + ", " + Integer.toString(fragmentShaderId) + "> :");
            VRRenderer.printError(GLES20.glGetProgramInfoLog(program));
			GLES20.glDeleteProgram(program);

			return 0;
		}

		return program;
	}

	private static int loadShader(Context context, int type, int shaderId)
	{
		String shaderSource = readShaderSource(context, shaderId);

		int shader = GLES20.glCreateShader(type);
		int[] compiled = new int[1];

		if(shader == 0) return 0;

		GLES20.glShaderSource(shader, shaderSource);
		GLES20.glCompileShader(shader);
		GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);

		if(compiled[0] == 0)
		{
			VRRenderer.printError("<" + Integer.toString(shaderId) + "> " + GLES20.glGetShaderInfoLog(shader));
			GLES20.glDeleteShader(shader);
			return 0;
		}

		return shader;
	}

	private static String readShaderSource(Context context, int shaderID)
	{
		StringBuilder shaderBuilder = new StringBuilder();

		try
		{
			InputStream inputStream = context.getResources().openRawResource(shaderID);
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			String read = in.readLine();
			while (read != null)
			{
				shaderBuilder.append(read).append("\n");
				read = in.readLine();
			}

			shaderBuilder.deleteCharAt(shaderBuilder.length() - 1);
			inputStream.close();
		}
		catch (Exception e)
		{
            VRRenderer.printError("Could not read shader <" + Integer.toString(shaderID) + "> : " + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return shaderBuilder.toString();
	}

}
