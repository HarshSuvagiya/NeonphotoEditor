package com.scorpion.NeonphotoEditor.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

public class Layparam {
    public static int SCALE_HEIGHT = 1920;
    public static int SCALE_WIDTH = 1080;

    public static void setHeightWidth(Context context, View view, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i3 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i4 = (displayMetrics.heightPixels * i2) / SCALE_HEIGHT;
        view.getLayoutParams().width = i3;
        view.getLayoutParams().height = i4;
    }


    public static void setWidthAsBoth(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH;
        view.getLayoutParams().width = i2;
        view.getLayoutParams().height = i2;
    }

    public static void setWidthAsHeight(Context context, View view, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i3 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i4 = (displayMetrics.widthPixels * i2) / SCALE_WIDTH;
        view.getLayoutParams().width = i3;
        view.getLayoutParams().height = i4;
    }

    public static void setHeight(Context context, View view, int i) {
        view.getLayoutParams().height = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
    }

    public static void setHeightAsBoth(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
        view.getLayoutParams().width = i2;
        view.getLayoutParams().height = i2;
    }

    public static void setHeightAsWidth(Context context, View view, int i, int i2) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i3 = (displayMetrics.heightPixels * i) / SCALE_HEIGHT;
        int i4 = (displayMetrics.heightPixels * i2) / SCALE_HEIGHT;
        view.getLayoutParams().width = i3;
        view.getLayoutParams().height = i4;
    }

    public static void setPadding(Context context, View view, int i, int i2, int i3, int i4) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        view.setPadding((displayMetrics.widthPixels * i) / SCALE_WIDTH, (displayMetrics.heightPixels * i2) / SCALE_HEIGHT, (displayMetrics.widthPixels * i3) / SCALE_WIDTH, (displayMetrics.heightPixels * i4) / SCALE_HEIGHT);
    }

    public static void setPadding(Context context, View view, int i) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i2 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i3 = (displayMetrics.heightPixels * i) / SCALE_HEIGHT;
        view.setPadding(i2, i3, i2, i3);
    }


    public static void setPaddingTop(Context context, View view, int i) {
        view.setPadding(0, (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT, 0, 0);
    }



    public static void setMargins(Context context, View view, int i, int i2, int i3, int i4) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i5 = (displayMetrics.widthPixels * i) / SCALE_WIDTH;
        int i6 = (displayMetrics.heightPixels * i2) / SCALE_HEIGHT;
        int i7 = (displayMetrics.widthPixels * i3) / SCALE_WIDTH;
        int i8 = (displayMetrics.heightPixels * i4) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i5, i6, i7, i8);
            view.requestLayout();
        }
    }

    public static void setMarginsAsHeight(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i2, i2, i2, i2);
            view.requestLayout();
        }
    }

    public static void setMarginLeft(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(i2, 0, 0, 0);
            view.requestLayout();
        }
    }

    public static void setMarginTop(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, i2, 0, 0);
            view.requestLayout();
        }
    }

    public static void setMarginRight(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().widthPixels * i) / SCALE_WIDTH;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, 0, i2, 0);
            view.requestLayout();
        }
    }

    public static void setMarginBottom(Context context, View view, int i) {
        int i2 = (context.getResources().getDisplayMetrics().heightPixels * i) / SCALE_HEIGHT;
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).setMargins(0, 0, 0, i2);
            view.requestLayout();
        }
    }
}
