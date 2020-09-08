package com.scorpion.NeonphotoEditor.util;

import android.graphics.Bitmap;

public class Vdata {
    String album;
    String artist;
    Bitmap bitmap;
    String category;
    String date_added;
    String date_taken;
    String description;
    String duration;
    long id;
    String mime;
    String name;
    String path;
    String resolution;
    String tags;
    String title;

    public long getId() {
        return this.id;
    }

    public void setID(long j) {
        this.id = j;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String str) {
        this.category = str;
    }

    public String getResolution() {
        return this.resolution;
    }

    public void setResolution(String str) {
        this.resolution = str;
    }

    public String getMime() {
        return this.mime;
    }

    public void setMime(String str) {
        this.mime = str;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String str) {
        this.tags = str;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap2) {
        this.bitmap = bitmap2;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String str) {
        this.artist = str;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String str) {
        this.album = str;
    }

    public String getDate_taken() {
        return this.date_taken;
    }

    public void setDate_taken(String str) {
        this.date_taken = str;
    }

    public String getDate_added() {
        return this.date_added;
    }

    public void setDate_added(String str) {
        this.date_added = str;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String str) {
        this.duration = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }
}
