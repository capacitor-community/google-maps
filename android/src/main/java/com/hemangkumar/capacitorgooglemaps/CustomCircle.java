package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Circle;
import com.google.android.libraries.maps.model.CircleOptions;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public class CustomCircle extends CustomShape<ShapeCircle> {
    private ShapeCircleOptions options;
    private final ShapeCircleTraits traits = new ShapeCircleTraits();

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
        options = new ShapeCircleOptions();
        return options;
    }

    @Override
    public ShapeCircle addToMap(GoogleMap googleMap) {
        Circle circle = googleMap.addCircle((CircleOptions) options.getNativeOptions());
        circle.setTag(tag);
        return new ShapeCircle(circle);
    }
}
