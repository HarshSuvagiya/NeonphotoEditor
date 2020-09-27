package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.Point;

public class FitViewHelper {
    private int mAngle;
    private float mAspectRatio = 1.0f;
    private int mMaxHeight;
    private int mMaxWidth;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;

    public enum ScaleType {
        FIT_CENTER,
        FIT_WIDTH,
        FIT_HEIGHT,
        CENTER_CROP
    }

    public void setScaleType(ScaleType scaleType) {
        mScaleType = scaleType;
    }

    public boolean setAspectRatio(float f, int i, int i2) {
        if (((double) f) <= 0.0d || i < 0 || i2 < 0) {
            throw new IllegalArgumentException();
        } else if (mAspectRatio == f && mMaxWidth == i && mMaxHeight == i2) {
            return false;
        } else {
            mAspectRatio = f;
            mMaxWidth = i;
            mMaxHeight = i2;
            return calculatePreviewSize(0, 0);
        }
    }

    public boolean setRotate90Degrees(int i) {
        if (mAngle == i * 90) {
            return false;
        }
        while (i < 0) {
            i += 4;
        }
        mAngle = i * 90;
        return calculatePreviewSize(0, 0);
    }

    public float getAspectRatio() {
        return mAspectRatio;
    }

    public int getRotation90Degrees() {
        return mAngle / 90;
    }

    public int getPreviewWidth() {
        return mPreviewWidth;
    }

    public int getPreviewHeight() {
        return mPreviewHeight;
    }

    private Point fitCenter(int i, int i2) {
        if ((mAngle / 90) % 2 == 1) {
            if (mMaxWidth == 0 || mMaxHeight == 0) {
                float f = (float) i2;
                float f2 = (float) i;
                if (f > mAspectRatio * f2) {
                    double d = (double) (f2 * mAspectRatio);
                    Double.isNaN(d);
                    i2 = (int) (d + 0.5d);
                } else {
                    double d2 = (double) (f / mAspectRatio);
                    Double.isNaN(d2);
                    i = (int) (d2 + 0.5d);
                }
            } else {
                i = mMaxWidth;
                i2 = mMaxHeight;
                float f3 = (float) i2;
                float f4 = (float) i;
                if (f3 > mAspectRatio * f4) {
                    double d3 = (double) (f4 * mAspectRatio);
                    Double.isNaN(d3);
                    i2 = (int) (d3 + 0.5d);
                } else {
                    double d4 = (double) (f3 / mAspectRatio);
                    Double.isNaN(d4);
                    i = (int) (d4 + 0.5d);
                }
            }
        } else if (mMaxWidth == 0 || mMaxHeight == 0) {
            float f5 = (float) i;
            float f6 = (float) i2;
            if (f5 > mAspectRatio * f6) {
                double d5 = (double) (f6 * mAspectRatio);
                Double.isNaN(d5);
                i = (int) (d5 + 0.5d);
            } else {
                double d6 = (double) (f5 / mAspectRatio);
                Double.isNaN(d6);
                i2 = (int) (d6 + 0.5d);
            }
        } else {
            i = mMaxWidth;
            i2 = mMaxHeight;
            float f7 = (float) i;
            float f8 = (float) i2;
            if (f7 > mAspectRatio * f8) {
                double d7 = (double) (f8 * mAspectRatio);
                Double.isNaN(d7);
                i = (int) (d7 + 0.5d);
            } else {
                double d8 = (double) (f7 / mAspectRatio);
                Double.isNaN(d8);
                i2 = (int) (d8 + 0.5d);
            }
        }
        return new Point(i, i2);
    }

    private Point fitWidth(int i, int i2) {
        int i3 = 0;
        if ((mAngle / 90) % 2 == 1) {
            if (mMaxWidth != 0 && mMaxHeight != 0) {
                double d = (double) (((float) mMaxWidth) * mAspectRatio);
                Double.isNaN(d);
                i3 = (int) (d + 0.5d);
                i = mMaxWidth;
                return new Point(i, i3);
            } else if (!(i == 0 || i2 == 0)) {
                double d2 = (double) (((float) i) * mAspectRatio);
                Double.isNaN(d2);
                i3 = (int) (d2 + 0.5d);
                return new Point(i, i3);
            }
        } else if (mMaxWidth != 0 && mMaxHeight != 0) {
            i = mMaxWidth;
            double d3 = (double) (((float) mMaxWidth) / mAspectRatio);
            Double.isNaN(d3);
            i3 = (int) (d3 + 0.5d);
            return new Point(i, i3);
        } else if (!(i == 0 || i2 == 0)) {
            double d4 = (double) (((float) i) / mAspectRatio);
            Double.isNaN(d4);
            i3 = (int) (d4 + 0.5d);
            return new Point(i, i3);
        }
        i = 0;
        return new Point(i, i3);
    }

