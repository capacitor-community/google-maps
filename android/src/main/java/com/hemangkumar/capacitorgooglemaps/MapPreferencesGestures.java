package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;

public class MapPreferencesGestures {
    public Boolean isRotateAllowed;
    public Boolean isScrollAllowed;
    public Boolean isScrollAllowedDuringRotateOrZoom;
    public Boolean isTiltAllowed;
    public Boolean isZoomAllowed;

    public MapPreferencesGestures() {
        this.isRotateAllowed = true;
        this.isScrollAllowed = true;
        this.isScrollAllowedDuringRotateOrZoom = true;
        this.isTiltAllowed = true;
        this.isZoomAllowed = true;
    }

    public void updateFromJSObject(@Nullable JSObject jsObject) {
        if (jsObject != null) {
            if (jsObject.has("isRotateAllowed")) {
                this.isRotateAllowed = jsObject.getBool("isRotateAllowed");
            }
            if (jsObject.has("isScrollAllowed")) {
                this.isScrollAllowed = jsObject.getBool("isScrollAllowed");
            }
            if (jsObject.has("isScrollAllowedDuringRotateOrZoom")) {
                this.isScrollAllowedDuringRotateOrZoom = jsObject.getBool("isScrollAllowedDuringRotateOrZoom");
            }
            if (jsObject.has("isTiltAllowed")) {
                this.isTiltAllowed = jsObject.getBool("isTiltAllowed");
            }
            if (jsObject.has("isZoomAllowed")) {
                this.isZoomAllowed = jsObject.getBool("isZoomAllowed");
            }
        }
    }
}
