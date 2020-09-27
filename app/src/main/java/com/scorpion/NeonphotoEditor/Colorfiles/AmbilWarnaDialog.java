package com.scorpion.NeonphotoEditor.Colorfiles;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;

import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

public class AmbilWarnaDialog {
    int alpha;
    LinearLayout ambilState;
    Context context;
    final float[] currentColorHsv;
    final Dialog dialog;
    FrameLayout fb1;
    FrameLayout fb2;
    int height;
    ImageView ivarrow;
    ImageView ivcancel;
    ImageView ivclose;
    ImageView ivok;
    ImageView ivtrans;
    LinearLayout linearbtn;
    final OnAmbilWarnaListener listener;
    LinearLayout lmain;
    RelativeLayout reltitle;
    RelativeLayout reltrans;
    public final boolean supportsAlpha;
    Boolean transparent;
    TextView tvtitle;
    final ImageView viewAlphaCheckered;
    final ImageView viewAlphaCursor;
    final View viewAlphaOverlay;
    final ViewGroup viewContainer;
    final ImageView viewCursor;
    final View viewHue;
    final View viewNewColor;
    final View viewOldColor;
    final AmbilWarnaSquare viewSatVal;
    final ImageView viewTarget;
    View vline;
    int width;

    public interface OnAmbilWarnaListener {
        void onCancel(AmbilWarnaDialog AmbilWarnaDialog1);

        void onOk(AmbilWarnaDialog AmbilWarnaDialog1, int i);
    }

    public AmbilWarnaDialog(Context context2, Boolean bool, int i, OnAmbilWarnaListener onAmbilWarnaListener) {
        this(context2, bool, i, false, onAmbilWarnaListener);
    }

