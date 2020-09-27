package com.scorpion.NeonphotoEditor.Multitouch;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ScaleGestureDetector {
    private static final float PRESSURE_THRESHOLD = 0.67f;
    private static final String TAG = "ScaleGestureDetector";
    private boolean mActive0MostRecent;
    private int mActiveId0;
    private int mActiveId1;
    private MotionEvent mCurrEvent;
    private float mCurrFingerDiffX;
    private float mCurrFingerDiffY;
    private float mCurrLen;
    private float mCurrPressure;
    private Vector2D mCurrSpanVector = new Vector2D();
    private float mFocusX;
    private float mFocusY;
    private boolean mGestureInProgress;
    private boolean mInvalidGesture;
    private final OnScaleGestureListener mListener;
    private MotionEvent mPrevEvent;
    private float mPrevFingerDiffX;
    private float mPrevFingerDiffY;
    private float mPrevLen;
    private float mPrevPressure;
    private float mScaleFactor;
    private long mTimeDelta;

    public interface OnScaleGestureListener {
        boolean onScale(View view, ScaleGestureDetector ScaleGestureDetector1);

        boolean onScaleBegin(View view, ScaleGestureDetector ScaleGestureDetector1);

        void onScaleEnd(View view, ScaleGestureDetector ScaleGestureDetector1);
    }

    public static class SimpleOnScaleGestureListener implements OnScaleGestureListener {
        public boolean onScale(View view, ScaleGestureDetector ScaleGestureDetector1) {
            return false;
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector ScaleGestureDetector1) {
            return true;
        }

        public void onScaleEnd(View view, ScaleGestureDetector fX_ScaleGestureDetector) {
        }
    }

    public ScaleGestureDetector(OnScaleGestureListener onScaleGestureListener) {
        mListener = onScaleGestureListener;
    }

    public boolean onTouchEvent(View view, MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            reset();
        }
        boolean z = false;
        if (mInvalidGesture) {
            return false;
        }
        if (mGestureInProgress) {
            switch (actionMasked) {
                case 1:
                    reset();
                    break;
                case 2:
                    setContext(view, motionEvent);
                    if (mCurrPressure / mPrevPressure > PRESSURE_THRESHOLD && mListener.onScale(view, this)) {
                        mPrevEvent.recycle();
                        mPrevEvent = MotionEvent.obtain(motionEvent);
                        break;
                    }
                case 3:
                    mListener.onScaleEnd(view, this);
                    reset();
                    break;
                case 5:
                    mListener.onScaleEnd(view, this);
                    int i = mActiveId0;
                    int i2 = mActiveId1;
                    reset();
                    mPrevEvent = MotionEvent.obtain(motionEvent);
                    if (!mActive0MostRecent) {
                        i = i2;
                    }
                    mActiveId0 = i;
                    mActiveId1 = motionEvent.getPointerId(motionEvent.getActionIndex());
                    mActive0MostRecent = false;
                    if (motionEvent.findPointerIndex(mActiveId0) < 0 || mActiveId0 == mActiveId1) {
                        mActiveId0 = motionEvent.getPointerId(findNewActiveIndex(motionEvent, mActiveId1, -1));
                    }
                    setContext(view, motionEvent);
                    mGestureInProgress = mListener.onScaleBegin(view, this);
                    break;
                case 6:
                    int pointerCount = motionEvent.getPointerCount();
                    int actionIndex = motionEvent.getActionIndex();
                    int pointerId = motionEvent.getPointerId(actionIndex);
                    if (pointerCount > 2) {
                        if (pointerId == mActiveId0) {
                            int findNewActiveIndex = findNewActiveIndex(motionEvent, mActiveId1, actionIndex);
                            if (findNewActiveIndex >= 0) {
                                mListener.onScaleEnd(view, this);
                                mActiveId0 = motionEvent.getPointerId(findNewActiveIndex);
                                mActive0MostRecent = true;
                                mPrevEvent = MotionEvent.obtain(motionEvent);
                                setContext(view, motionEvent);
                                mGestureInProgress = mListener.onScaleBegin(view, this);
                                mPrevEvent.recycle();
                                mPrevEvent = MotionEvent.obtain(motionEvent);
                                setContext(view, motionEvent);
                            }
                        } else {
                            if (pointerId == mActiveId1) {
                                int findNewActiveIndex2 = findNewActiveIndex(motionEvent, mActiveId0, actionIndex);
                                if (findNewActiveIndex2 >= 0) {
                                    mListener.onScaleEnd(view, this);
                                    mActiveId1 = motionEvent.getPointerId(findNewActiveIndex2);
                                    mActive0MostRecent = false;
                                    mPrevEvent = MotionEvent.obtain(motionEvent);
                                    setContext(view, motionEvent);
                                    mGestureInProgress = mListener.onScaleBegin(view, this);
                                }
                            }
                            mPrevEvent.recycle();
                            mPrevEvent = MotionEvent.obtain(motionEvent);
                            setContext(view, motionEvent);
                        }
                        z = true;
                        mPrevEvent.recycle();
                        mPrevEvent = MotionEvent.obtain(motionEvent);
                        setContext(view, motionEvent);
                    } else {
                        z = true;
                    }
                    if (z) {
                        setContext(view, motionEvent);
                        int i3 = pointerId == mActiveId0 ? mActiveId1 : mActiveId0;
                        int findPointerIndex = motionEvent.findPointerIndex(i3);
                        mFocusX = motionEvent.getX(findPointerIndex);
                        mFocusY = motionEvent.getY(findPointerIndex);
                        mListener.onScaleEnd(view, this);
                        reset();
                        mActiveId0 = i3;
                        mActive0MostRecent = true;
                        break;
                    }
                    break;
            }
        } else if (actionMasked != 5) {
            switch (actionMasked) {
                case 0:
                    mActiveId0 = motionEvent.getPointerId(0);
                    mActive0MostRecent = true;
                    break;
                case 1:
                    reset();
                    break;
            }
        } else {
            if (mPrevEvent != null) {
                mPrevEvent.recycle();
            }
            mPrevEvent = MotionEvent.obtain(motionEvent);
            mTimeDelta = 0;
            int actionIndex2 = motionEvent.getActionIndex();
            int findPointerIndex2 = motionEvent.findPointerIndex(mActiveId0);
            mActiveId1 = motionEvent.getPointerId(actionIndex2);
            if (findPointerIndex2 < 0 || findPointerIndex2 == actionIndex2) {
                mActiveId0 = motionEvent.getPointerId(findNewActiveIndex(motionEvent, mActiveId1, -1));
            }
            mActive0MostRecent = false;
            setContext(view, motionEvent);
            mGestureInProgress = mListener.onScaleBegin(view, this);
        }
        return true;
    }

    private int findNewActiveIndex(MotionEvent motionEvent, int i, int i2) {
        int pointerCount = motionEvent.getPointerCount();
        int findPointerIndex = motionEvent.findPointerIndex(i);
        for (int i3 = 0; i3 < pointerCount; i3++) {
            if (i3 != i2 && i3 != findPointerIndex) {
                return i3;
            }
        }
        return -1;
    }

    private void setContext(View view, MotionEvent motionEvent) {
        if (mCurrEvent != null) {
            mCurrEvent.recycle();
        }
        mCurrEvent = MotionEvent.obtain(motionEvent);
        mCurrLen = -1.0f;
        mPrevLen = -1.0f;
        mScaleFactor = -1.0f;
        mCurrSpanVector.set(0.0f, 0.0f);
        MotionEvent motionEvent2 = mPrevEvent;
        int findPointerIndex = motionEvent2.findPointerIndex(mActiveId0);
        int findPointerIndex2 = motionEvent2.findPointerIndex(mActiveId1);
        int findPointerIndex3 = motionEvent.findPointerIndex(mActiveId0);
        int findPointerIndex4 = motionEvent.findPointerIndex(mActiveId1);
        if (findPointerIndex < 0 || findPointerIndex2 < 0 || findPointerIndex3 < 0 || findPointerIndex4 < 0) {
            mInvalidGesture = true;
            Log.e(TAG, "Invalid MotionEvent stream detected.", new Throwable());
            if (mGestureInProgress) {
                mListener.onScaleEnd(view, this);
                return;
            }
            return;
        }
        float x = motionEvent2.getX(findPointerIndex);
        float y = motionEvent2.getY(findPointerIndex);
        float x2 = motionEvent2.getX(findPointerIndex2);
        float y2 = motionEvent2.getY(findPointerIndex2);
        float x3 = motionEvent.getX(findPointerIndex3);
        float y3 = motionEvent.getY(findPointerIndex3);
        float x4 = motionEvent.getX(findPointerIndex4) - x3;
        float y4 = motionEvent.getY(findPointerIndex4) - y3;
        mCurrSpanVector.set(x4, y4);
        mPrevFingerDiffX = x2 - x;
        mPrevFingerDiffY = y2 - y;
        mCurrFingerDiffX = x4;
        mCurrFingerDiffY = y4;
        mFocusX = x3 + (x4 * 0.5f);
        mFocusY = y3 + (y4 * 0.5f);
        mTimeDelta = motionEvent.getEventTime() - motionEvent2.getEventTime();
        mCurrPressure = motionEvent.getPressure(findPointerIndex3) + motionEvent.getPressure(findPointerIndex4);
        mPrevPressure = motionEvent2.getPressure(findPointerIndex) + motionEvent2.getPressure(findPointerIndex2);
    }

    private void reset() {
        if (mPrevEvent != null) {
            mPrevEvent.recycle();
            mPrevEvent = null;
        }
        if (mCurrEvent != null) {
            mCurrEvent.recycle();
            mCurrEvent = null;
        }
        mGestureInProgress = false;
        mActiveId0 = -1;
        mActiveId1 = -1;
        mInvalidGesture = false;
    }

    public boolean isInProgress() {
        return mGestureInProgress;
    }

    public float getFocusX() {
        return mFocusX;
    }

    public float getFocusY() {
        return mFocusY;
    }

    public float getCurrentSpan() {
        if (mCurrLen == -1.0f) {
            float f = mCurrFingerDiffX;
            float f2 = mCurrFingerDiffY;
            mCurrLen = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
        }
        return mCurrLen;
    }

    public Vector2D getCurrentSpanVector() {
        return mCurrSpanVector;
    }

    public float getCurrentSpanX() {
        return mCurrFingerDiffX;
    }

    public float getCurrentSpanY() {
        return mCurrFingerDiffY;
    }

    public float getPreviousSpan() {
        if (mPrevLen == -1.0f) {
            float f = mPrevFingerDiffX;
            float f2 = mPrevFingerDiffY;
            mPrevLen = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
        }
        return mPrevLen;
    }

    public float getPreviousSpanX() {
        return mPrevFingerDiffX;
    }

    public float getPreviousSpanY() {
        return mPrevFingerDiffY;
    }

    public float getScaleFactor() {
        if (mScaleFactor == -1.0f) {
            mScaleFactor = getCurrentSpan() / getPreviousSpan();
        }
        return mScaleFactor;
    }

    public long getTimeDelta() {
        return mTimeDelta;
    }

    public long getEventTime() {
        return mCurrEvent.getEventTime();
    }
}
