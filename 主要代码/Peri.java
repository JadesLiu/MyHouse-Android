package com.alisa.projet;

import android.graphics.Bitmap;

public class Peri {
    private int id;
    private String name;
    private String url;
    private Bitmap bmp;

    public Peri(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.bmp = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public String toString() {
        return url;
    }
}
