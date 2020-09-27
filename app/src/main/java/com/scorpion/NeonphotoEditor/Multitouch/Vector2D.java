package com.scorpion.NeonphotoEditor.Multitouch;

import android.graphics.PointF;

public class Vector2D extends PointF {
    public Vector2D() {
    }

    public Vector2D(float f, float f2) {
        super(f, f2);
    }

    public static float getAngle(Vector2D Vector2D1, Vector2D Vector2D2) {
        Vector2D1.normalize();
        Vector2D2.normalize();
        return (float) ((Math.atan2((double) Vector2D2.y, (double) Vector2D2.x) - Math.atan2((double) Vector2D2.y, (double) Vector2D2.x)) * 57.29577951308232d);
    }

    public void normalize() {
        float sqrt = (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y)));
        this.x /= sqrt;
        this.y /= sqrt;
    }
}
