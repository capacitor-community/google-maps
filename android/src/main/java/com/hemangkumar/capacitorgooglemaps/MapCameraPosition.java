package com.hemangkumar.capacitorgooglemaps;

import android.util.Log;

import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

public class MapCameraPosition {
    public CameraPosition cameraPosition;

    public MapCameraPosition() {
        this.cameraPosition = CameraPosition.builder().target(new LatLng(0, 0)).build();
    }

    public void updateFromJSObject(@Nullable JSObject cameraPosition, CameraPosition baseCameraPosition) {
        CameraPosition.Builder cameraPositionBuilder;
        if (baseCameraPosition != null) {
            // use given cameraPosition as the base
            cameraPositionBuilder = new CameraPosition.Builder(baseCameraPosition);
        } else {
            // use default cameraPosition as the base
            cameraPositionBuilder = new CameraPosition.Builder();
        }

        LatLng latLng = null;
        if (cameraPosition != null) {
            JSObject target = cameraPosition.getJSObject("target");

            if (target != null) {
                try {
                    if (target.has("latitude") && target.has("longitude")) {
                        double latitude = target.getDouble("latitude");
                        double longitude = target.getDouble("longitude");

                        latLng = new LatLng(latitude, longitude);
                        cameraPositionBuilder.target(latLng);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (cameraPosition.has("bearing")) {
                try {
                    float bearing = (float) cameraPosition.getDouble("bearing");
                    cameraPositionBuilder.bearing(bearing);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (cameraPosition.has("tilt")) {
                try {
                    float tilt = (float) cameraPosition.getDouble("tilt");
                    if (tilt < 0.0F || tilt > 90.0F) {
                        Log.d("GoogleMap", "Tilt needs to be between 0 and 90 inclusive: " + tilt);
                    } else {
                        cameraPositionBuilder.tilt(tilt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (cameraPosition.has("zoom")) {
                try {
                    float zoom = (float) cameraPosition.getDouble("zoom");
                    cameraPositionBuilder.zoom(zoom);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if (this.cameraPosition == null && latLng == null) {
            // at the very least a camera target should be set
            Log.d("GoogleMap", "Target of camera has been set to (0,0) automatically.");
            latLng = new LatLng(0, 0);
            cameraPositionBuilder.target(latLng);
        }

        try {
            this.cameraPosition = cameraPositionBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
