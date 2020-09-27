package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import cn.ezandroid.ezfilter.core.util.NumberUtil;

public class MediaUtil {
    public static final int AUDIO_BIT_RATE = 96000;
    public static final int BUFFER_SIZE = 262144;
    public static final int FRAME_RATE = 20;
    public static final int I_FRAME_INTERVAL = 1;
    public static final String KEY_ROTATION = "rotation-degrees";
    public static final String MIME_TYPE_AAC = "audio/mp4a-latm";
    public static final String MIME_TYPE_MP4 = "video/avc";

    private static int align16(int i) {
        return i % 16 > 0 ? ((i / 16) * 16) + 16 : i;
    }

    public static MediaFormat createVideoFormat(int i, int i2) {
        return createVideoFormat(align16(i), align16(i2), 2130708361);
    }

    public static MediaFormat createVideoFormat(int i, int i2, int i3) {
        int align16 = align16(i);
        int align162 = align16(i2);
        double d = (double) i;
        Double.isNaN(d);
        double d2 = (double) i2;
        Double.isNaN(d2);
        return createVideoFormat(align16, align162, (int) (d * 4.0d * d2), i3);
    }

    public static MediaFormat createVideoFormat(int i, int i2, int i3, int i4) {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", i, i2);
        createVideoFormat.setInteger("color-format", i4);
        createVideoFormat.setInteger("bitrate", i3);
        createVideoFormat.setInteger("frame-rate", 20);
        createVideoFormat.setInteger("i-frame-interval", 1);
        return createVideoFormat;
    }

    public static MediaFormat createAudioFormat(int i, int i2, int i3) {
        MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", i, i3);
        createAudioFormat.setInteger("aac-profile", 2);
        createAudioFormat.setInteger("channel-mask", i2);
        createAudioFormat.setInteger("channel-count", i3);
        createAudioFormat.setInteger("bitrate", 96000);
        createAudioFormat.setInteger("max-input-size", 262144);
        return createAudioFormat;
    }

    public static Track getFirstTrack(MediaExtractor mediaExtractor) {
        Track track = new Track();
        track.videoTrackIndex = -1;
        track.audioTrackIndex = -1;
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString("mime");
            if (track.videoTrackIndex < 0 && string.startsWith("video/")) {
                track.videoTrackIndex = i;
                track.videoTrackMime = string;
                track.videoTrackFormat = trackFormat;
            } else if (track.audioTrackIndex < 0 && string.startsWith("audio/")) {
                track.audioTrackIndex = i;
                track.audioTrackMime = string;
                track.audioTrackFormat = trackFormat;
            }
            if (track.videoTrackIndex >= 0 && track.audioTrackIndex >= 0) {
                break;
            }
        }
        if (track.videoTrackIndex >= 0 || track.audioTrackIndex >= 0) {
            return track;
        }
        Log.e("FX_MediaUtil", "Not found video/audio track.");
        return null;
    }


    public static Metadata getMetadata(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(str);
            String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(18);
            String extractMetadata3 = mediaMetadataRetriever.extractMetadata(19);
            String extractMetadata4 = mediaMetadataRetriever.extractMetadata(20);
            String extractMetadata5 = mediaMetadataRetriever.extractMetadata(12);
            String extractMetadata6 = mediaMetadataRetriever.extractMetadata(24);
            String extractMetadata7 = mediaMetadataRetriever.extractMetadata(10);
            Metadata metadata = new Metadata();
            metadata.duration = NumberUtil.parseLong(extractMetadata);
            metadata.width = NumberUtil.parseInt(extractMetadata2);
            metadata.height = NumberUtil.parseInt(extractMetadata3);
            metadata.bitrate = NumberUtil.parseInt(extractMetadata4, 1);
            metadata.rotation = NumberUtil.parseInt(extractMetadata6);
            metadata.tracks = NumberUtil.parseInt(extractMetadata7);
            metadata.mimeType = extractMetadata5;
            mediaMetadataRetriever.release();
            return metadata;
        } catch (RuntimeException e) {
            e.printStackTrace();
            mediaMetadataRetriever.release();
            return new Metadata();
        } catch (Throwable th) {
            mediaMetadataRetriever.release();
            throw th;
        }
    }

    public static class Track {
        public MediaFormat audioTrackFormat;
        public int audioTrackIndex;
        public String audioTrackMime;
        public MediaFormat videoTrackFormat;
        public int videoTrackIndex;
        public String videoTrackMime;

        private Track() {
        }
    }

    public static class Metadata {
        public int bitrate;
        public long duration;
        public int height;
        public String mimeType;
        public int rotation;
        public int tracks;
        public int width;

        public String toString() {
            return "Metadata{mimeType='" + this.mimeType + '\'' + ", width=" + this.width + ", height=" + this.height + ", duration=" + this.duration + ", rotation=" + this.rotation + ", tracks=" + this.tracks + ", bitrate=" + this.bitrate + '}';
        }
    }
}
