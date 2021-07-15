package com.alisa.projet;

import android.graphics.Bitmap;

public class Device {
    private int id;
    private String name;
    private String type;
    private String url;
    private int status;
    private Bitmap bmp;

    public Device(int id, String name, String type, String url, int status) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
        this.status = status;
        this.bmp = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getStatus() {
        return status;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public void setStatus(int status) { this.status = status; }

    public String toString() {
        return url;
    }
}
