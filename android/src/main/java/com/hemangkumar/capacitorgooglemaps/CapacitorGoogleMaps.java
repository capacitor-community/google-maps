package com.hemangkumar.capacitorgooglemaps;


import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import com.hemangkumar.capacitorgooglemaps.model.CustomMarker;
import com.hemangkumar.capacitorgooglemaps.utility.Events;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@CapacitorPlugin(
        name = "CapacitorGoogleMaps",
        permissions = {
                @Permission(
                        strings = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION },
                        alias = "geolocation"
                ),
                @Permission(strings = { Manifest.permission.INTERNET }, alias = "internet"),
        }
)
public class CapacitorGoogleMaps extends Plugin implements CustomMapViewEvents {

    private String GOOGLE_MAPS_KEY;

    private final HashMap<String, CustomMapView> customMapViews = new HashMap<>();
    Float devicePixelRatio;

    private String lastEventChainId;
    public List<MotionEvent> previousEvents = new ArrayList<>();
    private String delegateTouchEventsToMapId;




    /**
     *  This method should be called after we requested the WebView through notifyListeners("didRequestElementFromPoint").
     *  It should tell us if the exact point that was being touched, is from an element in which a MapView exists.
     *  Otherwise it is a 'normal' HTML element, and we should thus not delegate touch events.
     */
    @PluginMethod()
    public void elementFromPointResult(PluginCall call) {
        String eventChainId = call.getString("eventChainId");
        if (eventChainId != null && eventChainId.equals(lastEventChainId)) {
            Boolean isSameNode = call.getBoolean("isSameNode", false);
            if (isSameNode != null && isSameNode) {
                // The WebView apparently has decide the touched point belongs to a certain MapView.
                // Now we should find out which one exactly.
                String mapId = call.getString("mapId");
                if (mapId != null) {
                    delegateTouchEventsToMapId = mapId;
                }
            }
        }
        call.resolve();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void load() {
        super.load();

        this.getBridge().getWebView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int touchType = event.getActionMasked();

                if (touchType == MotionEvent.ACTION_DOWN) {
                    // Throw away all previous state when starting a new touch gesture.
                    // The framework may have dropped the up or cancel event for the previous gesture
                    // due to an app switch, ANR, or some other state change.
                    delegateTouchEventsToMapId = null;
                    previousEvents.clear();

                    // Initialize JSObjects for resolve().
                    JSObject result = new JSObject();
                    JSObject point = new JSObject();

                    // Generate a UUID, and assign it to lastId.
                    // This way we can make sure we are always referencing the last chain of events.
                    lastEventChainId = UUID.randomUUID().toString();
                    // Then add it to result object, so the WebView can reference the correct events when needed.
                    result.put("eventChainId", lastEventChainId);

                    // Get the touched position.
                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    // Since pixels on a webpage are calculated differently, should convert them first.
                    // Convert it to 'real' pixels in WebView by using devicePixelRatio.
                    if (devicePixelRatio != null && devicePixelRatio > 0) {
                        point.put("x", x / devicePixelRatio);
                        point.put("y", y / devicePixelRatio);

                        // Then add it to result object.
                        result.put("point", point);
                    }

                    // Then notify the listener that we request to let the WebView determine
                    // if the element touched is the same node as where some MapView is attached to.
                    notifyListeners("didRequestElementFromPoint", result);
                }

                if (delegateTouchEventsToMapId != null) {
                    CustomMapView customMapView = customMapViews.get(delegateTouchEventsToMapId);
                    if (customMapView != null && !customMapView.isHidden()) {
                        // Apparently, all touch events should be delegated to a specific MapView.

                        // If previous events exist, we should execute those first
                        if (!previousEvents.isEmpty()) {
                            for (int i = 0; i < previousEvents.size(); i++) {
                                MotionEvent e = previousEvents.get(i);
                                if (e != null) {
                                    // Delegate this previous event to the MapView.
                                    dispatchTouchEvent(e, customMapView);
                                }
                            }
                            // Since we delegated all previous events, we can now forget about them.
                            previousEvents.clear();
                        }

                        // Finally delegate the current event to the MapView.
                        dispatchTouchEvent(event, customMapView);
                    }
                } else {
                    // If delegateTouchEventsToMapId is not set, but it could still be set later!
                    // So we should save all past events.
                    // That way we can still execute them later on.
                    // It is important that we use MotionEvent.obtain() to copy the event first.
                    // Otherwise the event does not work properly when delegating it later on.
                    MotionEvent clonedEvent = MotionEvent.obtain(event);
                    previousEvents.add(clonedEvent);
                }

                return false;
            }
        });
    }

    /**
     * dispatching event on selected MapView
     */
    private void dispatchTouchEvent(MotionEvent event, CustomMapView customMapView) {
        Rect offsetViewBounds = new Rect();
        // returns the visible bounds
        customMapView.mapView.getDrawingRect(offsetViewBounds);
        // calculates the relative coordinates to the parent
        ViewGroup parentViewGroup = (ViewGroup) bridge.getWebView().getParent();
        parentViewGroup.offsetDescendantRectToMyCoords(customMapView.mapView, offsetViewBounds);

        int relativeTop = offsetViewBounds.top;
        int relativeLeft = offsetViewBounds.left;

        // Set location with offset points,
        // because if a map is positioned with a different top and left point than the WebView,
        // that should be accounted for.
        event.setLocation(event.getX() - relativeLeft, event.getY() - relativeTop);

        customMapView.mapView.dispatchTouchEvent(event);
    }

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
        this.GOOGLE_MAPS_KEY = call.getString("key");


        devicePixelRatio = call.getFloat("devicePixelRatio");
        call.resolve();
    }

    @PluginMethod()
    public void createMap(final PluginCall call) {
        final CapacitorGoogleMaps ctx = this;

        bridge.saveCall(call);
        final String callbackId = call.getCallbackId();

        final BoundingRect boundingRect = new BoundingRect();
        boundingRect.updateFromJSObject(call.getObject("boundingRect"));

        final MapCameraPosition mapCameraPosition = new MapCameraPosition();
        mapCameraPosition.updateFromJSObject(call.getObject("cameraPosition"), null);

        final MapPreferences mapPreferences = new MapPreferences();
        mapPreferences.updateFromJSObject(call.getObject("preferences"));


        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = new CustomMapView(getBridge().getContext(), getBridge().getActivity(),  ctx);

                customMapViews.put(customMapView.getId(), customMapView);

                customMapView.createMap(callbackId, boundingRect, mapCameraPosition, mapPreferences);

                customMapView.addToView(((ViewGroup) bridge.getWebView().getParent()));

                // Bring the WebView in front of the MapView
                // This allows us to overlay the MapView in HTML/CSS
                bridge.getWebView().bringToFront();

                // Hide the background
                bridge.getWebView().setBackgroundColor(Color.TRANSPARENT);
                bridge.getWebView().loadUrl("javascript:document.documentElement.style.backgroundColor = 'transparent';void(0);");
            }
        });
    }

    @PluginMethod()
    public void updateMap(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    final MapPreferences mapPreferences = customMapView.mapPreferences;
                    mapPreferences.updateFromJSObject(call.getObject("preferences"));

                    JSObject result = customMapView.invalidateMap();

                    call.resolve(result);
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod()
    public void getMap(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    JSObject result = customMapView.getMap();

                    call.resolve(result);
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void removeMap(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    customMapView.removeFromView(((ViewGroup) bridge.getWebView().getParent()));
                    customMapViews.remove(mapId);
                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void clearMap(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    customMapView.clear();
                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod()
    public void close(PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);
                if (customMapView != null){
                    View viewToRemove = customMapViews.get(mapId).mapView;
                    if (viewToRemove != null){
                        ((ViewGroup) getBridge().getWebView().getParent()).removeViewInLayout(viewToRemove);
                        call.resolve();
                    }
                }
                else {
                    call.reject("map not found");
                }

            }
        });
    }

    @PluginMethod()
    public void hide(PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);
                if (customMapView != null){
                    View viewToHide = customMapViews.get(mapId).mapView;
                    if (viewToHide != null){
                        viewToHide.setVisibility(View.INVISIBLE);
                        customMapView.setHidden(true);
                        call.resolve();
                    }
                }
                else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod()
    public void show(PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().executeOnMainThread(new Runnable() {
            CustomMapView customMapView = customMapViews.get(mapId);
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);
                if (customMapView != null){
                    View viewToShow = customMapViews.get(mapId).mapView;
                    if (viewToShow != null){
                        viewToShow.setVisibility(View.VISIBLE);
                        customMapView.setHidden(false);
                        call.resolve();
                    }
                }
                else {
                    call.reject("map not found");
                }
            }
        });
    }


    @PluginMethod
    public void moveCamera(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    final MapCameraPosition mapCameraPosition = customMapView.mapCameraPosition;

                    CameraPosition previousCameraPosition = null;
                    Boolean usePreviousCameraPositionAsBase = call.getBoolean("usePreviousCameraPositionAsBase", false);
                    if (usePreviousCameraPositionAsBase == null || !usePreviousCameraPositionAsBase) {
                        // if we should not use the previous one,
                        // use the current one
                        previousCameraPosition = customMapView.getCameraPosition();
                    }

                    mapCameraPosition.updateFromJSObject(call.getObject("cameraPosition"), previousCameraPosition);

                    Integer duration = call.getInt("duration", 0);

                    // @TODO: add move listeners for movement
                    JSObject result = customMapView.moveCamera(duration);

                    call.resolve(result);
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @Override
    public void onMapReady(String callbackId, JSObject result) {
        PluginCall call = bridge.getSavedCall(callbackId);
        call.resolve(result);
        bridge.releaseCall(call);
    }

    // -- events of tapping
    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapInfoWindow(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_INFO_WINDOW);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didCloseInfoWindow(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_CLOSE_INFO_WINDOW);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMap(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_MAP);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didLongPressMap(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_LONG_PRESS_MAP);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMarker(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didBeginDraggingMarker(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_BEGIN_DRAGGING_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didDragMarker(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_DRAG_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didEndDraggingMarker(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_END_DRAGGING_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMyLocationButton(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_MY_LOCATION_BUTTON);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMyLocationDot(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_MY_LOCATION_DOT);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapPoi(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_POI);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didBeginMovingCamera(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_BEGIN_MOVING_CAMERA);
    }
    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didMoveCamera(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_MOVE_CAMERA);
    }
    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didEndMovingCamera(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_END_MOVING_CAMERA);
    }

    public void setCallbackIdForEvent(final PluginCall call, final String eventName) {
        call.setKeepAlive(true);
        final String callbackId = call.getCallbackId();
        String mapId = call.getString("mapId");

        final CustomMapView customMapView = customMapViews.get(mapId);

        if (customMapView != null) {
            final Boolean preventDefault = call.getBoolean("preventDefault", false);


            getBridge().getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customMapView.setCallbackIdForEvent(callbackId, eventName, preventDefault);
                }
            });
        }
    }


    @Override
    public void resultForCallbackId(String callbackId, JSObject result) {
        PluginCall call = bridge.getSavedCall(callbackId);
        call.resolve(result);
    }

    @PluginMethod()
    public void addMarker(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    JSObject preferences = JSObjectDefaults.getJSObjectSafe(call, "preferences", new JSObject());

                    CustomMarker customMarker = new CustomMarker();
                    customMarker.updateFromJSObject(preferences);

                    customMapView.addMarker(customMarker);

                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod()
    public void addMarkers(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                JSObject markersObj = call.getObject("markers");
                try {
                    JSONArray arrayOfMarkers = markersObj.getJSONArray("arrayOfMarkers");
                    int length = arrayOfMarkers.length();
                    ArrayList<CustomMarker> customMarkers = new ArrayList<>(length);
                    for(int i =0; i<length; i++) {
                        JSObject jsObject = JSObject.fromJSONObject(arrayOfMarkers.getJSONObject(i));
                        JSObject preferences = JSObjectDefaults.getJSObjectSafe(jsObject, "preferences", new JSObject());

                        CustomMarker customMarker = new CustomMarker();
                        customMarker.updateFromJSObject(preferences);

                        customMarkers.add(customMarker);
                    }
                    customMapView.addMarkers(customMarkers);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    call.resolve();
                } else {
                    call.reject("map not found");
                }

            }
        });
    }


    @PluginMethod()
    public void updateMarker(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    final String markerId = call.getString("markerId");
                    JSObject newPreferences = JSObjectDefaults.getJSObjectSafe(call, "preferences" , new JSObject());

                    boolean isUpdated = customMapView.updateMarker(markerId, newPreferences);
                    if (!isUpdated) {
                        call.reject("marker not updated");
                    } else {
                        call.resolve();
                    }
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void removeMarker(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    final String markerId = call.getString("markerId");

                    customMapView.removeMarker(markerId);

                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }




}





