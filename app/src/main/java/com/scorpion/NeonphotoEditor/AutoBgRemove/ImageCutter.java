package com.scorpion.NeonphotoEditor.AutoBgRemove;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import androidx.core.view.ViewCompat;

import com.scorpion.NeonphotoEditor.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class ImageCutter {
    public static final List<Integer> ALL_TYPES = Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
    private static final String INPUT_NAME = "ImageTensor";
    private static final String MODEL_FILE = "model.pb";
    private static final String OUTPUT_NAME = "SemanticPredictions";
    public static final List<Integer> PEOPLE_TYPES = Arrays.asList(new Integer[]{15});
    static ArrayList<PointF> allredpath;
    public static BrushMode brushMode = BrushMode.BLUR;
    static Context mContext;
    private static TensorFlowInferenceInterface sTFInterface = null;
    private int bitheight;
    private int bitwidth;
    private int brushRadius = 14;
    private ExecutorService executor = Executors.newFixedThreadPool(12);
    private boolean found = false;
    private Future future;
    private Bitmap lastInterferenceBitmap = null;
    private Bitmap lastMaskBitmap = null;
    private boolean small = false;
    public int value = 0;

    public enum BrushMode {
        ERASER,
        BLUR
    }

    public static synchronized boolean initialize(AssetManager assetManager, Context context) {
        synchronized (ImageCutter.class) {
            synchronized (ImageCutter.class) {
                synchronized (ImageCutter.class) {
                    mContext = context;
                    sTFInterface = new TensorFlowInferenceInterface(assetManager, MODEL_FILE);
                    allredpath = new ArrayList<>();
                }
            }
        }
        return true;
    }

    public static float convertDpToPixel(float f, Context context) {
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public Bitmap execute(Bitmap bitmap, Set<PointF> set) {
        Bitmap bitmap2;
        Bitmap bitmap3 = null;
        if (bitmap.equals(this.lastInterferenceBitmap)) {
            if (!(this.lastMaskBitmap == null || this.lastInterferenceBitmap == null)) {
                bitmap3 = this.lastMaskBitmap;
                bitmap = this.lastInterferenceBitmap;
            }
            updateMask(bitmap3, set);
            bitmap2 = bitmap3;
        } else {
            bitmap2 = runModel(bitmap);
            if (!(this.lastMaskBitmap == null || this.lastInterferenceBitmap == null)) {
                this.lastMaskBitmap.recycle();
                this.lastInterferenceBitmap.recycle();
                this.lastMaskBitmap = null;
                this.lastInterferenceBitmap = null;
            }
        }
        Bitmap scaleBitmap = ImageUtils.scaleBitmap(bitmap2, bitmap.getWidth(), bitmap.getHeight());
        this.lastInterferenceBitmap = bitmap;
        this.lastMaskBitmap = scaleBitmap;
        return scaleBitmap;
    }

    private void updateMask(Bitmap bitmap, Set<PointF> set) {
        generatePoints(this.brushRadius, set);
        filterPoints(set, bitmap);
        int i = brushMode == BrushMode.BLUR ? 0 : -1;
        if (brushMode != BrushMode.BLUR) {
            mContext.getResources().getColor(R.color.redmask);
        }
        if (set != null && set.size() > 0) {
            for (PointF next : set) {
                bitmap.setPixel((int) next.x, (int) next.y, i);
            }
        }
    }

    private void filterPoints(Set<PointF> set, Bitmap bitmap) {
        Iterator<PointF> it = set.iterator();
        while (it.hasNext()) {
            PointF next = it.next();
            if (next.x >= ((float) bitmap.getWidth()) || next.x <= 0.0f || next.y >= ((float) bitmap.getHeight()) || next.y <= 0.0f) {
                it.remove();
            }
        }
    }


    public synchronized boolean isInitialized() {
        return sTFInterface != null;
    }


    private Bitmap runModel(Bitmap bitmap) {
        float max = 513.0f / ((float) Math.max(bitmap.getWidth(), bitmap.getHeight()));
        return segment(ImageUtils.tfResizeBilinear(bitmap, Math.round(((float) bitmap.getWidth()) * max), Math.round(((float) bitmap.getHeight()) * max)));
    }

    public Bitmap segment(Bitmap bitmap) {
        if (sTFInterface == null || bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > 513 || height > 513) {
            return null;
        }
        int i = width * height;
        int[] iArr = new int[i];
        byte[] bArr = new byte[(i * 3)];
        int[] iArr2 = new int[i];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = iArr[i2];
            int i4 = i2 * 3;
            bArr[i4 + 0] = (byte) ((i3 >> 16) & 255);
            bArr[i4 + 1] = (byte) ((i3 >> 8) & 255);
            bArr[i4 + 2] = (byte) (i3 & 255);
        }
        System.currentTimeMillis();
        sTFInterface.feed(INPUT_NAME, bArr, 1, (long) height, (long) width, 3);
        sTFInterface.run(new String[]{OUTPUT_NAME}, true);
        sTFInterface.fetch(OUTPUT_NAME, iArr2);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (!createMask(width, height, iArr2, createBitmap, PEOPLE_TYPES)) {
            createMask(width, height, iArr2, createBitmap, ALL_TYPES);
        } else {
            this.found = true;
        }
        return createBitmap;
    }

    private boolean createMask(int i, int i2, int[] iArr, Bitmap bitmap, List<Integer> list) {
        int i3;
        boolean z;
        int i4;
        this.bitwidth = i;
        this.bitheight = i2;
        allredpath.clear();
        boolean z2 = false;
        int i5 = 0;
        boolean z3 = false;
        int i6 = 0;
        while (i5 < i2) {
            int i7 = i6;
            boolean z4 = z3;
            int i8 = 0;
            while (i8 < i) {
                if (list.contains(Integer.valueOf(iArr[(i5 * i) + i8]))) {
                    i4 = ViewCompat.MEASURED_STATE_MASK;
                    allredpath.add(new PointF((float) i8, (float) i5));
                    this.value++;
                    i3 = i7 + 1;
                    z = true;
                } else {
                    i3 = i7;
                    z = z4;
                    i4 = 0;
                }
                bitmap.setPixel(i8, i5, i4);
                i8++;
                z4 = z;
                i7 = i3;
            }
            i5++;
            z3 = z4;
            i6 = i7;
        }
        if (i6 < ((i * i2) * 21) / 100) {
            z2 = true;
        }
        this.small = z2;
        return z3;
    }

    public void generatePoints(int i, Set<PointF> set) {
        int size = set.size() / 12;
        HashSet hashSet = new HashSet();
        HashSet hashSet2 = new HashSet(13);
        for (PointF add : set) {
            hashSet.add(add);
            if (hashSet.size() > size) {
                hashSet2.add(this.executor.submit(new PointGenerator(new HashSet(hashSet), i)));
                hashSet.clear();
            }
        }
        if (hashSet.size() > 0) {
            hashSet2.add(this.executor.submit(new PointGenerator(new HashSet(hashSet), i)));
        }
        Iterator it = hashSet2.iterator();
        while (it.hasNext()) {
            this.future = (Future) it.next();
            try {
                set.addAll((Collection) this.future.get());
            } catch (Exception unused) {
            }
        }
    }

    public ArrayList<PointF> getAllredpath() {
        return allredpath;
    }

    public int getBitwidth() {
        return this.bitwidth;
    }

    public int getBitheight() {
        return this.bitheight;
    }

}
