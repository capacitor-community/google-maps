package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.google.android.gms.maps.model.MapStyleOptions;

public class MapPreferencesAppearance {
    public Integer type;
    public MapStyleOptions style;
    public Boolean isBuildingsShown;
    public Boolean isIndoorShown;
    public Boolean isMyLocationDotShown;
    public Boolean isTrafficShown;

    public static final String TYPE_KEY = "type";
    public static final String STYLE_KEY = "style";
    public static final String BUILDINGS_SHOWN_KEY = "isBuildingsShown";
    public static final String INDOOR_SHOWN_KEY = "isIndoorShown";
    public static final String MY_LOCATION_DOT_SHOWN_KEY = "isMyLocationDotShown";
    public static final String TRAFFIC_SHOWN_KEY = "isTrafficShown";

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
            if (jsObject.has(TYPE_KEY)) {
                Integer mapType = jsObject.getInteger(TYPE_KEY, 1);
                if (mapType != null) {
                    if (mapType < 0 || mapType > 4) {
                        mapType = 1;
                    }
                    this.type = mapType;
                }
            }
            if (jsObject.has(STYLE_KEY)) {
                String mapStyle = jsObject.getString(STYLE_KEY, null);
                if (mapStyle == null) {
                    this.style = null;
                } else {
                    this.style = new MapStyleOptions(mapStyle);
                }
            }
            if (jsObject.has(BUILDINGS_SHOWN_KEY)) {
                this.isBuildingsShown = jsObject.getBool(BUILDINGS_SHOWN_KEY);
            }
            if (jsObject.has(INDOOR_SHOWN_KEY)) {
                this.isIndoorShown = jsObject.getBool(INDOOR_SHOWN_KEY);
            }
            if (jsObject.has(MY_LOCATION_DOT_SHOWN_KEY)) {
                this.isMyLocationDotShown = jsObject.getBool(MY_LOCATION_DOT_SHOWN_KEY);
            }
            if (jsObject.has(TRAFFIC_SHOWN_KEY)) {
                this.isTrafficShown = jsObject.getBool(TRAFFIC_SHOWN_KEY);
            }
        }
    }
}
