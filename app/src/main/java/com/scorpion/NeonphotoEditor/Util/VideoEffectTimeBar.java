package com.scorpion.NeonphotoEditor.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatImageView;

import com.scorpion.NeonphotoEditor.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Random;

public class VideoEffectTimeBar extends AppCompatImageView {
    ArrayList<String> alrange = new ArrayList<>();
    private Rect bitmapRect = new Rect();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    long cProgress;
    private boolean canSeek;
    String citem;
    long eEnd;
    long eStart;
    private boolean isEffect;
    private boolean isSeeking;
    private boolean isTouch;
    private boolean isVideoStatusDisplay;
    private long lProgress;
    private long maxValue = 100;
    private Paint paint = new Paint();
    public Runnable preBitmapRunnable = new Runnable() {
        public void run() {
            int bitmapCount = VideoEffectTimeBar.this.getBitmapCount();
            if (bitmapCount <= 0) {
                VideoEffectTimeBar.this.postDelayed(VideoEffectTimeBar.this.preBitmapRunnable, 100);
            } else {
                VideoEffectTimeBar.this.setPreBitmaps(bitmapCount);
            }
        }
    };
    private int preViewBitmapIndex = -1;
    private int progressBottom;
    private int progressHalfHeight = 3;
    private int progressTop;
    private SeekBarChangeListener scl;
    private int seekerColor = getResources().getColor(R.color.pink);
    float seekerWidth = 8.0f;
    private int selectorColor = getResources().getColor(R.color.colorAccentTrans);
    private Rect srcRect = new Rect();
    private int thumbCurrentVideoPositionX;
    String videoPath;

    public interface SeekBarChangeListener {
        void onSeeking(boolean z);

        void seekBarValueChanged(int i);
    }

    public VideoEffectTimeBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public VideoEffectTimeBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VideoEffectTimeBar(Context context) {
        super(context);
    }

