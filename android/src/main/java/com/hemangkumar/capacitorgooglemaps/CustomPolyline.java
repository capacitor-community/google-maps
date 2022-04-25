package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polyline;
import com.google.android.libraries.maps.model.PolylineOptions;

import java.util.List;

public class CustomPolyline extends CustomShape<ShapePolyline> {
    private final ShapePolylineTraits traits = new ShapePolylineTraits();
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

    @Override
    public ShapePolyline addToMap(GoogleMap googleMap) {
        Polyline polyline = googleMap.addPolyline((PolylineOptions) options.getNativeOptions());
        polyline.setTag(tag);
        return new ShapePolyline(polyline);
    }
}