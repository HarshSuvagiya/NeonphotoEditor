package com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker;

import android.content.Context;
import android.text.TextUtils;
import cn.ezandroid.ezfilter.core.util.NumberUtil;
import cn.ezandroid.ezfilter.core.util.Path;
import cn.ezandroid.ezfilter.extra.sticker.model.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class ComponentConvert {
    public static void convert(Context context, Component component, String str) {
        String[] strArr;
        component.src = str + component.src;
        if (Path.ASSETS.belongsTo(component.src)) {
            try {
                strArr = context.getAssets().list(Path.ASSETS.crop(component.src));
            } catch (IOException e) {
                e.printStackTrace();
                strArr = null;
            }
        } else if (Path.FILE.belongsTo(component.src)) {
            strArr = new File(Path.FILE.crop(component.src)).list();
        } else {
            strArr = new File(component.src).list();
        }
        ArrayList arrayList = new ArrayList();
        if (strArr != null) {
            for (String str2 : strArr) {
                if (!str2.toLowerCase().contains(".DS_Store".toLowerCase()) && !str2.toLowerCase().contains("Thumbs.db".toLowerCase())) {
                    arrayList.add(component.src + "/" + str2);
                }
            }
        }
        component.length = arrayList.size();
        Collections.sort(arrayList, modelclass.INSTANCE);
        component.resources = arrayList;
    }

    static /* synthetic */ int tempmethod(String str, String str2) {
        String[] split = str.split("_");
        String[] split2 = str2.split("_");
        String str3 = split[split.length - 1];
        if (str3.contains(".")) {
            str3 = str3.substring(0, str3.indexOf("."));
        }
        if (!TextUtils.isDigitsOnly(str3)) {
            str3 = str3.substring(str3.length() - 2);
        }
        String str4 = split2[split2.length - 1];
        if (str4.contains(".")) {
            str4 = str4.substring(0, str4.indexOf("."));
        }
        if (!TextUtils.isDigitsOnly(str4)) {
            str4 = str4.substring(str4.length() - 2);
        }
        return NumberUtil.parseInt(str3) - NumberUtil.parseInt(str4);
    }
}
