package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Polyline;

public class CustomPolyline extends CustomShape<ShapePolyline> {
    private final ShapePolylineTraits traits = ShapePolylineTraits.INSTANCE;
    private ShapePolylineOptions options;

    @Override
    protected ShapeOptions getOptions() {
        return options;
    }

    @Override
    protected ShapeTraits getShapeTraits() {
        return traits;
    }

    @Override
    protected ShapeOptions newOptions() {
        options = new ShapePolylineOptions();
        return options;
    }

    public ShapePolyline addToMap(GoogleMap googleMap) {
        Polyline polyline = googleMap.addPolyline(options.getNativeOptions());
        polyline.setTag(tag);
        return new ShapePolyline(polyline);
    }
}