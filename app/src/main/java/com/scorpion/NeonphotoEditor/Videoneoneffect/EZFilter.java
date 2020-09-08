package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Size;
import cn.ezandroid.ezfilter.camera.Camera2Builder;
import cn.ezandroid.ezfilter.camera.CameraBuilder;
import cn.ezandroid.ezfilter.core.cache.IBitmapCache;
import cn.ezandroid.ezfilter.core.cache.LruBitmapCache;
import cn.ezandroid.ezfilter.extra.IAdjustable;
import cn.ezandroid.ezfilter.image.BitmapBuilder;
import cn.ezandroid.ezfilter.view.ViewBuilder;
import cn.ezandroid.ezfilter.view.glview.IGLView;
import java.util.ArrayList;
import java.util.List;

public class EZFilter {
    /* access modifiers changed from: private */
    public static IBitmapCache sBitmapCache = new LruBitmapCache((int) (Runtime.getRuntime().maxMemory() / 4));

    private EZFilter() {
    }

    public static void setBitmapCache(IBitmapCache iBitmapCache) {
        sBitmapCache = iBitmapCache;
    }

    public static IBitmapCache getBitmapCache() {
        return sBitmapCache;
    }

    public static BitmapBuilder input(Bitmap bitmap) {
        return new BitmapBuilder(bitmap);
    }

    public static VideoBuilder input(Uri uri) {
        return new VideoBuilder(uri);
    }

    public static CameraBuilder input(Camera camera, Camera.Size size) {
        return new CameraBuilder(camera, size);
    }

    public static Camera2Builder input(CameraDevice cameraDevice, Size size) {
        return new Camera2Builder(cameraDevice, size);
    }

    public static ViewBuilder input(IGLView iGLView) {
        return new ViewBuilder(iGLView);
    }

    public static MultiBuilder input(List<Builder> list, MultiInput fX_MultiInput) {
        return new MultiBuilder(list, fX_MultiInput);
    }

    public static SplitBuilder input(Builder builder, SplitInput fX_SplitInput) {
        return new SplitBuilder(builder, fX_SplitInput);
    }

    public static abstract class Builder {
        protected boolean mEnableRecordAudio;
        protected boolean mEnableRecordVideo;
        protected List<FilterRender> mFilterRenders = new ArrayList();
        protected Handler mMainHandler = new Handler(Looper.getMainLooper());
        protected String mOutputPath;

        public abstract float getAspectRatio(IFitView fX_IFitView);

        public abstract FBORender getStartPointRender(IFitView fX_IFitView);

        protected Builder() {
        }

        /* access modifiers changed from: protected */
        public Builder addFilter(FilterRender fX_FilterRender) {
            if (fX_FilterRender != null && !this.mFilterRenders.contains(fX_FilterRender)) {
                fX_FilterRender.setBitmapCache(EZFilter.sBitmapCache);
                this.mFilterRenders.add(fX_FilterRender);
            }
            return this;
        }

        /* access modifiers changed from: protected */
        public <T extends FilterRender & IAdjustable> Builder addFilter(T t, float f) {
            if (t != null && !this.mFilterRenders.contains(t)) {
                t.setBitmapCache(EZFilter.sBitmapCache);
                ((IAdjustable) t).adjust(f);
                this.mFilterRenders.add(t);
            }
            return this;
        }

        /* access modifiers changed from: protected */
        public Builder enableRecord(String str, boolean z, boolean z2) {
            this.mOutputPath = str;
            this.mEnableRecordVideo = z;
            this.mEnableRecordAudio = z2;
            return this;
        }

        public RenderPipeline into(IFitView fX_IFitView, boolean z) {
            RenderPipeline renderPipeline = fX_IFitView.getRenderPipeline();
            if (renderPipeline != null && z) {
                renderPipeline.clean();
            }
            fX_IFitView.initRenderPipeline(getStartPointRender(fX_IFitView));
            RenderPipeline renderPipeline2 = fX_IFitView.getRenderPipeline();
            boolean aspectRatio = fX_IFitView.setAspectRatio(getAspectRatio(fX_IFitView), 0, 0);
            fX_IFitView.requestRender();
            if (renderPipeline2 != null) {
                renderPipeline2.clearEndPointRenders();
                renderPipeline2.addEndPointRender(new GLRender());
                if (this.mEnableRecordVideo || this.mEnableRecordAudio) {
                    renderPipeline2.addEndPointRender(new RecordableRender(this.mOutputPath, this.mEnableRecordVideo, this.mEnableRecordAudio));
                }
                for (FilterRender addFilterRender : this.mFilterRenders) {
                    renderPipeline2.addFilterRender(addFilterRender);
                }
                renderPipeline2.startRender();
            }
            if (aspectRatio) {
                Handler handler = this.mMainHandler;
                fX_IFitView.getClass();
                handler.post(new Runnable() {
                    public final void run() {
                        fX_IFitView.requestLayout();
                    }
                });
            }
            return renderPipeline2;
        }

        public RenderPipeline into(IFitView fX_IFitView) {
            return into(fX_IFitView, true);
        }
    }
}
