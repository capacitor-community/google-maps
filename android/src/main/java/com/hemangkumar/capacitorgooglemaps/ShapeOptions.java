package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public interface ShapeOptions {
    float getStrokeWidth();
    void strokeWidth(float width);

    int getStrokeColor();
    void strokeColor(int color);

    int getFillColor();
    void fillColor(int color);

    int getStrokeJointType();
    void strokeJointType(int jointType);

    List<PatternItem> getStrokePattern();
    void strokePattern(List<PatternItem> pattern);

    float getZIndex();
    void zIndex(float zIndex);

    boolean isClickable();
    void clickable(boolean clickable);

    boolean isGeodesic();
    void geodesic(boolean geodesic);

    boolean isVisible();
    void visible(boolean visible);

    List<List<LatLng>> getHoles();
    void addHole(Iterable<LatLng> points);

    List<LatLng> getPoints();
    void add(LatLng latLng);

    Object getNativeOptions();

    List<PatternItem> getPattern();
    void pattern(List<PatternItem> patternItems);

    int getJointType();
    void jointType(int jointType);

    float getWidth();
    void width(float width);

    int getColor();
    void color(int color);

    double getRadius();
    void radius(double radius);

    LatLng getCenter();
    void center(LatLng center);
}
