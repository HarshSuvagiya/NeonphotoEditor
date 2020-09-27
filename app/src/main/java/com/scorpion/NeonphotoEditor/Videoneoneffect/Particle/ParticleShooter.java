package com.scorpion.NeonphotoEditor.Videoneoneffect.Particle;

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
        mPosition = point;
        mBirthRate = i;
        mDirectionVector[0] = vector2.x;
        mDirectionVector[1] = vector2.y;
        mDirectionVector[2] = vector2.z;
        mAngleVariance = f;
        mSpeedVariance = f2;
        mFromSize = f3;
        mFromSizeVariance = f4;
        mToSize = f5;
        mToSizeVariance = f6;
        mFromAngle = f7;
        mFromAngleVariance = f8;
        mToAngle = f9;
        mToAngleVariance = f10;
        mFromColor = i2;
        mFromColorVariance = i3;
        mToColor = i4;
        mToColorVariance = i5;
        mDuration = f11;
        mTextureCount = i6;
    }

    public void setPosition(Geometry.Point point) {
        mPosition = point;
    }



}
