package com.scorpion.NeonphotoEditor.Videoneoneffect.particle;

import android.content.Context;
import android.content.res.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int i) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return sb.toString();
                }
                sb.append(readLine);
                sb.append("\n");
            }
        } catch (IOException unused) {
            throw new RuntimeException("Could not open resource: " + i);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("Resource not found: " + i, e);
        }
    }
}
