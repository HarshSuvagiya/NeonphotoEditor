package com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker;

import java.util.Comparator;

public final  class modelclass implements Comparator {
    public static final  modelclass INSTANCE = new modelclass();

    private  modelclass() {
    }

    public final int compare(Object obj, Object obj2) {
        return ComponentConvert.tempmethod((String) obj, (String) obj2);
    }
}
