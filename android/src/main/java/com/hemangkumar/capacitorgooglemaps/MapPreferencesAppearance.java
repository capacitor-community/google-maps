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

    public void updateFromJSObject(@Nullable JSObject gesturesObject) {
        if (gesturesObject != null) {
            if (gesturesObject.has("isBuildingsShown")) {
                this.isBuildingsShown = gesturesObject.getBool("isBuildingsShown");
            }
            if (gesturesObject.has("isIndoorShown")) {
                this.isIndoorShown = gesturesObject.getBool("isIndoorShown");
            }
            if (gesturesObject.has("isMyLocationDotShown")) {
                this.isMyLocationDotShown = gesturesObject.getBool("isMyLocationDotShown");
            }
            if (gesturesObject.has("isTrafficShown")) {
                this.isTrafficShown = gesturesObject.getBool("isTrafficShown");
            }
        }
    }
}
