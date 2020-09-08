package com.scorpion.NeonphotoEditor.Videoneoneffect.particle;

import android.graphics.Color;
import android.opengl.Matrix;
import java.util.Random;

public class ParticleShooter {
    private final float mAngleVariance;
    private int mBirthRate;
    private float[] mDirectionVector = new float[4];
    private final float mDuration;
    private final float mFromAngle;
    private final float mFromAngleVariance;
    private final int mFromColor;
    private final int mFromColorVariance;
    private final float mFromSize;
    private final float mFromSizeVariance;
    private Geometry.Point mPosition;
    private final Random mRandom = new Random();
    private float[] mResultVector = new float[4];
    private float[] mRotationMatrix = new float[16];
    private final float mSpeedVariance;
    private final int mTextureCount;
    private final float mToAngle;
    private final float mToAngleVariance;
    private final int mToColor;
    private final int mToColorVariance;
    private final float mToSize;
    private final float mToSizeVariance;

    public ParticleShooter(Geometry.Point point, int i, Geometry.Vector vector, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, int i2, int i3, int i4, int i5, float f11, int i6) {
        Geometry.Vector vector2 = vector;
        this.mPosition = point;
        this.mBirthRate = i;
        this.mDirectionVector[0] = vector2.x;
        this.mDirectionVector[1] = vector2.y;
        this.mDirectionVector[2] = vector2.z;
        this.mAngleVariance = f;
        this.mSpeedVariance = f2;
        this.mFromSize = f3;
        this.mFromSizeVariance = f4;
        this.mToSize = f5;
        this.mToSizeVariance = f6;
        this.mFromAngle = f7;
        this.mFromAngleVariance = f8;
        this.mToAngle = f9;
        this.mToAngleVariance = f10;
        this.mFromColor = i2;
        this.mFromColorVariance = i3;
        this.mToColor = i4;
        this.mToColorVariance = i5;
        this.mDuration = f11;
        this.mTextureCount = i6;
    }

    public void setPosition(Geometry.Point point) {
        this.mPosition = point;
    }



}
