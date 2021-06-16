package com.hemangkumar.capacitorgooglemaps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.getcapacitor.Bridge;
import com.getcapacitor.PluginCall;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.GoogleMapOptions;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;

public class CustomMapView implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {
    private final Context context;
    MapView mapView;
    GoogleMap googleMap;
    Integer DEFAULT_WIDTH = 500;
    Integer DEFAULT_HEIGHT = 500;
    Float DEFAULT_ZOOM = 12.0f;

    PluginCall savedCallForCreate = null;

    public CustomMapView(@NonNull Context ctx) {
        context = ctx;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (savedCallForCreate != null) {
            savedCallForCreate.resolve();
            savedCallForCreate = null;
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // todo
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    protected void handleOnStart() {
        if (mapView != null) {
            mapView.onStart();
        }
    }

    protected void handleOnResume() {
        if (mapView != null) {
            mapView.onResume();
        }
    }

    protected void handleOnPause() {
        if (mapView != null) {
            mapView.onPause();
        }
    }

    protected void handleOnStop() {
        if (mapView != null) {
            mapView.onStop();
        }
    }

    protected void handleOnDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    protected MapView create(PluginCall call) {
        savedCallForCreate = call;

        final Double latitude = call.getDouble("latitude");
        final Double longitude = call.getDouble("longitude");
        final Float zoom = call.getFloat("zoom", DEFAULT_ZOOM);

        final boolean liteMode = call.getBoolean("enabled", false);

        final Integer width = call.getInt("width", DEFAULT_WIDTH);
        final Integer height = call.getInt("height", DEFAULT_HEIGHT);
        final Integer x = call.getInt("x", 0);
        final Integer y = call.getInt("y", 0);

        return createMap(latitude, longitude, zoom, liteMode, width, height, x, y);
    }

    private MapView createMap(Double latitude, Double longitude, float zoom, boolean liteMode, Integer width, Integer height, Integer x, Integer y) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build();

        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.camera(cameraPosition);
        googleMapOptions.liteMode(liteMode);

        mapView = new MapView(context, googleMapOptions);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getScaledPixels(width), getScaledPixels(height));
        lp.topMargin = getScaledPixels(y);
        lp.leftMargin = getScaledPixels(x);

        mapView.setLayoutParams(lp);

        mapView.onCreate(null);
        mapView.onStart();
        mapView.getMapAsync(this);

        return mapView;
    }

    private int getScaledPixels(float pixels) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void addToView(ViewGroup parent) {
        FrameLayout mapViewParent = new FrameLayout(context);
        int mapViewParentId = View.generateViewId();
        mapViewParent.setId(mapViewParentId);

        mapViewParent.addView(mapView);

        parent.addView(mapViewParent);
    }
}
