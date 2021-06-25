package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMapOptions;

public class MapPreferences {
    public MapPreferencesGestures gestures;
    public MapPreferencesControls controls;
    public MapPreferencesAppearance appearance;

    public MapPreferences() {
        this.gestures = new MapPreferencesGestures();
        this.controls = new MapPreferencesControls();
        this.appearance = new MapPreferencesAppearance();
    }

    public void updateFromJSObject(@Nullable JSObject preferences) {
        if (preferences != null) {
            // update gestures
            JSObject gesturesObject = preferences.getJSObject("gestures");
            this.gestures.updateFromJSObject(gesturesObject);
            // update controls
            JSObject controlsObject = preferences.getJSObject("controls");
            this.controls.updateFromJSObject(controlsObject);
            // update appearance
            JSObject appearanceObject = preferences.getJSObject("appearance");
            this.appearance.updateFromJSObject(appearanceObject);
        }
    }

    public GoogleMapOptions generateGoogleMapOptions() {
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();

        // set gestures
        if (this.gestures != null) {
            googleMapOptions.rotateGesturesEnabled(gestures.isRotateAllowed);
            googleMapOptions.scrollGesturesEnabled(gestures.isScrollAllowed);
            googleMapOptions.scrollGesturesEnabledDuringRotateOrZoom(gestures.isScrollAllowedDuringRotateOrZoom);
            googleMapOptions.tiltGesturesEnabled(gestures.isTiltAllowed);
            googleMapOptions.zoomGesturesEnabled(gestures.isZoomAllowed);
        }

        // set controls
        if (this.controls != null) {
            googleMapOptions.compassEnabled(controls.isCompassButtonEnabled);
            googleMapOptions.mapToolbarEnabled(controls.isMapToolbarEnabled);
            googleMapOptions.zoomControlsEnabled(controls.isZoomButtonsEnabled);

            // controls.isIndoorLevelPickerEnabled can only be set through `UiSettings`
            // controls.isMyLocationButtonEnabled can only be set through `UiSettings`
        }

        // set appearance
        if (this.appearance != null) {
            // set mapType
            googleMapOptions.mapType(this.appearance.type);

            // appearance.isIndoorShown can only be set through `GoogleMap`
            // appearance.isBuildingsShown can only be set through `GoogleMap`
            // appearance.isMyLocationDotShown can only be set through `GoogleMap`
            // appearance.isTrafficShown can only be set through `GoogleMap`
        }

        return googleMapOptions;
    }
}