    public static Bitmap getBitmapAtTimeMy(String str, int i, int i2, int i3) {
        if (str == null || str.length() == 0 || i2 < 0 || i3 < 0 || i < 0) {
            return null;
        }
        try {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(str);
            try {
                Bitmap frameAtTime = mediaMetadataRetriever.getFrameAtTime((long) (i * 1000), 2);
                if (frameAtTime != null) {
                    Bitmap createBitmap = Bitmap.createBitmap(i2, i3, frameAtTime.getConfig());
                    Canvas canvas = new Canvas(createBitmap);
                    float width = ((float) i2) / ((float) frameAtTime.getWidth());
                    float height = ((float) i3) / ((float) frameAtTime.getHeight());
                    if (width <= height) {
                        width = height;
                    }
                    int width2 = (int) (((float) frameAtTime.getWidth()) * width);
                    int height2 = (int) (((float) frameAtTime.getHeight()) * width);
                    canvas.drawBitmap(frameAtTime, new Rect(0, 0, frameAtTime.getWidth(), frameAtTime.getHeight()), new Rect((i2 - width2) / 2, (i3 - height2) / 2, width2, height2), (Paint) null);
                    frameAtTime.recycle();
                    return createBitmap;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            init();
        }
    }

    public void init() {
        this.progressTop = 0;
        this.progressBottom = getHeight();
        invalidate();
    }

    public void setSeekBarChangeListener(SeekBarChangeListener seekBarChangeListener) {
        this.scl = seekBarChangeListener;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bitmapCount = getBitmapCount();
        if (bitmapCount > 0) {
            getWidth();
            int height = getHeight();
            if (this.preViewBitmapIndex != -1) {
                int i = 0;
                while (i < bitmapCount) {
                    Bitmap correctBitmap = i <= this.preViewBitmapIndex ? getCorrectBitmap(i) : getCorrectBitmap(0);
                    if (correctBitmap != null && !correctBitmap.isRecycled()) {
                        this.bitmapRect.set(i * height, 0, (i + 1) * height, getHeight());
                        int width = correctBitmap.getWidth();
                        int height2 = correctBitmap.getHeight();
                        float f = (float) width;
                        float f2 = (float) height;
                        float f3 = 1.0f;
                        float f4 = (f * 1.0f) / f2;
                        float f5 = (float) height2;
                        float f6 = (f5 * 1.0f) / f2;
                        if (f6 < 1.0f || f4 <= 1.0f) {
                            if (f6 > f4) {
                                f4 = f6;
                            }
                            if (f4 != 0.0f) {
                                f3 = f4;
                            }
                            int i2 = (((int) (f / f3)) - height) / 2;
                            int i3 = (((int) (f5 / f3)) - height) / 2;
                            this.srcRect.set(i2, i3, i2 + height, i3 + height);
                        } else {
                            if (f6 <= f4) {
                                f4 = f6;
                            }
                            int i4 = (int) (f4 * f2);
                            int i5 = (width - i4) / 2;
                            int i6 = (height2 - i4) / 2;
                            this.srcRect.set(i5, i6, i5 + i4, i4 + i6);
                        }
                        canvas.drawBitmap(correctBitmap, this.srcRect, this.bitmapRect, (Paint) null);
                    }
                    i++;
                }
            }
        }
        this.paint.setColor(this.selectorColor);
        for (int i7 = 0; i7 < this.alrange.size(); i7++) {
            String str = this.alrange.get(i7);
            long startTime = getStartTime(str);
            long endTime = getEndTime(str);
            if (this.cProgress >= startTime && this.cProgress <= endTime) {
                this.eStart = startTime;
                this.eEnd = endTime;
            }
            canvas.drawRect(new Rect(calculateCorrds(startTime), this.progressTop, calculateCorrds(endTime), this.progressBottom), this.paint);
        }
        if (this.cProgress < this.eStart || this.cProgress > this.eEnd) {
            setEffect(false);
        } else {
            setEffect(true);
        }
        if (!this.isVideoStatusDisplay) {
            return;
        }
        if (this.isTouch) {
            canvas.drawRect(new Rect(calculateCorrds(this.lProgress), this.progressTop, this.thumbCurrentVideoPositionX, this.progressBottom), this.paint);
            return;
        }
        this.paint.setColor(this.seekerColor);
        this.paint.setStrokeWidth(this.seekerWidth);
        canvas.drawLine((float) this.thumbCurrentVideoPositionX, 0.0f, (float) this.thumbCurrentVideoPositionX, (float) getHeight(), this.paint);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.canSeek) {
            int x = (int) motionEvent.getX();
            switch (motionEvent.getAction()) {
                case 0:
                    setSeeking(true);
                    break;
                case 1:
                    setSeeking(false);
                    break;
                case 2:
                    this.thumbCurrentVideoPositionX = x;
                    break;
            }
            notifySeekBarValueChanged();
        }
        return true;
    }

    private void notifySeekBarValueChanged() {
        invalidate();
        if (this.scl != null) {
            this.cProgress = (long) calculateProgress((long) this.thumbCurrentVideoPositionX);
            this.scl.seekBarValueChanged((int) this.cProgress);
        }
    }

    public void setTouch(boolean z, long j) {
        this.isTouch = z;
        if (z) {
            this.lProgress = j;
            this.citem = this.lProgress + ":";
            return;
        }
        this.citem += j;
        this.alrange.add(this.citem);
        this.citem = "";
    }

    public long getStartTime(String str) {
        if (str.contains(":")) {
            return Long.parseLong(str.substring(0, str.indexOf(":")));
        }
        return 0;
    }

    public long getEndTime(String str) {
        if (str.contains(":")) {
            return Long.parseLong(str.substring(str.indexOf(":") + 1));
        }
        return 0;
    }

    public int getBitmapCount() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return 0;
        }
        return width % height == 0 ? width / height : (width / height) + 1;
    }

    private Bitmap getCorrectBitmap(int i) {
        ArrayList<Bitmap> arrayList = this.bitmaps;
        if (arrayList == null || i >= arrayList.size()) {
            return null;
        }
        return this.bitmaps.get(i);
    }

    public void beginPreView(String str) {
        this.videoPath = str;
        this.bitmaps.clear();
        this.preViewBitmapIndex = -1;
        post(this.preBitmapRunnable);
    }

    public void setPreBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            this.bitmaps.add(bitmap);
            this.preViewBitmapIndex++;
            invalidate();
        }
    }

    public void setPreBitmaps(int i) {
        Size bitmapSize = getBitmapSize();
        if (bitmapSize.getHeight() > 0 && bitmapSize.getWidth() > 0) {
            final int i2 = (int) (this.maxValue / ((long) i));
            final int width = bitmapSize.getWidth();
            final int height = bitmapSize.getHeight();
            final int i3 = i;
            Observable.create(new ObservableOnSubscribe<Bitmap>() {
                public void subscribe(ObservableEmitter<Bitmap> observableEmitter) throws Exception {
                    for (int i = 0; i < i3; i++) {
                        Bitmap bitmapAtTimeMy = VideoEffectTimeBar.getBitmapAtTimeMy(VideoEffectTimeBar.this.videoPath, i2 * i, width, height);
                        if (bitmapAtTimeMy != null) {
                            observableEmitter.onNext(bitmapAtTimeMy);
                        }
                    }
                    observableEmitter.onComplete();
                }
            }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Bitmap>() {
                public void accept(Bitmap bitmap) throws Exception {
                    VideoEffectTimeBar.this.setPreBitmap(bitmap);
                }
            });
        }
    }

    private Size getBitmapSize() {
        return new Size((getWidth() * 2) / 3, (getWidth() * 2) / 3);
    }

    private int calculateCorrds(long j) {
        double width = (double) getWidth();
        Double.isNaN(width);
        double d = (double) this.maxValue;
        Double.isNaN(d);
        double d2 = (double) j;
        Double.isNaN(d2);
        return (int) (((width - 2.0d) / d) * d2);
    }

    private int calculateProgress(long j) {
        double d = (double) (j * this.maxValue);
        double width = (double) getWidth();
        Double.isNaN(width);
        Double.isNaN(d);
        return (int) (d / (width - 2.0d));
    }

    public void videoPlayingProgress(int i) {
        this.isVideoStatusDisplay = true;
        long j = (long) i;
        this.cProgress = j;
        this.thumbCurrentVideoPositionX = calculateCorrds(j);
        invalidate();
    }

    public void removeVideoStatusThumb() {
        this.isVideoStatusDisplay = false;
        invalidate();
    }

    public void setMaxValue(long j) {
        this.maxValue = j;
    }

    public void setProgressHeight(int i) {
        this.progressHalfHeight /= 2;
        invalidate();
    }

    public void setSelectorColor(int i) {
        this.selectorColor = i;
        invalidate();
    }

    public void setSeekerColor(int i) {
        this.seekerColor = i;
        invalidate();
    }

    public void setSeekerWidth(float f) {
        this.seekerWidth = f;
        invalidate();
    }

    public int getRandomColor() {
        Random random = new Random();
        return Color.argb(110, random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    public boolean isEffect() {
        return this.isEffect;
    }

    public void setEffect(boolean z) {
        this.isEffect = z;
    }

    public void removeEffectRange(int i) {
        if (this.alrange.size() > i) {
            this.alrange.remove(i);
        }
    }

    public boolean canSeek() {
        return this.canSeek;
    }

    public void setSeeking(boolean z) {
        this.isSeeking = z;
        if (this.scl != null) {
            this.scl.onSeeking(this.isSeeking);
        }
    }

    public void setCanSeek(boolean z) {
        this.canSeek = z;
    }

    public class Size {
        int height;
        int width;

        public Size(int i, int i2) {
            this.width = i;
            this.height = i2;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
