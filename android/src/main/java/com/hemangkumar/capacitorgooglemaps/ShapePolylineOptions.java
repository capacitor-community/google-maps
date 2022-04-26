package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.PolylineOptions;

import java.util.List;

public class ShapePolylineOptions extends ShapeOptions {
    private final PolylineOptions options = new PolylineOptions();

    @Override
    public int getFillColor() {
        return options.getColor();
    }

    @Override
    public void fillColor(int color) {
        options.color(color);
    }

    @Override
    public int getStrokeJointType() {
        return options.getJointType();
    }

    @Override
    public void strokeJointType(int jointType) {
        options.jointType(jointType);
    }

    @Override
    public List<PatternItem> getStrokePattern() {
        return options.getPattern();
    }

    @Override
    public void strokePattern(List<PatternItem> pattern) {
        options.pattern(pattern);
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
    public boolean isGeodesic() {
        return options.isGeodesic();
    }

    @Override
    public void geodesic(boolean geodesic) {
        options.geodesic(geodesic);
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
    public List<LatLng> getPoints() {
        return options.getPoints();
    }

    @Override
    public void add(LatLng latLng) {
        options.add(latLng);
    }

    @Override
    public PolylineOptions getNativeOptions() {
        return options;
    }

    @Override
    public List<PatternItem> getPattern() {
        return options.getPattern();
    }

    @Override
    public void pattern(List<PatternItem> patternItems) {
        options.pattern(patternItems);
    }

    @Override
    public int getJointType() {
        return options.getJointType();
    }

    @Override
    public void jointType(int jointType) {
        options.jointType(jointType);
    }

    @Override
    public float getWidth() {
        return options.getWidth();
    }

    @Override
    public void width(float width) {
        options.width(width);
    }

    @Override
    public int getColor() {
        return options.getColor();
    }

    @Override
    public void color(int color) {
        options.color(color);
    }
}
