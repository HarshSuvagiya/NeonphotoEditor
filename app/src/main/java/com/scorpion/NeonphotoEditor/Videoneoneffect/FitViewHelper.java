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
        this.mScaleType = scaleType;
    }

    public boolean setAspectRatio(float f, int i, int i2) {
        if (((double) f) <= 0.0d || i < 0 || i2 < 0) {
            throw new IllegalArgumentException();
        } else if (this.mAspectRatio == f && this.mMaxWidth == i && this.mMaxHeight == i2) {
            return false;
        } else {
            this.mAspectRatio = f;
            this.mMaxWidth = i;
            this.mMaxHeight = i2;
            return calculatePreviewSize(0, 0);
        }
    }

    public boolean setRotate90Degrees(int i) {
        if (this.mAngle == i * 90) {
            return false;
        }
        while (i < 0) {
            i += 4;
        }
        this.mAngle = i * 90;
        return calculatePreviewSize(0, 0);
    }

    public float getAspectRatio() {
        return this.mAspectRatio;
    }

    public int getRotation90Degrees() {
        return this.mAngle / 90;
    }

    public int getPreviewWidth() {
        return this.mPreviewWidth;
    }

    public int getPreviewHeight() {
        return this.mPreviewHeight;
    }

    private Point fitCenter(int i, int i2) {
        if ((this.mAngle / 90) % 2 == 1) {
            if (this.mMaxWidth == 0 || this.mMaxHeight == 0) {
                float f = (float) i2;
                float f2 = (float) i;
                if (f > this.mAspectRatio * f2) {
                    double d = (double) (f2 * this.mAspectRatio);
                    Double.isNaN(d);
                    i2 = (int) (d + 0.5d);
                } else {
                    double d2 = (double) (f / this.mAspectRatio);
                    Double.isNaN(d2);
                    i = (int) (d2 + 0.5d);
                }
            } else {
                i = this.mMaxWidth;
                i2 = this.mMaxHeight;
                float f3 = (float) i2;
                float f4 = (float) i;
                if (f3 > this.mAspectRatio * f4) {
                    double d3 = (double) (f4 * this.mAspectRatio);
                    Double.isNaN(d3);
                    i2 = (int) (d3 + 0.5d);
                } else {
                    double d4 = (double) (f3 / this.mAspectRatio);
                    Double.isNaN(d4);
                    i = (int) (d4 + 0.5d);
                }
            }
        } else if (this.mMaxWidth == 0 || this.mMaxHeight == 0) {
            float f5 = (float) i;
            float f6 = (float) i2;
            if (f5 > this.mAspectRatio * f6) {
                double d5 = (double) (f6 * this.mAspectRatio);
                Double.isNaN(d5);
                i = (int) (d5 + 0.5d);
            } else {
                double d6 = (double) (f5 / this.mAspectRatio);
                Double.isNaN(d6);
                i2 = (int) (d6 + 0.5d);
            }
        } else {
            i = this.mMaxWidth;
            i2 = this.mMaxHeight;
            float f7 = (float) i;
            float f8 = (float) i2;
            if (f7 > this.mAspectRatio * f8) {
                double d7 = (double) (f8 * this.mAspectRatio);
                Double.isNaN(d7);
                i = (int) (d7 + 0.5d);
            } else {
                double d8 = (double) (f7 / this.mAspectRatio);
                Double.isNaN(d8);
                i2 = (int) (d8 + 0.5d);
            }
        }
        return new Point(i, i2);
    }

    private Point fitWidth(int i, int i2) {
        int i3 = 0;
        if ((this.mAngle / 90) % 2 == 1) {
            if (this.mMaxWidth != 0 && this.mMaxHeight != 0) {
                double d = (double) (((float) this.mMaxWidth) * this.mAspectRatio);
                Double.isNaN(d);
                i3 = (int) (d + 0.5d);
                i = this.mMaxWidth;
                return new Point(i, i3);
            } else if (!(i == 0 || i2 == 0)) {
                double d2 = (double) (((float) i) * this.mAspectRatio);
                Double.isNaN(d2);
                i3 = (int) (d2 + 0.5d);
                return new Point(i, i3);
            }
        } else if (this.mMaxWidth != 0 && this.mMaxHeight != 0) {
            i = this.mMaxWidth;
            double d3 = (double) (((float) this.mMaxWidth) / this.mAspectRatio);
            Double.isNaN(d3);
            i3 = (int) (d3 + 0.5d);
            return new Point(i, i3);
        } else if (!(i == 0 || i2 == 0)) {
            double d4 = (double) (((float) i) / this.mAspectRatio);
            Double.isNaN(d4);
            i3 = (int) (d4 + 0.5d);
            return new Point(i, i3);
        }
        i = 0;
        return new Point(i, i3);
    }

    private Point fitHeight(int i, int i2) {
        int i3 = 0;
        if ((this.mAngle / 90) % 2 == 1) {
            if (this.mMaxWidth != 0 && this.mMaxHeight != 0) {
                double d = (double) (((float) this.mMaxHeight) / this.mAspectRatio);
                Double.isNaN(d);
                i3 = (int) (d + 0.5d);
                i2 = this.mMaxHeight;
                return new Point(i3, i2);
            } else if (!(i == 0 || i2 == 0)) {
                double d2 = (double) (((float) i2) / this.mAspectRatio);
                Double.isNaN(d2);
                i3 = (int) (d2 + 0.5d);
                return new Point(i3, i2);
            }
        } else if (this.mMaxWidth != 0 && this.mMaxHeight != 0) {
            i2 = this.mMaxHeight;
            double d3 = (double) (((float) this.mMaxHeight) * this.mAspectRatio);
            Double.isNaN(d3);
            i3 = (int) (d3 + 0.5d);
            return new Point(i3, i2);
        } else if (!(i == 0 || i2 == 0)) {
            double d4 = (double) (((float) i2) * this.mAspectRatio);
            Double.isNaN(d4);
            i3 = (int) (d4 + 0.5d);
            return new Point(i3, i2);
        }
        i2 = 0;
        return new Point(i3, i2);
    }

    private Point centerCrop(int i, int i2) {
        if ((this.mAngle / 90) % 2 == 1) {
            if (this.mMaxWidth != 0 && this.mMaxHeight != 0) {
                if (this.mAspectRatio > (((float) this.mMaxHeight) * 1.0f) / ((float) this.mMaxWidth)) {
                    double d = (double) (((float) this.mMaxWidth) * this.mAspectRatio);
                    Double.isNaN(d);
                    i2 = (int) (d + 0.5d);
                    i = this.mMaxWidth;
                } else {
                    double d2 = (double) (((float) this.mMaxHeight) / this.mAspectRatio);
                    Double.isNaN(d2);
                    i = (int) (d2 + 0.5d);
                    i2 = this.mMaxHeight;
                }
                return new Point(i, i2);
            } else if (!(i == 0 || i2 == 0)) {
                float f = (float) i2;
                float f2 = (float) i;
                if (this.mAspectRatio > (1.0f * f) / f2) {
                    double d3 = (double) (f2 * this.mAspectRatio);
                    Double.isNaN(d3);
                    i2 = (int) (d3 + 0.5d);
                } else {
                    double d4 = (double) (f / this.mAspectRatio);
                    Double.isNaN(d4);
                    i = (int) (d4 + 0.5d);
                }
                return new Point(i, i2);
            }
        } else if (this.mMaxWidth != 0 && this.mMaxHeight != 0) {
            if (this.mAspectRatio > (((float) this.mMaxWidth) * 1.0f) / ((float) this.mMaxHeight)) {
                i2 = this.mMaxHeight;
                double d5 = (double) (((float) this.mMaxHeight) * this.mAspectRatio);
                Double.isNaN(d5);
                i = (int) (d5 + 0.5d);
            } else {
                i = this.mMaxWidth;
                double d6 = (double) (((float) this.mMaxWidth) / this.mAspectRatio);
                Double.isNaN(d6);
                i2 = (int) (d6 + 0.5d);
            }
            return new Point(i, i2);
        } else if (!(i == 0 || i2 == 0)) {
            float f3 = (float) i;
            float f4 = (float) i2;
            if (this.mAspectRatio > (1.0f * f3) / f4) {
                double d7 = (double) (f4 * this.mAspectRatio);
                Double.isNaN(d7);
                i = (int) (d7 + 0.5d);
            } else {
                double d8 = (double) (f3 / this.mAspectRatio);
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
        if (this.mScaleType == ScaleType.FIT_CENTER) {
            point = fitCenter(i, i2);
        } else if (this.mScaleType == ScaleType.FIT_WIDTH) {
            point = fitWidth(i, i2);
        } else if (this.mScaleType == ScaleType.FIT_HEIGHT) {
            point = fitHeight(i, i2);
        } else {
            point = centerCrop(i, i2);
        }
        boolean z = (point.x == this.mPreviewWidth && point.y == this.mPreviewHeight) ? false : true;
        this.mPreviewWidth = point.x;
        this.mPreviewHeight = point.y;
        return z;
    }
}
