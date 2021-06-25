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

    public void updateFromJSObject(@Nullable JSObject gesturesObject) {
        if (gesturesObject != null) {
            if (gesturesObject.has("isRotateAllowed")) {
                this.isRotateAllowed = gesturesObject.getBool("isRotateAllowed");
            }
            if (gesturesObject.has("isScrollAllowed")) {
                this.isScrollAllowed = gesturesObject.getBool("isScrollAllowed");
            }
            if (gesturesObject.has("isScrollAllowedDuringRotateOrZoom")) {
                this.isScrollAllowedDuringRotateOrZoom = gesturesObject.getBool("isScrollAllowedDuringRotateOrZoom");
            }
            if (gesturesObject.has("isTiltAllowed")) {
                this.isTiltAllowed = gesturesObject.getBool("isTiltAllowed");
            }
            if (gesturesObject.has("isZoomAllowed")) {
                this.isZoomAllowed = gesturesObject.getBool("isZoomAllowed");
            }
        }
    }
}
