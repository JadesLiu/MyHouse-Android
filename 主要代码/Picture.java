package com.alisa.projet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Picture {
    private int id;
    private String url;
    private Bitmap bmp;

    public Picture(int id, String url) {
        this.id = id;
        this.url = url;
        this.bmp = null;
    }

    public int getId() {
        return id;
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
