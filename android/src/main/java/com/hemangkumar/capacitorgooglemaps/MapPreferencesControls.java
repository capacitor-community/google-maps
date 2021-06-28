package com.hemangkumar.capacitorgooglemaps;

import java.util.HashMap;

public class MapPreferencesControls extends JSObjectDefaults {
    public static final String COMPASS_BUTTON_KEY = "isCompassButtonEnabled";
    public static final String INDOOR_LEVEL_PICKER_KEY = "isIndoorLevelPickerEnabled";
    public static final String MAP_TOOLBAR_KEY = "isMapToolbarEnabled";
    public static final String MY_LOCATION_BUTTON_KEY = "isMyLocationButtonEnabled";
    public static final String ZOOM_BUTTONS_KEY = "isZoomButtonsEnabled";

    public MapPreferencesControls() {
        super(new HashMap<String, Object>(){{
            put(COMPASS_BUTTON_KEY, Boolean.TRUE);
            put(INDOOR_LEVEL_PICKER_KEY, Boolean.FALSE);
            put(MAP_TOOLBAR_KEY, Boolean.FALSE);
            put(MY_LOCATION_BUTTON_KEY, Boolean.TRUE);
            put(ZOOM_BUTTONS_KEY, Boolean.FALSE);
        }});
    }
}
