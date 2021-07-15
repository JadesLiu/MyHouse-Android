package com.alisa.projet;

import android.graphics.Bitmap;

public class Sensor {
    private int id;
    private String name;
    private String type;
    private String url;
    private Bitmap bmp;

    public Sensor(int id, String name, String type, String url) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
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
