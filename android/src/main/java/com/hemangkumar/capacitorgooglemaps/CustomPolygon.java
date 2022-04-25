package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class CustomPolygon extends CustomShape<ShapePolygon> {
    private final ShapePolygonTraits traits = new ShapePolygonTraits();
    private ShapePolygonOptions options;

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
        options = new ShapePolygonOptions();
        return options;
    }

    @Override
    public ShapePolygon addToMap(GoogleMap googleMap) {
        Polygon polygon = googleMap.addPolygon(options.getNativeOptions());
        polygon.setTag(tag);
        return new ShapePolygon(polygon);
    }
}
