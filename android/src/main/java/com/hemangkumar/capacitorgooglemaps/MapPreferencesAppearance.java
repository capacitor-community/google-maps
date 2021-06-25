package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;

public class MapPreferencesAppearance {
    public Boolean isBuildingsShown;
    public Boolean isIndoorShown;
    public Boolean isMyLocationDotShown;
    public Boolean isTrafficShown;

    public MapPreferencesAppearance() {
        this.isBuildingsShown = true;
        this.isIndoorShown = true;
        this.isMyLocationDotShown = false;
        this.isTrafficShown = false;
    }

    public void updateFromJSObject(@Nullable JSObject jsObject) {
        if (jsObject != null) {
            if (jsObject.has("isBuildingsShown")) {
                this.isBuildingsShown = jsObject.getBool("isBuildingsShown");
            }
            if (jsObject.has("isIndoorShown")) {
                this.isIndoorShown = jsObject.getBool("isIndoorShown");
            }
            if (jsObject.has("isMyLocationDotShown")) {
                this.isMyLocationDotShown = jsObject.getBool("isMyLocationDotShown");
            }
            if (jsObject.has("isTrafficShown")) {
                this.isTrafficShown = jsObject.getBool("isTrafficShown");
            }
        }
    }
}
