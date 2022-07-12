package com.fom.rapid.model;

import android.annotation.TargetApi;
import android.os.Build;

public class MediaObject {

    private String bucketId;
    private String bucketName;
    private String id;
    private String name;
    private String uri;
    private String mime;
    private String album;
    private String art;
    private String artist;
    private String composer;
    private String genre;
    private String year;
    private String resolution;
    private String resolutionWidth;
    private String resolutionHeight;
    private long size;
    private long date;
    private long duration;
    private boolean selected;

    public MediaObject() {
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @TargetApi(Build.VERSION_CODES.R)
    public String getResolutionWidth() {
        return resolutionWidth;
    }

    @TargetApi(Build.VERSION_CODES.R)
    public void setResolutionWidth(String resolutionWidth) {
        this.resolutionWidth = resolutionWidth;
    }

    @TargetApi(Build.VERSION_CODES.R)
    public String getResolutionHeight() {
        return resolutionHeight;
    }

    @TargetApi(Build.VERSION_CODES.R)
    public void setResolutionHeight(String resolutionHeight) {
        this.resolutionHeight = resolutionHeight;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
