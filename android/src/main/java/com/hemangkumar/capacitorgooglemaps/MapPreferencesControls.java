package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;

public class MapPreferencesControls {
    public Boolean isCompassButtonEnabled;
    public Boolean isIndoorLevelPickerEnabled;
    public Boolean isMapToolbarEnabled;
    public Boolean isMyLocationButtonEnabled;
    public Boolean isZoomButtonsEnabled;

    public MapPreferencesControls() {
        this.isCompassButtonEnabled = true;
        this.isIndoorLevelPickerEnabled = false;
        this.isMapToolbarEnabled = false;
        this.isMyLocationButtonEnabled = true;
        this.isZoomButtonsEnabled = false;
    }

    public void updateFromJSObject(@Nullable JSObject jsObject) {
        if (jsObject != null) {
            if (jsObject.has("isCompassButtonEnabled")) {
                this.isCompassButtonEnabled = jsObject.getBool("isCompassButtonEnabled");
            }
            if (jsObject.has("isMapToolbarEnabled")) {
                this.isMapToolbarEnabled = jsObject.getBool("isMapToolbarEnabled");
            }
            if (jsObject.has("isMyLocationButtonEnabled")) {
                this.isMyLocationButtonEnabled = jsObject.getBool("isMyLocationButtonEnabled");
            }
            if (jsObject.has("isZoomButtonsEnabled")) {
                this.isZoomButtonsEnabled = jsObject.getBool("isZoomButtonsEnabled");
            }
        }
    }
}
