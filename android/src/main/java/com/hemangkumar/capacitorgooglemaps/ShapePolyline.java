package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class ShapePolyline extends Shape {

    private final Polyline polyline;

    public ShapePolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    @Override
    public float getWidth() {
        return polyline.getWidth();
    }

    @Override
    public void setWidth(float width) {
        polyline.setWidth(width);
    }

    @Override
    public int getColor() {
        return polyline.getColor();
    }

    @Override
    public void setColor(int color) {
        polyline.setColor(color);
    }

    @Override
    public void setPattern(List<PatternItem> pattern) {
        polyline.setPattern(pattern);
    }

    @Override
    public List<PatternItem> getPattern() {
        return polyline.getPattern();
    }

    @Override
    public float getZIndex() {
        return polyline.getZIndex();
    }

    @Override
    public void setZIndex(float zIndex) {
        polyline.setZIndex(zIndex);
    }

    @Override
    public boolean isClickable() {
        return polyline.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        polyline.setClickable(clickable);
    }

    @Override
    public boolean isGeodesic() {
        return polyline.isGeodesic();
    }

    @Override
    public void setGeodesic(boolean geodesic) {
        polyline.setGeodesic(geodesic);
    }

    @Override
    public boolean isVisible() {
        return polyline.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        polyline.setVisible(visible);
    }

    @Override
    public void setPoints(List<LatLng> points) {
        // points should not be empty, otherwise the app crashes
        if (points != null && !points.isEmpty()) {
            polyline.setPoints(points);
        }
    }

    @Override
    public List<LatLng> getPoints() {
        return polyline.getPoints();
    }

    @Override
    public Object getTag() {
        return polyline.getTag();
    }

    @Override
    public void setTag(Object tag) {
        polyline.setTag(tag);
    }

    @Override
    public String getId() {
        return polyline.getId();
    }

    @Override
    public String getShapeName() {
        return "polyline";
    }

    @Override
    public Polyline getNativeShape() {
        return polyline;
    }

    @Override
    public int getJointType() {
        return polyline.getJointType();
    }

    @Override
    public void setJointType(int jointType) {
        polyline.setJointType(jointType);
    }
}
