package com.hemangkumar.capacitorgooglemaps;

public class ShapePolygonTraits extends ShapeTraits {
    public final static ShapePolygonTraits INSTANCE = new ShapePolygonTraits();

    private ShapePolygonTraits() {

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
    public boolean hasStrokeJointType() {
        return true;
    }

    @Override
    public boolean hasStrokePatterns() {
        return true;
    }

    @Override
    public boolean hasGeodesic() {
        return true;
    }

    @Override
    public boolean hasHoles() {
        return true;
    }

    @Override
    public boolean hasPoints() {
        return true;
    }

    @Override
    public boolean hasPatternIcon() {
        return true;
    }
}
