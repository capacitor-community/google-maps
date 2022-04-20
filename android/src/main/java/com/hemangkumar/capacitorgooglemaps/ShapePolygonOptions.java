package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.PolygonOptions;

import java.util.List;

public class ShapePolygonOptions implements ShapeOptions<ShapePolygonOptions, PolygonOptions> {

    private final PolygonOptions options = new PolygonOptions();

    @Override
    public float getStrokeWidth() {
        return options.getStrokeWidth();
    }

    @Override
    public ShapePolygonOptions strokeWidth(float width) {
        options.strokeWidth(width);
        return this;
    }

    @Override
    public int getStrokeColor() {
        return options.getStrokeColor();
    }

    @Override
    public ShapePolygonOptions strokeColor(int color) {
        options.strokeColor(color);
        return this;
    }

    @Override
    public int getFillColor() {
        return options.getFillColor();
    }

    @Override
    public ShapePolygonOptions fillColor(int color) {
        options.fillColor(color);
        return this;
    }

    @Override
    public int getStrokeJointType() {
        return options.getStrokeJointType();
    }

    @Override
    public ShapePolygonOptions strokeJointType(int jointType) {
        options.strokeJointType(jointType);
        return this;
    }

    @Override
    public List<PatternItem> getStrokePattern() {
        return options.getStrokePattern();
    }

    @Override
    public ShapePolygonOptions strokePattern(List<PatternItem> pattern) {
        options.strokePattern(pattern);
        return this;
    }

    @Override
    public float getZIndex() {
        return options.getZIndex();
    }

    @Override
    public ShapePolygonOptions zIndex(float zIndex) {
        options.zIndex(zIndex);
        return this;
    }

    @Override
    public boolean isClickable() {
        return options.isClickable();
    }

    @Override
    public ShapePolygonOptions clickable(boolean clickable) {
        options.clickable(clickable);
        return this;
    }

    @Override
    public boolean isGeodesic() {
        return options.isGeodesic();
    }

    @Override
    public ShapePolygonOptions geodesic(boolean geodesic) {
        options.geodesic(geodesic);
        return this;
    }

    @Override
    public boolean isVisible() {
        return options.isVisible();
    }

    @Override
    public ShapePolygonOptions visible(boolean visible) {
        options.visible(visible);
        return this;
    }

    @Override
    public List<List<LatLng>> getHoles() {
        return options.getHoles();
    }

    @Override
    public ShapePolygonOptions addHole(Iterable<LatLng> points) {
        options.addHole(points);
        return this;
    }

    @Override
    public PolygonOptions getNativeOptions() {
        return options;
    }
}
