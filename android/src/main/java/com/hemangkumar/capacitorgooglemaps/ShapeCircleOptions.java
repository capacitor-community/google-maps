package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.CircleOptions;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public class ShapeCircleOptions extends ShapeOptions {
    private final CircleOptions options = new CircleOptions();

    @Override
    public float getStrokeWidth() {
        return options.getStrokeWidth();
    }

    @Override
    public void strokeWidth(float width) {
        options.strokeWidth(width);
    }

    @Override
    public int getStrokeColor() {
        return options.getStrokeColor();
    }

    @Override
    public void strokeColor(int color) {
        options.strokeColor(color);
    }

    @Override
    public int getFillColor() {
        return options.getFillColor();
    }

    @Override
    public void fillColor(int color) {
        options.fillColor(color);
    }

    @Override
    public List<PatternItem> getStrokePattern() {
        return options.getStrokePattern();
    }

    @Override
    public void strokePattern(List<PatternItem> pattern) {
        options.strokePattern(pattern);
    }

    @Override
    public float getZIndex() {
        return options.getZIndex();
    }

    @Override
    public void zIndex(float zIndex) {
        options.zIndex(zIndex);
    }

    @Override
    public boolean isClickable() {
        return options.isClickable();
    }

    @Override
    public void clickable(boolean clickable) {
        options.clickable(clickable);
    }

    @Override
    public boolean isVisible() {
        return options.isVisible();
    }

    @Override
    public void visible(boolean visible) {
        options.visible(visible);
    }

    @Override
    public CircleOptions getNativeOptions() {
        return options;
    }

    @Override
    public double getRadius() {
        return options.getRadius();
    }

    @Override
    public void radius(double radius) {
        options.radius(radius);
    }

    @Override
    public LatLng getCenter() {
        return options.getCenter();
    }

    @Override
    public void center(LatLng center) {
        options.center(center);
    }
}
