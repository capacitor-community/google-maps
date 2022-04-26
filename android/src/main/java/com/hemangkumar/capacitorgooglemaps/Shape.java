package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public abstract class Shape {

    public float getWidth() {
        return 0;
    }

    public void setWidth(float width) {

    }

    public int getColor() {
        return 0;
    }

    public void setColor(int color) {

    }

    public float getStrokeWidth() {
        return 0;
    }

    public void setStrokeWidth(float width) {

    }

    public int getStrokeColor() {
        return 0;
    }

    public void setStrokeColor(int color) {

    }

    public int getFillColor() {
        return 0;
    }

    public void setFillColor(int color) {

    }

    public int getStrokeJointType() {
        return 0;
    }

    public void setStrokeJointType(int jointType) {

    }

    public List<PatternItem> getStrokePattern() {
        return null;
    }

    public void setStrokePattern(List<PatternItem> pattern) {

    }

    public List<PatternItem> getPattern() {
        return null;
    }

    public void setPattern(List<PatternItem> pattern) {

    }

    public abstract float getZIndex();

    public abstract void setZIndex(float zIndex);

    public abstract boolean isClickable();

    public abstract void setClickable(boolean clickable);

    public boolean isGeodesic() {
        return false;
    }

    void setGeodesic(boolean geodesic) {

    }

    public abstract boolean isVisible();

    public abstract void setVisible(boolean visible);

    public abstract void remove();

    public List<List<LatLng>> getHoles() {
        return null;
    }

    public void setHoles(List<List<LatLng>> holes) {

    }

    public void setPoints(List<LatLng> points) {

    }

    public List<LatLng> getPoints() {
        return null;
    }

    public LatLng getCenter() {
        return null;
    }

    public void setCenter(LatLng latLng) {

    }

    public double getRadius() {
        return 0;
    }

    public void setRadius(double radius) {

    }

    public abstract Object getTag();

    public abstract void setTag(Object tag);

    public abstract String getId();

    public abstract String getShapeName();

    public abstract Object getNativeShape();

    public int getJointType() {
        return 0;
    }

    public void setJointType(int jointType) {

    }
}
