package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public interface Shape {
    float getStrokeWidth();
    void setStrokeWidth(float width);

    int getStrokeColor();
    void setStrokeColor(int color);

    int getFillColor();
    void setFillColor(int color);

    int getStrokeJointType();
    void setStrokeJointType(int jointType);

    List<PatternItem> getStrokePattern();
    void setStrokePattern(List<PatternItem> pattern);

    float getZIndex();
    void setZIndex(float zIndex);

    boolean isClickable();
    void setClickable(boolean clickable);

    boolean isGeodesic();
    void setGeodesic(boolean geodesic);

    boolean isVisible();
    void setVisible(boolean visible);

    List<List<LatLng>> getHoles();
    void setHoles(List<? extends List<LatLng>> holes);

    Object getNativeOptions();
}
