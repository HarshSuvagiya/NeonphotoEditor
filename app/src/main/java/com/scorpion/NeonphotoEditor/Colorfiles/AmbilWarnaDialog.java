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
        this.currentColorHsv = new float[3];
        this.supportsAlpha = z;
        this.listener = onAmbilWarnaListener;
        this.transparent = bool;
        this.context = context2;
        i = !z ? i | ViewCompat.MEASURED_STATE_MASK : i;
        Color.colorToHSV(i, this.currentColorHsv);
        this.alpha = Color.alpha(i);
        final View inflate = LayoutInflater.from(context2).inflate(R.layout.color_dialog, (ViewGroup) null);
        this.viewHue = inflate.findViewById(R.id.ambilwarna_viewHue);
        this.viewSatVal = (AmbilWarnaSquare) inflate.findViewById(R.id.ambilwarna_viewSatBri);
        this.viewCursor = (ImageView) inflate.findViewById(R.id.ambilwarna_cursor);
        this.viewOldColor = inflate.findViewById(R.id.ambilwarna_oldColor);
        this.viewNewColor = inflate.findViewById(R.id.ambilwarna_newColor);
        this.viewTarget = (ImageView) inflate.findViewById(R.id.ambilwarna_target);
        this.viewContainer = (ViewGroup) inflate.findViewById(R.id.ambilwarna_viewContainer);
        this.viewAlphaOverlay = inflate.findViewById(R.id.ambilwarna_overlay);
        this.viewAlphaCursor = (ImageView) inflate.findViewById(R.id.ambilwarna_alphaCursor);
        this.viewAlphaCheckered = (ImageView) inflate.findViewById(R.id.ambilwarna_alphaCheckered);
        this.ivok = (ImageView) inflate.findViewById(R.id.ivok);
        this.ivcancel = (ImageView) inflate.findViewById(R.id.ivcancel);
        this.ivclose = (ImageView) inflate.findViewById(R.id.ivclose);
        this.ivarrow = (ImageView) inflate.findViewById(R.id.ivcarrow);
        this.lmain = (LinearLayout) inflate.findViewById(R.id.lineardialog);
        this.reltitle = (RelativeLayout) inflate.findViewById(R.id.reltitle);
        this.tvtitle = (TextView) inflate.findViewById(R.id.tvtitle);
        this.fb1 = (FrameLayout) inflate.findViewById(R.id.framebox1);
        this.fb2 = (FrameLayout) inflate.findViewById(R.id.framebox2);
        this.ambilState = (LinearLayout) inflate.findViewById(R.id.ambilwarna_state);
        this.vline = inflate.findViewById(R.id.viewline);
        this.linearbtn = (LinearLayout) inflate.findViewById(R.id.linearbtn);
        this.reltrans = (RelativeLayout) inflate.findViewById(R.id.reltrans);
        this.ivtrans = (ImageView) inflate.findViewById(R.id.ivtrans);
        this.width = context2.getResources().getDisplayMetrics().widthPixels;
        this.height = context2.getResources().getDisplayMetrics().heightPixels;
        this.viewAlphaOverlay.setVisibility(z ? View.VISIBLE : View.GONE);
        this.viewAlphaCursor.setVisibility(z ? View.VISIBLE : View.GONE);
        this.viewAlphaCheckered.setVisibility(z ? View.VISIBLE : View.GONE);
        this.viewSatVal.setHue(getHue());
        this.viewOldColor.setBackgroundColor(i);
        this.viewNewColor.setBackgroundColor(i);
        this.viewHue.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                    return false;
                }
                float y = motionEvent.getY();
                if (y < 0.0f) {
                    y = 0.0f;
                }
                if (y > ((float) AmbilWarnaDialog.this.viewHue.getMeasuredHeight())) {
                    y = ((float) AmbilWarnaDialog.this.viewHue.getMeasuredHeight()) - 0.001f;
                }
                float measuredHeight = 360.0f - ((360.0f / ((float) AmbilWarnaDialog.this.viewHue.getMeasuredHeight())) * y);
                if (measuredHeight == 360.0f) {
                    measuredHeight = 0.0f;
                }
                AmbilWarnaDialog.this.setHue(measuredHeight);
                AmbilWarnaDialog.this.viewSatVal.setHue(AmbilWarnaDialog.this.getHue());
                AmbilWarnaDialog.this.moveCursor();
                AmbilWarnaDialog.this.viewNewColor.setBackgroundColor(AmbilWarnaDialog.this.getColor());
                AmbilWarnaDialog.this.updateAlphaView();
                return true;
            }
        });
        if (z) {
            this.viewAlphaCheckered.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                        return false;
                    }
                    float y = motionEvent.getY();
                    if (y < 0.0f) {
                        y = 0.0f;
                    }
                    if (y > ((float) AmbilWarnaDialog.this.viewAlphaCheckered.getMeasuredHeight())) {
                        y = ((float) AmbilWarnaDialog.this.viewAlphaCheckered.getMeasuredHeight()) - 0.001f;
                    }
                    int round = Math.round(255.0f - ((255.0f / ((float) AmbilWarnaDialog.this.viewAlphaCheckered.getMeasuredHeight())) * y));
                    AmbilWarnaDialog.this.setAlpha(round);
                    AmbilWarnaDialog.this.moveAlphaCursor();
                    AmbilWarnaDialog.this.viewNewColor.setBackgroundColor((round << 24) | (AmbilWarnaDialog.this.getColor() & ViewCompat.MEASURED_SIZE_MASK));
                    return true;
                }
            });
        }
        this.viewSatVal.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 2 && motionEvent.getAction() != 0 && motionEvent.getAction() != 1) {
                    return false;
                }
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if (x < 0.0f) {
                    x = 0.0f;
                }
                if (x > ((float) AmbilWarnaDialog.this.viewSatVal.getMeasuredWidth())) {
                    x = (float) AmbilWarnaDialog.this.viewSatVal.getMeasuredWidth();
                }
                if (y < 0.0f) {
                    y = 0.0f;
                }
                if (y > ((float) AmbilWarnaDialog.this.viewSatVal.getMeasuredHeight())) {
                    y = (float) AmbilWarnaDialog.this.viewSatVal.getMeasuredHeight();
                }
                AmbilWarnaDialog.this.setSat((1.0f / ((float) AmbilWarnaDialog.this.viewSatVal.getMeasuredWidth())) * x);
                AmbilWarnaDialog.this.setVal(1.0f - ((1.0f / ((float) AmbilWarnaDialog.this.viewSatVal.getMeasuredHeight())) * y));
                AmbilWarnaDialog.this.moveTarget();
                AmbilWarnaDialog.this.viewNewColor.setBackgroundColor(AmbilWarnaDialog.this.getColor());
                return true;
            }
        });
        this.dialog = new Dialog(context2);
        this.dialog.setContentView(inflate);
        this.ivtrans.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AmbilWarnaDialog.this.listener != null) {
                    AmbilWarnaDialog.this.listener.onOk(AmbilWarnaDialog.this, 0);
                }
            }
        });
        this.ivok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AmbilWarnaDialog.this.listener != null) {
                    AmbilWarnaDialog.this.listener.onOk(AmbilWarnaDialog.this, AmbilWarnaDialog.this.getColor());
                }
            }
        });
        this.ivclose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AmbilWarnaDialog.this.listener != null) {
                    AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
                }
            }
        });
        this.ivcancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AmbilWarnaDialog.this.listener != null) {
                    AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
                }
            }
        });
        forUI();
        inflate.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                AmbilWarnaDialog.this.moveCursor();
                if (AmbilWarnaDialog.this.supportsAlpha) {
                    AmbilWarnaDialog.this.moveAlphaCursor();
                }
                AmbilWarnaDialog.this.moveTarget();
                if (AmbilWarnaDialog.this.supportsAlpha) {
                    AmbilWarnaDialog.this.updateAlphaView();
                }
                inflate.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void forUI() {
        int i;
        if (!this.transparent.booleanValue()) {
            this.vline.setVisibility(View.GONE);
            this.reltrans.setVisibility(View.GONE);
            this.lmain.setBackgroundResource(R.drawable.dialog_color_small);
            i = 1056;
        } else {
            i = 1376;
        }
        SetLayparam.setHeightWidth(this.context, this.lmain, 969, i);
        SetLayparam.setHeight(this.context, this.reltitle, 150);
        SetLayparam.setHeightAsBoth(this.context, this.ivclose, 90);
        SetLayparam.setMarginRight(this.context, this.ivclose, 40);
        SetLayparam.setHeightWidth(this.context, this.ivarrow, 100, 49);
        SetLayparam.setMargins(this.context, this.ivarrow, 60, 0, 60, 0);
        SetLayparam.setHeightWidth(this.context, this.ivok, 316, 116);
        SetLayparam.setHeightWidth(this.context, this.ivcancel, 316, 116);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((this.width * 580) / 1080, (this.height * 400) / 1920);
        layoutParams.setMargins(0, 0, (this.width * 30) / 1080, 0);
        this.viewSatVal.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((this.width * 115) / 1080, (this.height * 400) / 1920);
        layoutParams2.setMargins((this.width * 30) / 1080, 0, 0, 0);
        layoutParams2.addRule(1, R.id.ambilwarna_viewSatBri);
        this.viewHue.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams((this.width * 190) / 1080, (this.height * 90) / 1920);
        this.fb1.setLayoutParams(layoutParams3);
        this.fb2.setLayoutParams(layoutParams3);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams((this.width * 820) / 1080, (this.height * 3) / 1920);
        layoutParams4.gravity = 1;
        layoutParams4.setMargins(0, 0, 0, (this.height * 60) / 1920);
        this.vline.setLayoutParams(layoutParams4);
        LinearLayout.LayoutParams layoutParams5 = new LinearLayout.LayoutParams((this.width * 810) / 1080, (this.height * 140) / 1920);
        layoutParams5.gravity = 1;
        this.reltrans.setLayoutParams(layoutParams5);
        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams((this.width * 106) / 1080, (this.width * 106) / 1080);
        layoutParams6.setMargins(0, 0, (this.width * 20) / 1080, 0);
        layoutParams6.addRule(11);
        layoutParams6.addRule(15);
        this.ivtrans.setLayoutParams(layoutParams6);
    }

    public void moveCursor() {
        float measuredHeight = ((float) this.viewHue.getMeasuredHeight()) - ((getHue() * ((float) this.viewHue.getMeasuredHeight())) / 360.0f);
        if (measuredHeight == ((float) this.viewHue.getMeasuredHeight())) {
            measuredHeight = 0.0f;
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewCursor.getLayoutParams();
        double left = (double) this.viewHue.getLeft();
        double floor = Math.floor((double) (this.viewCursor.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) this.viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) this.viewHue.getTop()) + measuredHeight);
        double floor2 = Math.floor((double) (this.viewCursor.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) this.viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        this.viewCursor.setLayoutParams(layoutParams);
    }

    public void moveTarget() {
        float sat = getSat() * ((float) this.viewSatVal.getMeasuredWidth());
        float val = (1.0f - getVal()) * ((float) this.viewSatVal.getMeasuredHeight());
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewTarget.getLayoutParams();
        double left = (double) (((float) this.viewSatVal.getLeft()) + sat);
        double floor = Math.floor((double) (this.viewTarget.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) this.viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) this.viewSatVal.getTop()) + val);
        double floor2 = Math.floor((double) (this.viewTarget.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) this.viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        this.viewTarget.setLayoutParams(layoutParams);
    }

    public void moveAlphaCursor() {
        float measuredHeight = (float) this.viewAlphaCheckered.getMeasuredHeight();
        float alpha2 = measuredHeight - ((getAlpha() * measuredHeight) / 255.0f);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.viewAlphaCursor.getLayoutParams();
        double left = (double) this.viewAlphaCheckered.getLeft();
        double floor = Math.floor((double) (this.viewAlphaCursor.getMeasuredWidth() / 2));
        Double.isNaN(left);
        double d = left - floor;
        double paddingLeft = (double) this.viewContainer.getPaddingLeft();
        Double.isNaN(paddingLeft);
        layoutParams.leftMargin = (int) (d - paddingLeft);
        double top = (double) (((float) this.viewAlphaCheckered.getTop()) + alpha2);
        double floor2 = Math.floor((double) (this.viewAlphaCursor.getMeasuredHeight() / 2));
        Double.isNaN(top);
        double d2 = top - floor2;
        double paddingTop = (double) this.viewContainer.getPaddingTop();
        Double.isNaN(paddingTop);
        layoutParams.topMargin = (int) (d2 - paddingTop);
        this.viewAlphaCursor.setLayoutParams(layoutParams);
    }


    public int getColor() {
        return (Color.HSVToColor(this.currentColorHsv) & ViewCompat.MEASURED_SIZE_MASK) | (this.alpha << 24);
    }


    public float getHue() {
        return this.currentColorHsv[0];
    }


    public void setHue(float f) {
        this.currentColorHsv[0] = f;
    }

    private float getAlpha() {
        return (float) this.alpha;
    }


    public void setAlpha(int i) {
        this.alpha = i;
    }

    private float getSat() {
        return this.currentColorHsv[1];
    }

    public void setSat(float f) {
        this.currentColorHsv[1] = f;
    }

    private float getVal() {
        return this.currentColorHsv[2];
    }

    public void setVal(float f) {
        this.currentColorHsv[2] = f;
    }

    public void setTitle(String str) {
        this.dialog.setTitle(str);
    }

    public void show() {
        this.dialog.show();
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    public void updateAlphaView() {
        this.viewAlphaOverlay.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.HSVToColor(this.currentColorHsv), 0}));
    }
}
