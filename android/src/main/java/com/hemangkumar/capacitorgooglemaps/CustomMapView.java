package com.hemangkumar.capacitorgooglemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.JsonReader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.GoogleMapOptions;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.UiSettings;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
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

    public MapCameraPosition mapCameraPosition;
    public MapPreferences mapPreferences;

    public CustomMapView(@NonNull Context context, CustomMapViewEvents customMapViewEvents) {
        this.context = context;
        this.customMapViewEvents = customMapViewEvents;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // populate `googleMap` variable for other methods to use
        this.googleMap = googleMap;

        // set controls
        UiSettings googleMapUISettings = this.googleMap.getUiSettings();
        googleMapUISettings.setIndoorLevelPickerEnabled(this.mapPreferences.controls.isIndoorLevelPickerEnabled);
        googleMapUISettings.setMyLocationButtonEnabled(this.mapPreferences.controls.isMyLocationButtonEnabled);

        // set appearance
        this.googleMap.setBuildingsEnabled(this.mapPreferences.appearance.isBuildingsShown);
        this.googleMap.setIndoorEnabled(this.mapPreferences.appearance.isIndoorShown);
        if (hasPermission()) {
            this.googleMap.setMyLocationEnabled(this.mapPreferences.appearance.isMyLocationDotShown);
        }
        this.googleMap.setTrafficEnabled(this.mapPreferences.appearance.isTrafficShown);

        // set listeners
        this.googleMap.setOnInfoWindowCloseListener(this);
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMyLocationClickListener(this);
        this.googleMap.setOnMyLocationButtonClickListener(this);

        // execute callback
        if (customMapViewEvents != null && savedCallbackIdForCreate != null) {
            JSObject result = getResultForMap();
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

    public void createMap(String callbackId, BoundingRect boundingRect, MapCameraPosition mapCameraPosition, MapPreferences mapPreferences) {
        savedCallbackIdForCreate = callbackId;

        this.mapCameraPosition = mapCameraPosition;
        this.mapPreferences = mapPreferences;

        GoogleMapOptions googleMapOptions = this.mapPreferences.generateGoogleMapOptions();
        googleMapOptions.camera(this.mapCameraPosition.cameraPosition);

        mapView = new MapView(context, googleMapOptions);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getScaledPixels(boundingRect.width), getScaledPixels(boundingRect.height));
        lp.topMargin = getScaledPixels(boundingRect.y);
        lp.leftMargin = getScaledPixels(boundingRect.x);

        mapView.setLayoutParams(lp);

        mapView.onCreate(null);
        mapView.onStart();
        mapView.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    public JSObject invalidateMap() {
        if (this.googleMap == null) {
            return null;
        }

        UiSettings googleMapUISettings = this.googleMap.getUiSettings();

        // set gestures
        googleMapUISettings.setRotateGesturesEnabled(this.mapPreferences.gestures.isRotateAllowed);
        googleMapUISettings.setScrollGesturesEnabled(this.mapPreferences.gestures.isScrollAllowed);
        googleMapUISettings.setScrollGesturesEnabledDuringRotateOrZoom(this.mapPreferences.gestures.isScrollAllowedDuringRotateOrZoom);
        googleMapUISettings.setTiltGesturesEnabled(this.mapPreferences.gestures.isTiltAllowed);
        googleMapUISettings.setZoomGesturesEnabled(this.mapPreferences.gestures.isZoomAllowed);

        // set controls
        googleMapUISettings.setCompassEnabled(this.mapPreferences.controls.isCompassButtonEnabled);
        googleMapUISettings.setIndoorLevelPickerEnabled(this.mapPreferences.controls.isIndoorLevelPickerEnabled);
        googleMapUISettings.setMapToolbarEnabled(this.mapPreferences.controls.isMapToolbarEnabled);
        googleMapUISettings.setMyLocationButtonEnabled(this.mapPreferences.controls.isMyLocationButtonEnabled);
        googleMapUISettings.setZoomControlsEnabled(this.mapPreferences.controls.isZoomButtonsEnabled);

        // set appearance
        this.googleMap.setBuildingsEnabled(this.mapPreferences.appearance.isBuildingsShown);
        this.googleMap.setIndoorEnabled(this.mapPreferences.appearance.isIndoorShown);
        if (hasPermission()) {
            this.googleMap.setMyLocationEnabled(this.mapPreferences.appearance.isMyLocationDotShown);
        }
        this.googleMap.setTrafficEnabled(this.mapPreferences.appearance.isTrafficShown);

        return getResultForMap();
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

        // generate id for the just added marker,
        // put this marker into a hashmap with the corresponding id,
        // so we can retrieve the marker by id later on
        String markerId = UUID.randomUUID().toString();
        mHashMap.put(markerId, marker);

        JSObject tag = new JSObject();
        // set id to tag
        tag.put("markerId", markerId);
        // then set metadata to tag
        tag.put("metadata", metadata);

        // finally set tag to marker
        marker.setTag(tag);

        return getResultForMarker(marker);
    }

    private JSObject getResultForMap() {
        if (this.mapView != null && this.googleMap != null) {
            JSObject result = new JSObject();
            JSObject resultGoogleMap = new JSObject();
            result.put("googleMap", resultGoogleMap);

            resultGoogleMap.put("mapId", id);

            // @TODO: add cameraPosition etc. to result as well.

            return result;
        }
        return null;
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
        markerResult.put("markerId", marker.getId());
        markerResult.put("title", marker.getTitle());
        markerResult.put("snippet", marker.getSnippet());
        markerResult.put("opacity", marker.getAlpha());
        markerResult.put("isFlat", marker.isFlat());
        markerResult.put("isDraggable", marker.isDraggable());
        markerResult.put("metadata", new JSObject());

        JSObject tag = (JSObject) marker.getTag();
        if (tag != null) {
            // get and set markerId to marker
            String markerId = tag.getString("markerId");
            markerResult.put("markerId", markerId);

            // get and set metadata to marker
            try {
                JSObject metadata = tag.getJSObject("metadata", new JSObject());
                markerResult.put("metadata", metadata);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // return result
        result.put("position", positionResult);
        result.put("marker", markerResult);

        return result;
    }
}
