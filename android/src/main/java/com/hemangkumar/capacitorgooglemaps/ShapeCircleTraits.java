package com.hemangkumar.capacitorgooglemaps;

public class ShapeCircleTraits extends ShapeTraits {

    public static final ShapeCircleTraits INSTANCE = new ShapeCircleTraits();

    private ShapeCircleTraits() {

    }

    @Override
    public boolean hasAboveMarkers() {
        return true;
    }

    @Override
    public boolean hasStrokeWidth() {
        return true;
    }

    @Override
    public boolean hasStrokeColor() {
        return true;
    }

    @Override
    public boolean hasFillColor() {
        return true;
    }

    @Override
    public boolean hasStrokePatterns() {
        return true;
    }

    @Override
    public boolean hasCenter() {
        return true;
    }

    @Override
    public boolean hasRadius() {
        return true;
    }
}
