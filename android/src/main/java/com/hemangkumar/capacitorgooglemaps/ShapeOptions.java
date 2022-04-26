package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public abstract class ShapeOptions {
    public float getStrokeWidth() {
        return 0;
    }

    public void strokeWidth(float width) {
        // nothing
    }

    public int getStrokeColor() {
        return 0;
    }

    public void strokeColor(int color) {
        // nothing
    }

    public int getFillColor() {
        return 0;
    }

    public void fillColor(int color) {
        // nothing
    }

    public int getStrokeJointType() {
        return 0;
    }

    public void strokeJointType(int jointType) {
        // nothing
    }

    public List<PatternItem> getStrokePattern() {
        return null;
    }

    public void strokePattern(List<PatternItem> pattern) {
        // nothing
    }

    public abstract float getZIndex();

    public abstract void zIndex(float zIndex);

    public abstract boolean isClickable();

    public abstract void clickable(boolean clickable);

    public boolean isGeodesic() {
        return false;
    }

    public void geodesic(boolean geodesic) {
        // nothing
    }

    public abstract boolean isVisible();

    public abstract void visible(boolean visible);

    public List<List<LatLng>> getHoles() {
        return null;
    }

    public void addHole(Iterable<LatLng> points) {
        // nothing
    }

    public List<LatLng> getPoints() {
        return null;
    }

    public void add(LatLng latLng) {
        // nothing
    }

    public abstract Object getNativeOptions();

    public List<PatternItem> getPattern() {
        return null;
    }

    public void pattern(List<PatternItem> patternItems) {
        // nothing
    }

    public int getJointType() {
        return 0;
    }

    public void jointType(int jointType) {
        // nothing
    }

    public float getWidth() {
        return 0;
    }

    public void width(float width) {
        // nothing
    }

    public int getColor() {
        return 0;
    }

    public void color(int color) {
        // nothing
    }

    public double getRadius() {
        return 0;
    }

    public void radius(double radius) {
        // nothing
    }

    public LatLng getCenter() {
        return null;
    }

    public void center(LatLng center) {
        // nothing
    }

    public IconDescriptor getPatternIconDescriptor() {
        return null;
    }

    public void patternIconDescriptor(IconDescriptor iconDescriptor) {
        // nothing
    }
}
