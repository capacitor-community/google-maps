package com.hemangkumar.capacitorgooglemaps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.libraries.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@CapacitorPlugin
public class CapacitorGoogleMaps extends Plugin implements CustomMapViewEvents  {
    private final HashMap<String, CustomMapView> customMapViews = new HashMap<>();
    Float devicePixelRatio;
    private String lastEventChainId;
    public List<MotionEvent> previousEvents = new ArrayList<>();
    private String delegateTouchEventsToMapId;

    @PluginMethod()
    public void elementFromPointResult(PluginCall call) {
        // This method should be called after we requested the WebView through notifyListeners("didRequestElementFromPoint").
        // It should tell us if the exact point that was being touched, is from an element in which a MapView exists.
        // Otherwise it is a 'normal' HTML element, and we should thus not delegate touch events.
        String eventChainId = call.getString("eventChainId");
        if (eventChainId != null && eventChainId.equals(lastEventChainId)) {
            Boolean isSameNode = call.getBoolean("isSameNode", false);
            if (isSameNode != null && isSameNode) {
                // The WebView apparently has decides the touched point belongs to a certain MapView.
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
                    if (customMapView != null) {
                        // Apparently, all touch events should be delegated to a specific MapView.

                        // If previous events exist, we should execute those first
                        if (previousEvents.size() > 0) {
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
        mapCameraPosition.updateFromJSObject(call.getObject("cameraPosition"));

        final MapPreferences mapPreferences = new MapPreferences();
        mapPreferences.updateFromJSObject(call.getObject("preferences"));

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = new CustomMapView(getBridge().getContext(), ctx);

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

    @PluginMethod
    public void moveCamera(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    final MapCameraPosition mapCameraPosition = customMapView.mapCameraPosition;
                    mapCameraPosition.updateFromJSObject(call.getObject("cameraPosition"));

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

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didCloseInfoWindow(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_CLOSE_INFO_WINDOW);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMap(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_MAP);
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
            Boolean preventDefault = call.getBoolean("preventDefault", false);
            customMapView.setCallbackIdForEvent(callbackId, eventName, preventDefault);
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

        final Double latitude = call.getDouble("latitude", 0d);
        final Double longitude = call.getDouble("longitude", 0d);
        final String title = call.getString("title", "");
        final String snippet = call.getString("snippet", "");
        final Float opacity = call.getFloat("opacity", 1.0f);
        final Boolean isFlat = call.getBoolean("isFlat", false);
        final Boolean isDraggable = call.getBoolean("isDraggable", true);
        final JSObject metadata = call.getObject("metadata", new JSObject());

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
