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

    public void updateFromJSObject(@Nullable JSObject gesturesObject) {
        if (gesturesObject != null) {
            if (gesturesObject.has("isCompassButtonEnabled")) {
                this.isCompassButtonEnabled = gesturesObject.getBool("isCompassButtonEnabled");
            }
            if (gesturesObject.has("isMapToolbarEnabled")) {
                this.isMapToolbarEnabled = gesturesObject.getBool("isMapToolbarEnabled");
            }
            if (gesturesObject.has("isMyLocationButtonEnabled")) {
                this.isMyLocationButtonEnabled = gesturesObject.getBool("isMyLocationButtonEnabled");
            }
            if (gesturesObject.has("isZoomButtonsEnabled")) {
                this.isZoomButtonsEnabled = gesturesObject.getBool("isZoomButtonsEnabled");
            }
        }
    }
}
