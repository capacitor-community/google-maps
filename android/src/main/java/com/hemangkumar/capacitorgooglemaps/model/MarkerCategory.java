package com.hemangkumar.capacitorgooglemaps.model;

import com.google.android.gms.maps.model.BitmapDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkerCategory {

    private int id;
    private String title;
    private int icon;

    static private Map<Integer, MarkerCategory> markerCategories = new HashMap();

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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public MarkerCategory(int id, String title, int icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;

        markerCategories.put(this.id, this);
    }

    public static MarkerCategory getMarkerCategoryById(int id) {
        return markerCategories.get(id);
    }
}