    public AmbilWarnaDialog(Context context2, Boolean bool, int i, boolean z, OnAmbilWarnaListener onAmbilWarnaListener) {
        currentColorHsv = new float[3];
        supportsAlpha = z;
        listener = onAmbilWarnaListener;
        transparent = bool;
        context = context2;
        i = !z ? i | ViewCompat.MEASURED_STATE_MASK : i;
        Color.colorToHSV(i, currentColorHsv);
        alpha = Color.alpha(i);
        final View inflate = LayoutInflater.from(context2).inflate(R.layout.color_dialog, (ViewGroup) null);
        viewHue = inflate.findViewById(R.id.ambilwarna_viewHue);
        viewSatVal = (AmbilWarnaSquare) inflate.findViewById(R.id.ambilwarna_viewSatBri);
        viewCursor = (ImageView) inflate.findViewById(R.id.ambilwarna_cursor);
        viewOldColor = inflate.findViewById(R.id.ambilwarna_oldColor);
        viewNewColor = inflate.findViewById(R.id.ambilwarna_newColor);
        viewTarget = (ImageView) inflate.findViewById(R.id.ambilwarna_target);
        viewContainer = (ViewGroup) inflate.findViewById(R.id.ambilwarna_viewContainer);
        viewAlphaOverlay = inflate.findViewById(R.id.ambilwarna_overlay);
        viewAlphaCursor = (ImageView) inflate.findViewById(R.id.ambilwarna_alphaCursor);
        viewAlphaCheckered = (ImageView) inflate.findViewById(R.id.ambilwarna_alphaCheckered);
        ivok = (ImageView) inflate.findViewById(R.id.ivok);
        ivcancel = (ImageView) inflate.findViewById(R.id.ivcancel);
        ivclose = (ImageView) inflate.findViewById(R.id.ivclose);
        ivarrow = (ImageView) inflate.findViewById(R.id.ivcarrow);
        lmain = (LinearLayout) inflate.findViewById(R.id.lineardialog);
        reltitle = (RelativeLayout) inflate.findViewById(R.id.reltitle);
        tvtitle = (TextView) inflate.findViewById(R.id.tvtitle);
        fb1 = (FrameLayout) inflate.findViewById(R.id.framebox1);
        fb2 = (FrameLayout) inflate.findViewById(R.id.framebox2);
        ambilState = (LinearLayout) inflate.findViewById(R.id.ambilwarna_state);
        vline = inflate.findViewById(R.id.viewline);
        linearbtn = (LinearLayout) inflate.findViewById(R.id.linearbtn);
        reltrans = (RelativeLayout) inflate.findViewById(R.id.reltrans);
        ivtrans = (ImageView) inflate.findViewById(R.id.ivtrans);
        width = context2.getResources().getDisplayMetrics().widthPixels;
        height = context2.getResources().getDisplayMetrics().heightPixels;
        viewAlphaOverlay.setVisibility(z ? View.VISIBLE : View.GONE);
        viewAlphaCursor.setVisibility(z ? View.VISIBLE : View.GONE);
        viewAlphaCheckered.setVisibility(z ? View.VISIBLE : View.GONE);
        viewSatVal.setHue(getHue());
        viewOldColor.setBackgroundColor(i);
        viewNewColor.setBackgroundColor(i);
        viewHue.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                    return false;
                }
                float y = motionEvent.getY();
                if (y < 0.0f) {
                    y = 0.0f;
                }
                if (y > ((float) viewHue.getMeasuredHeight())) {
                    y = ((float) viewHue.getMeasuredHeight()) - 0.001f;
                }
                float measuredHeight = 360.0f - ((360.0f / ((float) viewHue.getMeasuredHeight())) * y);
                if (measuredHeight == 360.0f) {
                    measuredHeight = 0.0f;
                }
                setHue(measuredHeight);
                viewSatVal.setHue(getHue());
                moveCursor();
                viewNewColor.setBackgroundColor(getColor());
                updateAlphaView();
                return true;
            }
        });
        if (z) {
            viewAlphaCheckered.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                        return false;
                    }
                    float y = motionEvent.getY();
                    if (y < 0.0f) {
                        y = 0.0f;
                    }
                    if (y > ((float) viewAlphaCheckered.getMeasuredHeight())) {
                        y = ((float) viewAlphaCheckered.getMeasuredHeight()) - 0.001f;
                    }
                    int round = Math.round(255.0f - ((255.0f / ((float) viewAlphaCheckered.getMeasuredHeight())) * y));
                    setAlpha(round);
                    moveAlphaCursor();
                    viewNewColor.setBackgroundColor((round << 24) | (getColor() & ViewCompat.MEASURED_SIZE_MASK));
                    return true;
                }
            });
        }
        viewSatVal.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                    return false;
                }
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (x < 0.0f) {
                    x = 0.0f;
                }
                if (x > ((float) viewSatVal.getMeasuredWidth())) {
                    x = (float) viewSatVal.getMeasuredWidth();
                }
                if (y < 0.0f) {
                    y = 0.0f;
                }
                if (y > ((float) viewSatVal.getMeasuredHeight())) {
                    y = (float) viewSatVal.getMeasuredHeight();
                }
                setSat((1.0f / ((float) viewSatVal.getMeasuredWidth())) * x);
                setVal(1.0f - ((1.0f / ((float) viewSatVal.getMeasuredHeight())) * y));
                moveTarget();
                viewNewColor.setBackgroundColor(getColor());
                return true;
            }
        });
        dialog = new Dialog(context2);
        dialog.setContentView(inflate);
        ivtrans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.onOk(AmbilWarnaDialog.this, 0);
                }
            }
        });
        ivok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.onOk(AmbilWarnaDialog.this, getColor());
                }
            }
        });
        ivclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.onCancel(AmbilWarnaDialog.this);
                }
            }
        });
        ivcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.onCancel(AmbilWarnaDialog.this);
                }
            }
        });
        forUI();
        inflate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                moveCursor();
                if (supportsAlpha) {
                    moveAlphaCursor();
                }
                moveTarget();
                if (supportsAlpha) {
                    updateAlphaView();
                }
                inflate.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void forUI() {
        int i;
        if (!transparent.booleanValue()) {
            vline.setVisibility(View.GONE);
            reltrans.setVisibility(View.GONE);
            lmain.setBackgroundResource(R.drawable.dialog_color_small);
            i = 1056;
        } else {
            i = 1376;
        }
        SetLayparam.setHeightWidth(context, lmain, 969, i);
        SetLayparam.setHeight(context, reltitle, 150);
        SetLayparam.setHeightAsBoth(context, ivclose, 90);
        SetLayparam.setMarginRight(context, ivclose, 40);
        SetLayparam.setHeightWidth(context, ivarrow, 100, 49);
        SetLayparam.setMargins(context, ivarrow, 60, 0, 60, 0);
        SetLayparam.setHeightWidth(context, ivok, 316, 116);
        SetLayparam.setHeightWidth(context, ivcancel, 316, 116);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((width * 580) / 1080, (height * 400) / 1920);
        layoutParams.setMargins(0, 0, (width * 30) / 1080, 0);
        viewSatVal.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((width * 115) / 1080, (height * 400) / 1920);
        layoutParams2.setMargins((width * 30) / 1080, 0, 0, 0);
        layoutParams2.addRule(1, R.id.ambilwarna_viewSatBri);
        viewHue.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams((width * 190) / 1080, (height * 90) / 1920);
        fb1.setLayoutParams(layoutParams3);
        fb2.setLayoutParams(layoutParams3);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams((width * 820) / 1080, (height * 3) / 1920);
        layoutParams4.gravity = 1;
        layoutParams4.setMargins(0, 0, 0, (height * 60) / 1920);
        vline.setLayoutParams(layoutParams4);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams((width * 810) / 1080, (height * 140) / 1920);
        layoutParams5.gravity = 1;
        reltrans.setLayoutParams(layoutParams5);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams((width * 106) / 1080, (width * 106) / 1080);
        layoutParams6.setMargins(0, 0, (width * 20) / 1080, 0);
        layoutParams6.addRule(11);
        layoutParams6.addRule(15);
        ivtrans.setLayoutParams(layoutParams6);
    }

    public void moveCursor() {
        float measuredHeight = ((float) viewHue.getMeasuredHeight()) - ((getHue() * ((float) viewHue.getMeasuredHeight())) / 360.0f);
        if (measuredHeight == ((float) viewHue.getMeasuredHeight())) {
            measuredHeight = 0.0f;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewCursor.getLayoutParams();
        double left = (double) viewHue.getLeft();
        double floor = Math.floor((double) (viewCursor.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) viewHue.getTop()) + measuredHeight);
        double floor2 = Math.floor((double) (viewCursor.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        viewCursor.setLayoutParams(layoutParams);
    }

    public void moveTarget() {
        float sat = getSat() * ((float) viewSatVal.getMeasuredWidth());
        float val = (1.0f - getVal()) * ((float) viewSatVal.getMeasuredHeight());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewTarget.getLayoutParams();
        double left = (double) (((float) viewSatVal.getLeft()) + sat);
        double floor = Math.floor((double) (viewTarget.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) viewSatVal.getTop()) + val);
        double floor2 = Math.floor((double) (viewTarget.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        viewTarget.setLayoutParams(layoutParams);
    }

    public void moveAlphaCursor() {
        float measuredHeight = (float) viewAlphaCheckered.getMeasuredHeight();
        float alpha2 = measuredHeight - ((getAlpha() * measuredHeight) / 255.0f);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewAlphaCursor.getLayoutParams();
        double left = (double) viewAlphaCheckered.getLeft();
        double floor = Math.floor((double) (viewAlphaCursor.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) viewAlphaCheckered.getTop()) + alpha2);
        double floor2 = Math.floor((double) (viewAlphaCursor.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        viewAlphaCursor.setLayoutParams(layoutParams);
    }


    public int getColor() {
        return (Color.HSVToColor(currentColorHsv) & ViewCompat.MEASURED_SIZE_MASK) | (alpha << 24);
    }


    public float getHue() {
        return currentColorHsv[0];
    }


    public void setHue(float f) {
        currentColorHsv[0] = f;
    }

    private float getAlpha() {
        return (float) alpha;
    }


    public void setAlpha(int i) {
        alpha = i;
    }

    private float getSat() {
        return currentColorHsv[1];
    }

    public void setSat(float f) {
        currentColorHsv[1] = f;
    }

    private float getVal() {
        return currentColorHsv[2];
    }

    public void setVal(float f) {
        currentColorHsv[2] = f;
    }

    public void setTitle(String str) {
        dialog.setTitle(str);
    }

    public void show() {
        dialog.show();
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void updateAlphaView() {
        viewAlphaOverlay.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.HSVToColor(currentColorHsv), 0}));
    }
}
