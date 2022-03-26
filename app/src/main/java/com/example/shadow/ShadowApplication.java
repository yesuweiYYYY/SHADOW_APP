package com.example.shadow;

import android.app.Application;
import android.graphics.Bitmap;

public class ShadowApplication extends Application {
    private String username;
    private Bitmap head;

    public void onCreate()
    {
        super.onCreate();

    }

    public Bitmap getHead() {
        return head;
    }

    public void setHead(Bitmap head) {
        this.head = head;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
