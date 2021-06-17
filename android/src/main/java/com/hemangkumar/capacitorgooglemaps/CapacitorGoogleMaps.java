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

}
