package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.Circle;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public class ShapeCircle extends Shape {
    private final Circle circle;

    public ShapeCircle(Circle circle) {
        this.circle = circle;
    }

    public float getStrokeWidth() {
        return circle.getStrokeWidth();
    }

    @Override
    public void setStrokeWidth(float width) {
        circle.setStrokeWidth(width);
    }

    @Override
    public int getStrokeColor() {
        return circle.getStrokeColor();
    }

    @Override
    public void setStrokeColor(int color) {
        circle.setStrokeColor(color);
    }

    @Override
    public int getFillColor() {
        return circle.getFillColor();
    }

    @Override
    public void setFillColor(int color) {
        circle.setFillColor(color);
    }

    @Override
    public List<PatternItem> getStrokePattern() {
        return circle.getStrokePattern();
    }

    @Override
    public void setStrokePattern(List<PatternItem> pattern) {
        circle.setStrokePattern(pattern);
    }

    @Override
    public float getZIndex() {
        return circle.getZIndex();
    }

    @Override
    public void setZIndex(float zIndex) {
        circle.setZIndex(zIndex);
    }

    @Override
    public boolean isClickable() {
        return circle.isClickable();
    }

    @Override
    public void setClickable(boolean clickable) {
        circle.setClickable(clickable);
    }

    @Override
    public boolean isVisible() {
        return circle.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        circle.setVisible(visible);
    }

    @Override
    public void remove() {
        circle.remove();
    }

    @Override
    public LatLng getCenter() {
        return circle.getCenter();
    }

    @Override
    public void setCenter(LatLng latLng) {
        circle.setCenter(latLng);
    }

    @Override
    public double getRadius() {
        return circle.getRadius();
    }

    @Override
    public void setRadius(double radius) {
        circle.setRadius(radius);
    }

    @Override
    public Object getTag() {
        return circle.getTag();
    }

    @Override
    public void setTag(Object tag) {
        circle.setTag(tag);
    }

    @Override
    public String getId() {
        return circle.getId();
    }

    @Override
    public String getShapeName() {
        return "circle";
    }

    @Override
    public Circle getNativeShape() {
        return circle;
    }
}
