package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Circle;

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

    public ShapeCircle addToMap(GoogleMap googleMap) {
        Circle circle = googleMap.addCircle(options.getNativeOptions());
        circle.setTag(tag);
        return new ShapeCircle(circle);
    }
}
