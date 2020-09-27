package com.scorpion.NeonphotoEditor.Util;

import java.util.Locale;

public enum Path {
    FILE("file://"),
    ASSETS("file:///android_asset/"),
    SPIRAL("file:///android_asset/Spiral/"),
    TEXTURES("file:///android_asset/Textures/"),
    DRAWABLE("drawable://"),
    UNKNOWN("");
    
    private String mScheme;

    private Path(String str) {
        this.mScheme = str;
    }

    public static Path ofUri(String str) {
        if (str != null) {
            for (Path fX_Path : values()) {
                if (fX_Path.belongsTo(str)) {
                    return fX_Path;
                }
            }
        }
        return UNKNOWN;
    }

    public boolean belongsTo(String str) {
        return str.toLowerCase(Locale.US).startsWith(this.mScheme);
    }

    public String wrap(String str) {
        return this.mScheme + str;
    }

    public String back(int i) {
        return this.mScheme + i + "/back.png";
    }

    public String front(int i) {
        return this.mScheme + i + "/front.png";
    }

    public String thumb(int i) {
        return this.mScheme + i + "/image.png";
    }

    public String texture(int i) {
        return this.mScheme + i + ".png";
    }

    public String crop(String str) {
        if (belongsTo(str)) {
            return str.substring(this.mScheme.length());
        }
        throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected path [%2$s]", new Object[]{str, this.mScheme}));
    }
}
