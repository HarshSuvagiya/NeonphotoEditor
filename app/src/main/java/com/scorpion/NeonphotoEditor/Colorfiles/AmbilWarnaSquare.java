package com.scorpion.NeonphotoEditor.Colorfiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;

public class AmbilWarnaSquare extends View {
    final float[] color = {1.0f, 1.0f, 1.0f};
    Shader luar;
    Paint paint;

    public AmbilWarnaSquare(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AmbilWarnaSquare(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            luar = new LinearGradient(0.0f, 0.0f, 0.0f, (float) getMeasuredHeight(), -1, ViewCompat.MEASURED_STATE_MASK, Shader.TileMode.CLAMP);
        }
        paint.setShader(new ComposeShader(luar, new LinearGradient(0.0f, 0.0f, (float) getMeasuredWidth(), 0.0f, -1, Color.HSVToColor(color), Shader.TileMode.CLAMP), PorterDuff.Mode.MULTIPLY));
        canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), paint);
    }

    public void setHue(float f) {
        color[0] = f;
        invalidate();
    }
}
