package com.lbb.christophemei.epicture;

import android.graphics.Bitmap;

/**
 * Created by Christophe Mei on 08/02/2018.
 */

public class ListItem {
    private String headline;
    private String url;
    private int fav;
    private String id;
    public Bitmap image;
    public int first = 0;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[ headline=" + headline + "]";
    }
}
