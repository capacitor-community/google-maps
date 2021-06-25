package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;

public class BoundingRect {
    public Integer width;
    public Integer height;
    public Integer x;
    public Integer y;

    public BoundingRect() {
        this.width = 500;
        this.height = 500;
        this.x = 0;
        this.y = 0;
    }

    public void updateFromJSObject(@Nullable JSObject jsObject) {
        if (jsObject != null) {
            if (jsObject.has("width")) {
                this.width = jsObject.getInteger("width");
            }
            if (jsObject.has("height")) {
                this.height = jsObject.getInteger("height");
            }
            if (jsObject.has("x")) {
                this.x = jsObject.getInteger("x");
            }
            if (jsObject.has("y")) {
                this.y = jsObject.getInteger("y");
            }
        }
    }
}
