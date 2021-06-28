package com.hemangkumar.capacitorgooglemaps;

import java.util.HashMap;

public class MapPreferencesGestures extends JSObjectDefaults {
    public static final String ROTATE_ALLOWED_KEY = "isRotateAllowed";
    public static final String SCROLL_ALLOWED_KEY = "isScrollAllowed";
    public static final String SCROLL_ALLOWED_DURING_ROTATE_OR_ZOOM_KEY = "isScrollAllowedDuringRotateOrZoom";
    public static final String TILT_ALLOWED_KEY = "isTiltAllowed";
    public static final String ZOOM_ALLOWED_KEY = "isZoomAllowed";

    public MapPreferencesGestures() {
        super(new HashMap<String, Object>(){{
            put(ROTATE_ALLOWED_KEY, Boolean.TRUE);
            put(SCROLL_ALLOWED_KEY, Boolean.TRUE);
            put(SCROLL_ALLOWED_DURING_ROTATE_OR_ZOOM_KEY, Boolean.TRUE);
            put(TILT_ALLOWED_KEY, Boolean.TRUE);
            put(ZOOM_ALLOWED_KEY, Boolean.TRUE);
        }});
    }
}
