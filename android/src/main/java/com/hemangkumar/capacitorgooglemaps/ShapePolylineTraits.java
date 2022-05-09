package com.hemangkumar.capacitorgooglemaps;

public class ShapePolylineTraits extends ShapeTraits {

    public final static ShapePolylineTraits INSTANCE = new ShapePolylineTraits();

    private ShapePolylineTraits() {

    }

    @Override
    public boolean hasWidth() {
        return true;
    }

    @Override
    public boolean hasColor() {
        return true;
    }

    @Override
    public boolean hasPattern() {
        return true;
    }

    @Override
    public boolean hasPoints() {
        return true;
    }

    @Override
    public boolean hasJointType() {
        return true;
    }
}
