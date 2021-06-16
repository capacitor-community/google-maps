package com.hemangkumar.capacitorgooglemaps;

import android.util.Log;
import android.view.ViewGroup;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.ArrayList;
import java.util.List;

@CapacitorPlugin
public class CapacitorGoogleMaps extends Plugin {
    private final List<CustomMapView> customMapViews = new ArrayList<>();

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        for (CustomMapView customMapView : customMapViews) {
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
        for (CustomMapView customMapView : customMapViews) {
            if (customMapView != null) {
                customMapView.handleOnResume();
            }
        }
    }

    @Override
    protected void handleOnPause() {
        for (CustomMapView customMapView : customMapViews) {
            if (customMapView != null) {
                customMapView.handleOnPause();
            }
        }
        super.handleOnPause();
    }

    @Override
    protected void handleOnStop() {
        super.handleOnStop();
        for (CustomMapView customMapView : customMapViews) {
            if (customMapView != null) {
                customMapView.handleOnStop();
            }
        }
    }

    @Override
    protected void handleOnDestroy() {
        for (CustomMapView customMapView : customMapViews) {
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
        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = new CustomMapView(getBridge().getContext());

                customMapViews.add(customMapView);

                customMapView.create(call);

                customMapView.addToView(((ViewGroup) bridge.getWebView().getParent()));
            }
        });
    }

//    @PluginMethod()
//    public void addMarker(final PluginCall call) {
//        final Double latitude = call.getDouble("latitude", 0d);
//        final Double longitude = call.getDouble("longitude", 0d);
//        final Float opacity = call.getFloat("opacity", 1.0f);
//        final String title = call.getString("title", "");
//        final String snippet = call.getString("snippet", "");
//        final Boolean isFlat = call.getBoolean("isFlat", true);
//        final JSObject metadata = call.getObject("metadata");
//
//        getBridge().getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                LatLng latLng = new LatLng(latitude, longitude);
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(latLng);
//                markerOptions.alpha(opacity);
//                markerOptions.title(title);
//                markerOptions.snippet(snippet);
//                markerOptions.flat(isFlat);
//
//                Marker marker = googleMap.addMarker(markerOptions);
//
//                // set metadata to marker
//                marker.setTag(metadata);
//
//                // get auto-generated id of the just added marker,
//                // put this marker into a hashmap with the corresponding id,
//                // so we can retrieve the marker by id later on
//                mHashMap.put(marker.getId(), marker);
//
//                // initialize JSObject to return when resolving this call
//                JSObject result = new JSObject();
//                JSObject markerResult = new JSObject();
//
//                // get marker specific values
//                markerResult.put("id", marker.getId());
//                result.put("marker", markerResult);
//
//                call.resolve(result);
//            }
//        });
//    }
}
