package com.scorpion.NeonphotoEditor.Multitouch;

import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;

public class MultiTouchListener implements View.OnTouchListener {
    private static final int INVALID_POINTER_ID = -1;
    private ArrayList<View> alview = new ArrayList<>();
    public boolean isRotateEnabled = true;
    public boolean isScaleEnabled = true;
    public boolean isTranslateEnabled = true;
    private int mActivePointerId = -1;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
    public float maximumScale = 10.0f;
    public float minimumScale = 0.5f;

    private static float adjustAngle(float f) {
        return f > 180.0f ? f - 360.0f : f < -180.0f ? f + 360.0f : f;
    }


    public static void move(View view, TransformInfo transformInfo) {
        computeRenderOffset(view, transformInfo.pivotX, transformInfo.pivotY);
        adjustTranslation(view, transformInfo.deltaX, transformInfo.deltaY);
        float max = Math.max(transformInfo.minimumScale, Math.min(transformInfo.maximumScale, view.getScaleX() * transformInfo.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        view.setRotation(adjustAngle(view.getRotation() + transformInfo.deltaAngle));
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[0] - fArr[0];
            float f4 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - f3);
            view.setTranslationY(view.getTranslationY() - f4);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        Iterator<View> it = alview.iterator();
        while (it.hasNext()) {
            onTouch2(it.next(), motionEvent);
        }
        mScaleGestureDetector.onTouchEvent(view, motionEvent);
        if (!isTranslateEnabled) {
            return true;
        }
        int action = motionEvent.getAction();
        int actionMasked = motionEvent.getActionMasked() & action;
        int i = 0;
        if (actionMasked != 6) {
            switch (actionMasked) {
                case 0:
                    mPrevX = motionEvent.getX();
                    mPrevY = motionEvent.getY();
                    mActivePointerId = motionEvent.getPointerId(0);
                    break;
                case 1:
                    mActivePointerId = -1;
                    view.performClick();
                    break;
                case 2:
                    int findPointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                    if (findPointerIndex != -1) {
                        float x = motionEvent.getX(findPointerIndex);
                        float y = motionEvent.getY(findPointerIndex);
                        if (!mScaleGestureDetector.isInProgress()) {
                            adjustTranslation(view, x - mPrevX, y - mPrevY);
                            break;
                        }
                    }
                    break;
                case 3:
                    mActivePointerId = -1;
                    break;
            }
        } else {
            int i2 = (65280 & action) >> 8;
            if (motionEvent.getPointerId(i2) == mActivePointerId) {
                if (i2 == 0) {
                    i = 1;
                }
                mPrevX = motionEvent.getX(i);
                mPrevY = motionEvent.getY(i);
                mActivePointerId = motionEvent.getPointerId(i);
            }
        }
        return true;
    }

    public boolean onTouch2(View view, MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(view, motionEvent);
        if (!isTranslateEnabled) {
            return true;
        }
        int action = motionEvent.getAction();
        int actionMasked = motionEvent.getActionMasked() & action;
        int i = 0;
        if (actionMasked != 6) {
            switch (actionMasked) {
                case 0:
                    mPrevX = motionEvent.getX();
                    mPrevY = motionEvent.getY();
                    mActivePointerId = motionEvent.getPointerId(0);
                    break;
                case 1:
                    mActivePointerId = -1;
                    break;
                case 2:
                    int findPointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                    if (findPointerIndex != -1) {
                        float x = motionEvent.getX(findPointerIndex);
                        float y = motionEvent.getY(findPointerIndex);
                        if (!mScaleGestureDetector.isInProgress()) {
                            adjustTranslation(view, x - mPrevX, y - mPrevY);
                            break;
                        }
                    }
                    break;
                case 3:
                    mActivePointerId = -1;
                    break;
            }
        } else {
            int i2 = (65280 & action) >> 8;
            if (motionEvent.getPointerId(i2) == mActivePointerId) {
                if (i2 == 0) {
                    i = 1;
                }
                mPrevX = motionEvent.getX(i);
                mPrevY = motionEvent.getY(i);
                mActivePointerId = motionEvent.getPointerId(i);
            }
        }
        return true;
    }

    public void addSimultaneousView(View view) {
        alview.add(view);
    }

    public void removeAll() {
        alview.clear();
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector ScaleGestureDetector1) {
            mPivotX = ScaleGestureDetector1.getFocusX();
            mPivotY = ScaleGestureDetector1.getFocusY();
            mPrevSpanVector.set(ScaleGestureDetector1.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector ScaleGestureDetector1) {
            TransformInfo transformInfo = new TransformInfo();
            transformInfo.deltaScale = isScaleEnabled ? ScaleGestureDetector1.getScaleFactor() : 1.0f;
            float f = 0.0f;
            transformInfo.deltaAngle = isRotateEnabled ? Vector2D.getAngle(mPrevSpanVector, ScaleGestureDetector1.getCurrentSpanVector()) : 0.0f;
            transformInfo.deltaX = isTranslateEnabled ? ScaleGestureDetector1.getFocusX() - mPivotX : 0.0f;
            if (isTranslateEnabled) {
                f = ScaleGestureDetector1.getFocusY() - mPivotY;
            }
            transformInfo.deltaY = f;
            transformInfo.pivotX = mPivotX;
            transformInfo.pivotY = mPivotY;
            transformInfo.minimumScale = minimumScale;
            transformInfo.maximumScale = maximumScale;
            MultiTouchListener.move(view, transformInfo);
            return false;
        }
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }
}
