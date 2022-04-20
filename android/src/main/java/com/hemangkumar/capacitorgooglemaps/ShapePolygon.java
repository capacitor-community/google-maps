package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polygon;

import java.util.List;

public class ShapePolygon implements Shape {

    private final Polygon polygon;

    public ShapePolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public float getStrokeWidth() {
        return polygon.getStrokeWidth();
    }

    @Override
    public void setStrokeWidth(float width) {
        polygon.setStrokeWidth(width);
    }

    @Override
    public int getStrokeColor() {
        return polygon.getStrokeColor();
    }

    @Override
    public void setStrokeColor(int color) {
        polygon.setStrokeColor(color);
    }

    @Override
    public int getFillColor() {
        return polygon.getFillColor();
    }

    @Override
    public void setFillColor(int color) {
        polygon.setFillColor(color);
    }

    @Override
    public int getStrokeJointType() {
        return polygon.getStrokeJointType();
    }

    @Override
    public void setStrokeJointType(int jointType) {
        polygon.setStrokeJointType(jointType);
    }

    @Override
    public List<PatternItem> getStrokePattern() {
        return polygon.getStrokePattern();
    }

    @Override
    public void setStrokePattern(List<PatternItem> pattern) {
        polygon.setStrokePattern(pattern);
    }

    @Override
    public float getZIndex() {
        return polygon.getZIndex();
    }

    @Override
    public void setZIndex(float zIndex) {
        polygon.setZIndex(zIndex);
    }

    @Override
    public boolean isClickable() {
        return polygon.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        polygon.setClickable(clickable);
    }

    @Override
    public boolean isGeodesic() {
        return polygon.isGeodesic();
    }

    @Override
    public void setGeodesic(boolean geodesic) {
        polygon.setGeodesic(geodesic);
    }

    @Override
    public boolean isVisible() {
        return polygon.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        polygon.setVisible(visible);
    }

    @Override
    public List<List<LatLng>> getHoles() {
        return polygon.getHoles();
    }

    @Override
    public void setHoles(List<? extends List<LatLng>> points) {
        polygon.setHoles(points);
    }

    @Override
    public Object getNativeOptions() {
        return polygon;
    }
}