    private Point fitHeight(int i, int i2) {
        int i3 = 0;
        if ((mAngle / 90) % 2 == 1) {
            if (mMaxWidth != 0 && mMaxHeight != 0) {
                double d = (double) (((float) mMaxHeight) / mAspectRatio);
                Double.isNaN(d);
                i3 = (int) (d + 0.5d);
                i2 = mMaxHeight;
                return new Point(i3, i2);
            } else if (!(i == 0 || i2 == 0)) {
                double d2 = (double) (((float) i2) / mAspectRatio);
                Double.isNaN(d2);
                i3 = (int) (d2 + 0.5d);
                return new Point(i3, i2);
            }
        } else if (mMaxWidth != 0 && mMaxHeight != 0) {
            i2 = mMaxHeight;
            double d3 = (double) (((float) mMaxHeight) * mAspectRatio);
            Double.isNaN(d3);
            i3 = (int) (d3 + 0.5d);
            return new Point(i3, i2);
        } else if (!(i == 0 || i2 == 0)) {
            double d4 = (double) (((float) i2) * mAspectRatio);
            Double.isNaN(d4);
            i3 = (int) (d4 + 0.5d);
            return new Point(i3, i2);
        }
        i2 = 0;
        return new Point(i3, i2);
    }

    private Point centerCrop(int i, int i2) {
        if ((mAngle / 90) % 2 == 1) {
            if (mMaxWidth != 0 && mMaxHeight != 0) {
                if (mAspectRatio > (((float) mMaxHeight) * 1.0f) / ((float) mMaxWidth)) {
                    double d = (double) (((float) mMaxWidth) * mAspectRatio);
                    Double.isNaN(d);
                    i2 = (int) (d + 0.5d);
                    i = mMaxWidth;
                } else {
                    double d2 = (double) (((float) mMaxHeight) / mAspectRatio);
                    Double.isNaN(d2);
                    i = (int) (d2 + 0.5d);
                    i2 = mMaxHeight;
                }
                return new Point(i, i2);
            } else if (!(i == 0 || i2 == 0)) {
                float f = (float) i2;
                float f2 = (float) i;
                if (mAspectRatio > (1.0f * f) / f2) {
                    double d3 = (double) (f2 * mAspectRatio);
                    Double.isNaN(d3);
                    i2 = (int) (d3 + 0.5d);
                } else {
                    double d4 = (double) (f / mAspectRatio);
                    Double.isNaN(d4);
                    i = (int) (d4 + 0.5d);
                }
                return new Point(i, i2);
            }
        } else if (mMaxWidth != 0 && mMaxHeight != 0) {
            if (mAspectRatio > (((float) mMaxWidth) * 1.0f) / ((float) mMaxHeight)) {
                i2 = mMaxHeight;
                double d5 = (double) (((float) mMaxHeight) * mAspectRatio);
                Double.isNaN(d5);
                i = (int) (d5 + 0.5d);
            } else {
                i = mMaxWidth;
                double d6 = (double) (((float) mMaxWidth) / mAspectRatio);
                Double.isNaN(d6);
                i2 = (int) (d6 + 0.5d);
            }
            return new Point(i, i2);
        } else if (!(i == 0 || i2 == 0)) {
            float f3 = (float) i;
            float f4 = (float) i2;
            if (mAspectRatio > (1.0f * f3) / f4) {
                double d7 = (double) (f4 * mAspectRatio);
                Double.isNaN(d7);
                i = (int) (d7 + 0.5d);
            } else {
                double d8 = (double) (f3 / mAspectRatio);
                Double.isNaN(d8);
                i2 = (int) (d8 + 0.5d);
            }
            return new Point(i, i2);
        }
        i = 0;
        i2 = 0;
        return new Point(i, i2);
    }

    /* access modifiers changed from: protected */
    public boolean calculatePreviewSize(int i, int i2) {
        Point point;
        if (mScaleType == ScaleType.FIT_CENTER) {
            point = fitCenter(i, i2);
        } else if (mScaleType == ScaleType.FIT_WIDTH) {
            point = fitWidth(i, i2);
        } else if (mScaleType == ScaleType.FIT_HEIGHT) {
            point = fitHeight(i, i2);
        } else {
            point = centerCrop(i, i2);
        }
        boolean z = (point.x == mPreviewWidth && point.y == mPreviewHeight) ? false : true;
        mPreviewWidth = point.x;
        mPreviewHeight = point.y;
        return z;
    }
}
