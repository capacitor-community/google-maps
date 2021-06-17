package com.hemangkumar.capacitorgooglemaps;

import android.view.ViewGroup;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.libraries.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CapacitorPlugin
public class CapacitorGoogleMaps extends Plugin implements CustomMapViewEvents  {
    private HashMap<String, CustomMapView> customMapViews = new HashMap<>();

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        for (CustomMapView customMapView : customMapViews.values()) {
            if (customMapView != null) {
                customMapView.handleOnStart();
            }
        }
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        for (CustomMapView customMapView : customMapViews.values()) {
            if (customMapView != null) {
                customMapView.handleOnResume();
            }
        }
    }

    @Override
    protected void handleOnPause() {
        for (CustomMapView customMapView : customMapViews.values()) {
            if (customMapView != null) {
                customMapView.handleOnPause();
            }
        }
        super.handleOnPause();
    }

    @Override
    protected void handleOnStop() {
        super.handleOnStop();
        for (CustomMapView customMapView : customMapViews.values()) {
            if (customMapView != null) {
                customMapView.handleOnStop();
            }
        }
    }

    @Override
    protected void handleOnDestroy() {
        for (CustomMapView customMapView : customMapViews.values()) {
            if (customMapView != null) {
                customMapView.handleOnDestroy();
            }
        }
        super.handleOnDestroy();
    }

    @PluginMethod()
    public void initialize(PluginCall call) {
        /*
         *  TODO: Check API key
         */
        call.resolve();
    }

    @PluginMethod()
    public void create(final PluginCall call) {
        Integer DEFAULT_WIDTH = 500;
        Integer DEFAULT_HEIGHT = 500;
        Float DEFAULT_ZOOM = 12.0f;

        final CapacitorGoogleMaps ctx = this;

        bridge.saveCall(call);
        final String callbackId = call.getCallbackId();

        final Double latitude = call.getDouble("latitude");
        final Double longitude = call.getDouble("longitude");
        final Float zoom = call.getFloat("zoom", DEFAULT_ZOOM);
        final Boolean liteMode = call.getBoolean("enabled", false);
        final Integer width = call.getInt("width", DEFAULT_WIDTH);
        final Integer height = call.getInt("height", DEFAULT_HEIGHT);
        final Integer x = call.getInt("x", 0);
        final Integer y = call.getInt("y", 0);

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = new CustomMapView(getBridge().getContext(), ctx);

                customMapViews.put(customMapView.getId(), customMapView);

                customMapView.createMap(callbackId, latitude, longitude, zoom, liteMode, width, height, x, y);

                customMapView.addToView(((ViewGroup) bridge.getWebView().getParent()));
            }
        });
    }

    @Override
    public void onMapReady(String callbackId, JSObject result) {
        PluginCall call = bridge.getSavedCall(callbackId);
        call.resolve(result);
        bridge.releaseCall(call);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMarker(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMyLocationDot(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_MY_LOCATION_DOT);
    }

    public void setCallbackIdForEvent(final PluginCall call, String eventName) {
        call.setKeepAlive(true);
        String callbackId = call.getCallbackId();
        String mapId = call.getString("mapId");

        CustomMapView customMapView = customMapViews.get(mapId);

        if (customMapView != null) {
            customMapView.setCallbackIdForEvent(callbackId, eventName);
        }
    }

    public void resultForCallbackId(String callbackId, JSObject result) {
        PluginCall call = bridge.getSavedCall(callbackId);
        call.resolve(result);
    }

    @PluginMethod()
    public void addMarker(final PluginCall call) {
        final String mapId = call.getString("mapId");

        final Double latitude = call.getDouble("latitude", 0d);
        final Double longitude = call.getDouble("longitude", 0d);
        final String title = call.getString("title", "");
        final String snippet = call.getString("snippet", "");
        final Float opacity = call.getFloat("opacity", 1.0f);
        final Boolean isFlat = call.getBoolean("isFlat", true);
        final Boolean isDraggable = call.getBoolean("isDraggable", false);
        final JSObject metadata = call.getObject("metadata");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    JSObject result = customMapView.addMarker(latitude, longitude, title, snippet, opacity, isFlat, isDraggable, metadata);

                    call.resolve(result);
                } else {
                    call.reject("map not found");
                }
            }
        });
    }
}
