package com.hemangkumar.capacitorgooglemaps;

import android.content.Context;
import android.location.Location;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.GoogleMapOptions;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.UUID;

public class CustomMapView implements OnMapReadyCallback, GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {
    private final Context context;
    private final CustomMapViewEvents customMapViewEvents;

    private final String id;

    MapView mapView;
    GoogleMap googleMap;

    private HashMap<String, Marker> mHashMap = new HashMap<>();

    String savedCallbackIdForCreate;

    String savedCallbackIdForDidCloseInfoWindow;

    String savedCallbackIdForDidTapMap;

    String savedCallbackIdForDidTapMarker;
    Boolean preventDefaultForDidTapMarker = false;

    String savedCallbackIdForDidTapMyLocationDot;

    public static final String EVENT_DID_CLOSE_INFO_WINDOW = "didCloseInfoWindow";
    public static final String EVENT_DID_TAP_MAP = "didTapMap";
    public static final String EVENT_DID_TAP_MARKER = "didTapMarker";
    public static final String EVENT_DID_TAP_MY_LOCATION_DOT = "didTapMyLocationDot";

    public CustomMapView(@NonNull Context context, CustomMapViewEvents customMapViewEvents) {
        this.context = context;
        this.customMapViewEvents = customMapViewEvents;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // populate `googleMap` variable for other methods to use
        this.googleMap = googleMap;

        // set listeners
        this.googleMap.setOnInfoWindowCloseListener(this);
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMyLocationClickListener(this);
        this.googleMap.setOnMyLocationButtonClickListener(this);

        // execute callback
        if (customMapViewEvents != null && savedCallbackIdForCreate != null) {
            JSObject result = new JSObject();
            result.put("mapId", id);
            customMapViewEvents.onMapReady(savedCallbackIdForCreate, result);
        }
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        if (customMapViewEvents != null && savedCallbackIdForDidCloseInfoWindow != null) {
            JSObject result = getResultForMarker(marker);
            customMapViewEvents.resultForCallbackId(savedCallbackIdForDidCloseInfoWindow, result);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (customMapViewEvents != null && savedCallbackIdForDidTapMap != null) {
            // initialize JSObjects to return
            JSObject result = new JSObject();
            JSObject positionResult = new JSObject();

            // get position values
            positionResult.put("latitude", latLng.latitude);
            positionResult.put("longitude", latLng.longitude);

            // return result
            result.put("position", positionResult);

            customMapViewEvents.resultForCallbackId(savedCallbackIdForDidTapMap, result);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (customMapViewEvents != null && savedCallbackIdForDidTapMarker != null) {
            JSObject result = getResultForMarker(marker);
            customMapViewEvents.resultForCallbackId(savedCallbackIdForDidTapMarker, result);
        }
        return preventDefaultForDidTapMarker;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        if (customMapViewEvents != null && savedCallbackIdForDidTapMyLocationDot != null) {
            JSObject result = new JSObject(); // todo
            customMapViewEvents.resultForCallbackId(savedCallbackIdForDidTapMyLocationDot, result);
        }
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

    public void setCallbackIdForEvent(String callbackId, String eventName, Boolean preventDefault) {
        if (callbackId != null && eventName != null) {
            if (eventName.equals(CustomMapView.EVENT_DID_CLOSE_INFO_WINDOW)) {
                savedCallbackIdForDidCloseInfoWindow = callbackId;
            } else if (eventName.equals(CustomMapView.EVENT_DID_TAP_MAP)) {
                savedCallbackIdForDidTapMap = callbackId;
            } else if (eventName.equals(CustomMapView.EVENT_DID_TAP_MARKER)) {
                savedCallbackIdForDidTapMarker = callbackId;
                if (preventDefault == null) {
                    preventDefault = false;
                }
                preventDefaultForDidTapMarker = preventDefault;
            } else if (eventName.equals(CustomMapView.EVENT_DID_TAP_MY_LOCATION_DOT)) {
                savedCallbackIdForDidTapMyLocationDot = callbackId;
            }
        }
    }

    public void createMap(String callbackId, Double latitude, Double longitude, float zoom, boolean liteMode, Integer width, Integer height, Integer x, Integer y) {
        savedCallbackIdForCreate = callbackId;

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
    }

    private int getScaledPixels(float pixels) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void addToView(ViewGroup parent) {
        parent.addView(mapView);
    }

    public JSObject addMarker(Double latitude, Double longitude, String title, String snippet, Float opacity, Boolean isFlat, Boolean isDraggable, JSObject metadata) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        markerOptions.alpha(opacity);
        markerOptions.flat(isFlat);
        markerOptions.draggable(isDraggable);

        Marker marker = googleMap.addMarker(markerOptions);

        // set metadata to marker
        marker.setTag(metadata);

        // get auto-generated id of the just added marker,
        // put this marker into a hashmap with the corresponding id,
        // so we can retrieve the marker by id later on
        mHashMap.put(marker.getId(), marker);

        return getResultForMarker(marker);
    }

    private JSObject getResultForMarker(Marker marker) {
        // initialize JSObjects to return
        JSObject result = new JSObject();
        JSObject positionResult = new JSObject();
        JSObject markerResult = new JSObject();

        // get position values
        positionResult.put("latitude", marker.getPosition().latitude);
        positionResult.put("longitude", marker.getPosition().longitude);

        // get marker specific values
        markerResult.put("id", marker.getId());
        markerResult.put("title", marker.getTitle());
        markerResult.put("snippet", marker.getSnippet());
        markerResult.put("opacity", marker.getAlpha());
        markerResult.put("isFlat", marker.isFlat());
        markerResult.put("isDraggable", marker.isDraggable());
        markerResult.put("metadata", marker.getTag());

        // return result
        result.put("position", positionResult);
        result.put("marker", markerResult);

        return result;
    }
}
