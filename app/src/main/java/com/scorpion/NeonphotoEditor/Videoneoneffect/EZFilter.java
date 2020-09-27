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

    public static MultiBuilder input(List<Builder> list, MultiInput MultiInput1) {
        return new MultiBuilder(list, MultiInput1);
    }

    public static SplitBuilder input(Builder builder, SplitInput SplitInput1) {
        return new SplitBuilder(builder, SplitInput1);
    }

    public static abstract class Builder {
        protected boolean mEnableRecordAudio;
        protected boolean mEnableRecordVideo;
        protected List<FilterRender> mFilterRenders = new ArrayList();
        protected Handler mMainHandler = new Handler(Looper.getMainLooper());
        protected String mOutputPath;

        public abstract float getAspectRatio(IFitView IFitView1);

        public abstract FBORender getStartPointRender(IFitView IFitView1);

        protected Builder() {
        }

        public Builder addFilter(FilterRender FilterRender1) {
            if (FilterRender1 != null && !this.mFilterRenders.contains(FilterRender1)) {
                FilterRender1.setBitmapCache(EZFilter.sBitmapCache);
                this.mFilterRenders.add(FilterRender1);
            }
            return this;
        }

        public <T extends FilterRender & IAdjustable> Builder addFilter(T t, float f) {
            if (t != null && !this.mFilterRenders.contains(t)) {
                t.setBitmapCache(EZFilter.sBitmapCache);
                ((IAdjustable) t).adjust(f);
                this.mFilterRenders.add(t);
            }
            return this;
        }

        public Builder enableRecord(String str, boolean z, boolean z2) {
            this.mOutputPath = str;
            this.mEnableRecordVideo = z;
            this.mEnableRecordAudio = z2;
            return this;
        }

        public RenderPipeline into(IFitView IFitView1, boolean z) {
            RenderPipeline renderPipeline = IFitView1.getRenderPipeline();
            if (renderPipeline != null && z) {
                renderPipeline.clean();
            }
            IFitView1.initRenderPipeline(getStartPointRender(IFitView1));
            RenderPipeline renderPipeline2 = IFitView1.getRenderPipeline();
            boolean aspectRatio = IFitView1.setAspectRatio(getAspectRatio(IFitView1), 0, 0);
            IFitView1.requestRender();
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
                IFitView1.getClass();
                handler.post(new Runnable() {
                    public final void run() {
                        IFitView1.requestLayout();
                    }
                });
            }
            return renderPipeline2;
        }

        public RenderPipeline into(IFitView IFitView1) {
            return into(IFitView1, true);
        }
    }
}
