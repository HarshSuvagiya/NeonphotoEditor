package com.scorpion.NeonphotoEditor.Util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.scorpion.NeonphotoEditor.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Helper {
    public static void show(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
    }


    public static void showLog(String str) {
        PrintStream printStream = System.out;
        printStream.println("AAA : " + str);
    }

    public static void showLog(String str, String str2) {
        PrintStream printStream = System.out;
        printStream.println(str + " : " + str2);
    }


    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }


    public static void shareImage(Context context, String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + str));
        context.startActivity(Intent.createChooser(intent, "Share Image"));
    }



    public static Bitmap getBitmapResize(Context context, Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width >= height) {
            int i3 = (height * i) / width;
            if (i3 > i2) {
                i = (i * i2) / i3;
            } else {
                i2 = i3;
            }
        } else {
            int i4 = (width * i2) / height;
            if (i4 > i) {
                i2 = (i2 * i) / i4;
            } else {
                i = i4;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static String getOutputFolder(Context context) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getTempFolder(Context context) {
        File file = new File(getOutputFolder(context), ".temp");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static void deleteFolder(File file) {
        try {
            if (file.isDirectory()) {
                String[] list = file.list();
                for (String file2 : list) {
                    File file3 = new File(file, file2);
                    if (file3.isDirectory()) {
                        Log.d("DeleteRecursive", "Recursive Call" + file3.getPath());
                        deleteFolder(file3);
                    } else {
                        Log.d("DeleteRecursive", "Delete File" + file3.getPath());
                        if (!file3.delete()) {
                            Log.d("DeleteRecursive", "DELETE FAIL");
                        }
                    }
                }
            }
            file.delete();
        } catch (Exception unused) {
        }
    }

    public static void copyFile(String str, String str2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(str);
        FileOutputStream fileOutputStream = new FileOutputStream(str2);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = fileInputStream.read(bArr);
            if (read != -1) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    public static ArrayList<Vdata> getAllVideo(Context context) {
        ArrayList<Vdata> arrayList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "_data", "title", "artist", "album", "datetaken", "date_added", "duration", "tags", "description", "category", "mime_type", "resolution"}, (String) null, (String[]) null, "datetaken DESC");
        try {
            query.moveToFirst();
            do {
                Vdata Vdata = new Vdata();
                Vdata.setID((long) query.getInt(query.getColumnIndexOrThrow("_id")));
                Vdata.setPath(query.getString(query.getColumnIndexOrThrow("_data")));
                Vdata.setTitle(query.getString(query.getColumnIndexOrThrow("title")));
                Vdata.setArtist(query.getString(query.getColumnIndexOrThrow("artist")));
                Vdata.setAlbum(query.getString(query.getColumnIndexOrThrow("album")));
                Vdata.setDate_taken(query.getString(query.getColumnIndexOrThrow("datetaken")));
                Vdata.setDate_added(query.getString(query.getColumnIndexOrThrow("date_added")));
                Vdata.setDuration(query.getString(query.getColumnIndexOrThrow("duration")));
                Vdata.setTags(query.getString(query.getColumnIndexOrThrow("tags")));
                Vdata.setDescription(query.getString(query.getColumnIndexOrThrow("description")));
                Vdata.setResolution(query.getString(query.getColumnIndexOrThrow("resolution")));
                Vdata.setMime(query.getString(query.getColumnIndexOrThrow("mime_type")));
                Vdata.setCategory(query.getString(query.getColumnIndexOrThrow("category")));
                File file = new File(Vdata.getPath());
                if (!file.exists()) {
                    showLog("NNN", "Not Found : " + Vdata.getPath());
                } else if (file.length() <= 0 || TextUtils.isEmpty(Vdata.getDuration())) {
                    showLog("NNN", "Size zero : " + Vdata.getPath());
                } else if (Long.parseLong(Vdata.getDuration()) != 0) {
                    arrayList.add(Vdata);
                } else {
                    showLog("NNN", "Duration zero : " + Vdata.getPath());
                }
            } while (query.moveToNext());
            query.close();
            return arrayList;
        } catch (Exception e) {
            showLog("EEE", e.toString());
            return arrayList;
        }
    }



    public static String milliSecondsToTimer(long j) {
        String str;
        String str2 = "";
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i > 0) {
            str2 = i + ":";
        }
        if (i3 < 10) {
            str = "0" + i3;
        } else {
            str = "" + i3;
        }
        return str2 + i2 + ":" + str;
    }

    public static String getFormatTime(long j) {
        String str;
        String str2;
        String str3 = "";
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = ((int) j2) / 60000;
        int i3 = (int) ((j2 % 60000) / 1000);
        if (i > 0) {
            str3 = i + " hour";
        }
        if (i3 < 10) {
            str = "0" + i3;
        } else {
            str = "" + i3;
        }
        if (i2 < 10) {
            str2 = "0" + i2 + " min";
        } else {
            str2 = "" + i2 + " min";
        }
        String str4 = str3 + str2 + str + " sec";
        if (i2 != 0 || i != 0) {
            return str4;
        }
        return str + " sec";
    }

    public static String getDate(long j, String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str);
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return simpleDateFormat.format(instance.getTime());
    }


    public static String saveCropBitmap(Context context, Bitmap bitmap) {
        File file = new File(getTempFolder(context), "crop.jpg");
        String absolutePath = file.getAbsolutePath();
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            return absolutePath;
        } catch (Exception unused) {
            return "";
        }
    }

    public static String saveMainBitmap(Context context, Bitmap bitmap) {
        String outputFolder = getOutputFolder(context);
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.getDefault());
        File file = new File(outputFolder, context.getResources().getString(R.string.app_name) + "_" + simpleDateFormat.format(Long.valueOf(currentTimeMillis)) + ".jpg");
        String absolutePath = file.getAbsolutePath();
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            return absolutePath;
        } catch (Exception unused) {
            return "";
        }
    }

    public static Bitmap getMask(Bitmap bitmap, Bitmap bitmap2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        Bitmap createScaledBitmap2 = Bitmap.createScaledBitmap(bitmap2, width, height, true);
        Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap2.getWidth(), createScaledBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(createScaledBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(createScaledBitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode((Xfermode) null);
        return createBitmap;
    }

    public static Bitmap getTextBitmap(String str, Typeface typeface, int i) {
        TextPaint textPaint = new TextPaint(65);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(typeface);
        textPaint.setColor(i);
        textPaint.setTextSize(80.0f);
        int measureText = (int) textPaint.measureText(str);
        StaticLayout staticLayout = new StaticLayout(str, textPaint, measureText, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        Bitmap createBitmap = Bitmap.createBitmap(measureText, staticLayout.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(65);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0);
        canvas.drawPaint(paint);
        canvas.save();
        canvas.translate(0.0f, 0.0f);
        staticLayout.draw(canvas);
        canvas.restore();
        return createBitmap;
    }
}
