package com.scorpion.NeonphotoEditor.AutoBgRemove;

import android.graphics.PointF;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class PointGenerator implements Callable<Set<PointF>> {
    private final Set<PointF> partition;
    private final int radius;

    public PointGenerator(Set<PointF> set, int i) {
        this.partition = set;
        this.radius = i;
    }

    public Set<PointF> call() {
        HashSet hashSet = new HashSet();
        for (PointF next : this.partition) {
            float f = next.x;
            float f2 = next.y;
            for (float f3 = f - ((float) this.radius); f3 <= f; f3 += 1.0f) {
                for (float f4 = f2 - ((float) this.radius); f4 <= f2; f4 += 1.0f) {
                    float f5 = f3 - f;
                    float f6 = f4 - f2;
                    float f7 = (f5 * f5) + (f6 * f6);
                    int i = this.radius;
                    if (f7 <= ((float) (i * i))) {
                        float f8 = f - f5;
                        float f9 = f2 - f6;
                        hashSet.add(new PointF(f3, f4));
                        hashSet.add(new PointF(f3, f9));
                        hashSet.add(new PointF(f8, f4));
                        hashSet.add(new PointF(f8, f9));
                    }
                }
            }
        }
        return hashSet;
    }
}
