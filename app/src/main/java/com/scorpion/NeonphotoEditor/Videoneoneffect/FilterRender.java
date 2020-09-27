package com.scorpion.NeonphotoEditor.Videoneoneffect;

import com.scorpion.NeonphotoEditor.Videoneoneffect.Particle.Geometry;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Particle.ParticleShooter;

import cn.ezandroid.ezfilter.core.cache.IBitmapCache;
import cn.ezandroid.ezfilter.core.cache.LruBitmapCache;

public class FilterRender extends FBORender {
    protected IBitmapCache mBitmapCache = LruBitmapCache.getSingleInstance();
    public boolean mIsPause = true;
    public ParticleShooter mParticleShooter;

    public void setBitmapCache(IBitmapCache iBitmapCache) {
        this.mBitmapCache = iBitmapCache;
    }

    public IBitmapCache getBitmapCache() {
        return this.mBitmapCache;
    }

    public void setPosition(Geometry.Point point) {
        if (this.mParticleShooter != null) {
            this.mParticleShooter.setPosition(point);
        }
    }

    public void start() {
        this.mIsPause = false;
    }

    public void pause() {
        this.mIsPause = true;
    }
}
