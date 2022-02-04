package com.hemangkumar.capacitorgooglemaps.model;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

public class MarkerCategory {

    private int id;
    private String title;
    private Bitmap icon;

    static private final Map<Integer, MarkerCategory> markerCategories = new HashMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public MarkerCategory(int id, String title, Bitmap icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;

        markerCategories.put(this.id, this);
    }

    public static MarkerCategory getMarkerCategoryById(int id) {
        if(markerCategories.containsKey(id)) {
            return markerCategories.get(id);
        } else {
            return markerCategories.get(0);
        }
    }
}
