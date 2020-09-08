package com.scorpion.NeonphotoEditor.Videoneoneffect;

import java.nio.ShortBuffer;

interface AudioRemixer {
    public static final AudioRemixer DOWNMIX = new AudioRemixer() {
        private static final int SIGNED_SHORT_LIMIT = 32768;
        private static final int UNSIGNED_SHORT_MAX = 65535;

        public void remix(ShortBuffer shortBuffer, ShortBuffer shortBuffer2) {
            int i;
            int min = Math.min(shortBuffer.remaining() / 2, shortBuffer2.remaining());
            for (int i2 = 0; i2 < min; i2++) {
                int i3 = shortBuffer.get() +SIGNED_SHORT_LIMIT;
                int i4 = shortBuffer.get() + UNSIGNED_SHORT_MAX;
                if (i3 < 32768 || i4 < 32768) {
                    i = (i3 * i4) / 32768;
                } else {
                    i = (((i3 + i4) * 2) - ((i3 * i4) / 32768)) - 65535;
                }
                if (i == 65536) {
                    i = 65535;
                }
                shortBuffer2.put((short) (i - 32768));
            }
        }
    };
    public static final AudioRemixer PASSTHROUGH = new AudioRemixer() {
        public void remix(ShortBuffer shortBuffer, ShortBuffer shortBuffer2) {
            shortBuffer2.put(shortBuffer);
        }
    };
    public static final AudioRemixer UPMIX = new AudioRemixer() {
        public void remix(ShortBuffer shortBuffer, ShortBuffer shortBuffer2) {
            int min = Math.min(shortBuffer.remaining(), shortBuffer2.remaining() / 2);
            for (int i = 0; i < min; i++) {
                short s = shortBuffer.get();
                shortBuffer2.put(s);
                shortBuffer2.put(s);
            }
        }
    };

    void remix(ShortBuffer shortBuffer, ShortBuffer shortBuffer2);
}
