package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public interface ShapeOptions<T, O> {
    float getStrokeWidth();
    T strokeWidth(float width);

    int getStrokeColor();
    T strokeColor(int color);

    int getFillColor();
    T fillColor(int color);

    int getStrokeJointType();
    T strokeJointType(int jointType);

    List<PatternItem> getStrokePattern();
    T strokePattern(List<PatternItem> pattern);

    float getZIndex();
    T zIndex(float zIndex);

    boolean isClickable();
    T clickable(boolean clickable);

    boolean isGeodesic();
    T geodesic(boolean geodesic);

    boolean isVisible();
    T visible(boolean visible);

    List<List<LatLng>> getHoles();
    T addHole(Iterable<LatLng> points);

    O getNativeOptions();
}
