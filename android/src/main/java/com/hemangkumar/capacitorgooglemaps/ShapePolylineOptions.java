package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.PolylineOptions;

import java.util.List;

import kotlin.NotImplementedError;

public class ShapePolylineOptions implements ShapeOptions<ShapePolylineOptions, PolylineOptions> {
    private PolylineOptions options = new PolylineOptions();
    @Override
    public float getStrokeWidth() {
        return options.getWidth();
    }

    @Override
    public ShapePolylineOptions strokeWidth(float width) {
        options.width(width);
        return this;
    }

    @Override
    public int getStrokeColor() {
        return options.getColor();
    }

    @Override
    public ShapePolylineOptions strokeColor(int color) {
        options.color(color);
        return this;
    }

    @Override
    public int getFillColor() {
        return options.getColor();
    }

    @Override
    public ShapePolylineOptions fillColor(int color) {
        options.color(color);
        return this;
    }

    @Override
    public int getStrokeJointType() {
        return options.getJointType();
    }

    @Override
    public ShapePolylineOptions strokeJointType(int jointType) {
        options.jointType(jointType);
        return this;
    }

    @Override
    public List<PatternItem> getStrokePattern() {
        return options.getPattern();
    }

    @Override
    public ShapePolylineOptions strokePattern(List<PatternItem> pattern) {
        options.pattern(pattern);
        return this;
    }

    @Override
    public float getZIndex() {
        return options.getZIndex();
    }

    @Override
    public ShapePolylineOptions zIndex(float zIndex) {
        options.zIndex(zIndex);
        return this;
    }

    @Override
    public boolean isClickable() {
        return options.isClickable();
    }

    @Override
    public ShapePolylineOptions clickable(boolean clickable) {
        options.clickable(clickable);
        return this;
    }

    @Override
    public boolean isGeodesic() {
        return options.isGeodesic();
    }

    @Override
    public ShapePolylineOptions geodesic(boolean geodesic) {
        options.geodesic(geodesic);
        return this;
    }

    @Override
    public boolean isVisible() {
        return options.isVisible();
    }

    @Override
    public ShapePolylineOptions visible(boolean visible) {
        options.visible(visible);
        return this;
    }

    @Override
    public List<List<LatLng>> getHoles() {
        throw new NotImplementedError("addHole");
    }

    @Override
    public ShapePolylineOptions addHole(Iterable<LatLng> points) {
        throw new NotImplementedError("addHole");
    }

    @Override
    public PolylineOptions getNativeOptions() {
        return options;
    }
}
