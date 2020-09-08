package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import android.util.Log;
import io.reactivex.annotations.SchedulerSupport;

public class ShaderHelper {
    private static final String TAG = "Shader helper";

    public static int compileShader(String str, int i) {
        String str2 = SchedulerSupport.NONE;
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                str2 = GLES20.glGetShaderInfoLog(glCreateShader);
                GLES20.glDeleteShader(glCreateShader);
                glCreateShader = 0;
            }
        }
        if (glCreateShader != 0) {
            return glCreateShader;
        }
        throw new RuntimeException("failed to compile shader. Reason: " + str2);
    }

    public static int compileVertexShader(String str) {
        return compileShader(35633, str);
    }

    public static int compileFragmentShader(String str) {
        return compileShader(35632, str);
    }

    private static int compileShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader == 0) {
            Log.w(TAG, "Could not create new shader.");
            return 0;
        }
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateShader;
        }
        GLES20.glDeleteShader(glCreateShader);
        Log.w(TAG, "Compilation of shader failed.");
        return 0;
    }

    public static int linkProgram(int i, int i2, String[] strArr) {
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram == 0) {
            Log.w(TAG, "Could not create new program.");
            return 0;
        }
        GLES20.glAttachShader(glCreateProgram, i);
        GLES20.glAttachShader(glCreateProgram, i2);
        for (int i3 = 0; i3 < strArr.length; i3++) {
            GLES20.glBindAttribLocation(glCreateProgram, i3, strArr[i3]);
        }
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 0) {
            return glCreateProgram;
        }
        GLES20.glDeleteProgram(glCreateProgram);
        Log.w(TAG, "Linking of program failed.");
        return 0;
    }

    private static boolean validateProgram(int i) {
        GLES20.glValidateProgram(i);
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(i, 35715, iArr, 0);
        Log.v(TAG, "Results of validating program: " + iArr[0] + "\nLog: " + GLES20.glGetProgramInfoLog(i));
        if (iArr[0] != 0) {
            return true;
        }
        return false;
    }

    public static int buildProgram(String str, String str2, String[] strArr) {
        int linkProgram = linkProgram(compileVertexShader(str), compileFragmentShader(str2), strArr);
        validateProgram(linkProgram);
        return linkProgram;
    }
}
