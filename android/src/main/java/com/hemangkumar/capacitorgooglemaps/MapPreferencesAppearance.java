package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.model.MapStyleOptions;

public class MapPreferencesAppearance {
    public Integer type;
    public MapStyleOptions style;
    public Boolean isBuildingsShown;
    public Boolean isIndoorShown;
    public Boolean isMyLocationDotShown;
    public Boolean isTrafficShown;

    public MapPreferencesAppearance() {
        this.type = 1;
        this.style = null;
        this.isBuildingsShown = true;
        this.isIndoorShown = true;
        this.isMyLocationDotShown = false;
        this.isTrafficShown = false;
    }

    public void updateFromJSObject(@Nullable JSObject jsObject) {
        if (jsObject != null) {
            // update mapType
            if (jsObject.has("type")) {
                Integer mapType = jsObject.getInteger("type", 1);
                if (mapType != null) {
                    if (mapType < 0 || mapType > 4) {
                        mapType = 1;
                    }
                    this.type = mapType;
                }
            }
            if (jsObject.has("style")) {
                String mapStyle = jsObject.getString("style", null);
                if (mapStyle == null) {
                    this.style = null;
                } else {
                    this.style = new MapStyleOptions(mapStyle);
                }
            }
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
