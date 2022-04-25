package com.hemangkumar.capacitorgooglemaps;

public class ShapePolygonTraits extends ShapeTraits {
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
}
