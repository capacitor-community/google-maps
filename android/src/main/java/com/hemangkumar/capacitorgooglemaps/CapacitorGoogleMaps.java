package com.hemangkumar.capacitorgooglemaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@CapacitorPlugin(
        name = "CapacitorGoogleMaps",
        permissions = {
                @Permission(
                        strings = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        alias = "location"
                ),
        }
)
public class CapacitorGoogleMaps extends Plugin implements CustomMapViewEvents {
    private final Map<String, CustomMapView> customMapViews = new ConcurrentHashMap<>();
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
        mapCameraPosition.updateFromJSObject(call.getObject("cameraPosition"), null);

        final MapPreferences mapPreferences = new MapPreferences();
        mapPreferences.updateFromJSObject(call.getObject("preferences"));

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = new CustomMapView(getBridge().getActivity(), ctx);

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

    @PluginMethod
    public void moveCamera(final PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    final MapCameraPosition mapCameraPosition = customMapView.mapCameraPosition;

                    CameraPosition currentCameraPosition = null;

                    Boolean useCurrentCameraPositionAsBase = call.getBoolean("useCurrentCameraPositionAsBase", true);

                    if (useCurrentCameraPositionAsBase != null && useCurrentCameraPositionAsBase) {
                        currentCameraPosition = customMapView.getCameraPosition();
                    }

                    mapCameraPosition.updateFromJSObject(call.getObject("cameraPosition"), currentCameraPosition);

                    Integer duration = call.getInt("duration", 0);

                    customMapView.moveCamera(duration);

                    call.resolve();
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
    public void didTapInfoWindow(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_INFO_WINDOW);
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
    public void didLongPressMap(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_LONG_PRESS_MAP);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMarker(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapPolygon(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_POLYGON);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapPolyline(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_POLYLINE);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didBeginDraggingMarker(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_BEGIN_DRAGGING_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didDragMarker(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_DRAG_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didEndDraggingMarker(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_END_DRAGGING_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMyLocationButton(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_MY_LOCATION_BUTTON);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMyLocationDot(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_MY_LOCATION_DOT);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapPoi(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_TAP_POI);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didBeginMovingCamera(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_BEGIN_MOVING_CAMERA);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didMoveCamera(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_MOVE_CAMERA);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didEndMovingCamera(final PluginCall call) {
        setCallbackIdForEvent(call, CustomMapView.EVENT_DID_END_MOVING_CAMERA);
    }

    public void setCallbackIdForEvent(final PluginCall call, final String eventName) {
        call.setKeepAlive(true);
        final String callbackId = call.getCallbackId();
        String mapId = call.getString("mapId");

        final CustomMapView customMapView = customMapViews.get(mapId);

        if (customMapView != null) {
            final Boolean preventDefault = call.getBoolean("preventDefault", false);

            getBridge().getActivity().runOnUiThread(
                    () -> customMapView.setCallbackIdForEvent(callbackId, eventName, preventDefault));
        }
    }

    @Override
    public void resultForCallbackId(String callbackId, JSObject result) {
        PluginCall call = bridge.getSavedCall(callbackId);
        call.resolve(result);
    }

    @PluginMethod()
    public void addMarker(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            CustomMarker customMarker = new CustomMarker();
            customMarker.updateFromJSObject(call.getData());
            customMapView.addMarker(
                    customMarker,
                    (Marker marker) -> {
                        call.resolve(CustomMarker.getResultForMarker(marker, customMapView.getId()));
                    }
            );
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void removeMarker(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            final String markerId = call.getString("markerId", "");
            if (customMapView.removeMarker(markerId)) {
                call.resolve();
            } else {
                call.reject("marker is not found when remove");
            }
        });
    }

    @PluginMethod()
    public void addMarkers(final PluginCall call) {
        final String mapId = call.getString("mapId");
        CustomMapView customMapView = customMapViews.get(mapId);
        if (customMapView == null) {
            call.reject("map not found");
            return;
        }
        try {
            final JSArray jsMarkers = call.getArray("markers", new JSArray());
            final List<CustomMarker> customMarkers = createCustomMarkers(jsMarkers);
            addCustomMarkers(customMarkers, mapId, customMapView, call);
        } catch (RuntimeException e) {
            call.reject("exception in addMarkers", e);
        }
    }

    private List<CustomMarker> createCustomMarkers(final JSArray jsMarkers) {
        final int n = jsMarkers.length();
        final List<CustomMarker> customMarkers = new ArrayList<>(n);
        final Object syncRoot = new Object();
        final AtomicInteger nMarkersCounter = new AtomicInteger(0);
        // prepare customMarkers as fast as possible. Really it doesn't increase the total
        // speed of this method :( noticeably.
        final ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < n; i++) {
            final int fi = i;
            if (executorService.isShutdown()) {
                throw new RuntimeException("exception in addMarkers");
            }
            executorService.execute(() -> {
                try {
                    JSONObject jsonObject = (JSONObject) jsMarkers.get(fi);
                    JSObject jsObject = JSObject.fromJSONObject(jsonObject);
                    CustomMarker customMarker = new CustomMarker();
                    customMarker.updateFromJSObject(jsObject);
                    synchronized (customMarkers) {
                        customMarkers.add(customMarker);
                        if (nMarkersCounter.addAndGet(1) == n) {
                            synchronized (syncRoot) {
                                syncRoot.notify();
                            }
                        }
                    }
                } catch (JSONException ignored) {
                    executorService.shutdown();
                }
            });
        }

        synchronized (syncRoot) {
            try {
                // Wait for customMarkers are populated
                // I follow https://www.baeldung.com/java-wait-notify#1-why-enclose-wait-in-a-while-loop
                do {
                    syncRoot.wait();
                } while (nMarkersCounter.get() < n);
            } catch (InterruptedException e) {
                throw new RuntimeException("exception in createCustomMarkers", e);
            }
        }
        return customMarkers;
    }

    private void addCustomMarkers(final List<CustomMarker> customMarkers,
                                  String mapId,
                                  final CustomMapView customMapView,
                                  final PluginCall call) {
        final int n = customMarkers.size();
        final List<JSObject> result = new ArrayList<>(n);
        final Object syncRoot = new Object();
        final AtomicInteger nMarkersAdded = new AtomicInteger(0);
        final AtomicBoolean isMarkerAdded = new AtomicBoolean(false);
        final ExecutorService executorService = Executors.newFixedThreadPool(1);


        if (executorService.isShutdown()) {
            throw new RuntimeException("exception in addCustomMarkers");
        }

        executorService.execute(() -> {
            for (CustomMarker customMarker : customMarkers) {
                getBridge().getActivity().runOnUiThread(() -> {
                    customMapView.addMarker(
                            customMarker,
                            (Marker marker) -> {
                                result.add(
                                        (JSObject) CustomMarker.getResultForMarker(marker, mapId)
                                                .opt("marker")
                                );
                                synchronized (syncRoot) {
                                    isMarkerAdded.set(true);
                                    syncRoot.notify();
                                }
                                if (nMarkersAdded.addAndGet(1) == n) {
                                    JSObject jsResult = new JSObject();
                                    jsResult.put("mapId", mapId);
                                    JSArray jsMarkerOutputEntries = JSArray.from(result.toArray());
                                    jsResult.put("markers", jsMarkerOutputEntries);
                                    call.resolve(jsResult);
                                }
                            }
                    );
                }); // end of runOnUiThread
                synchronized (syncRoot) {
                    try {
                        // wait for Marker is rendered before the next iteration
                        // here is a background thread -> No UI freeze
                        // I don not follow https://www.baeldung.com/java-wait-notify#1-why-enclose-wait-in-a-while-loop
                        // because it is not absolutely necessary here.
                        if (!isMarkerAdded.get()) {
                            syncRoot.wait();
                            isMarkerAdded.set(false);
                        }
                    } catch (InterruptedException ignored) {
                        //executorService.shutdown();
                        break;
                    }
                }
            } // end for
        }); // end of execute
    }

    @PluginMethod()
    public void addCluster(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            final String mapId = customMapView.getId();
            JSArray jsMarkers = call.getArray("markers");
            Collection<CustomMarker> customMarkers = prepareCustomMarkersForCluster(mapId, jsMarkers);

            JSObject jsResult = prepareResultForCluster(mapId, customMarkers);

            JSObject jsClusterIcon = call.getObject("clusterIcon");
            IconDescriptor iconDescriptor = jsClusterIcon != null ?
                    new IconDescriptor(jsClusterIcon) :
                    null;

            JSObject jsClusterCaptionPrefs = call.getObject("clusterCaptionPrefs");
            CaptionPreferences captionPreferences = jsClusterCaptionPrefs != null ?
                    new CaptionPreferences(jsClusterCaptionPrefs) :
                    null;

            customMapView.addCluster(customMarkers, iconDescriptor, captionPreferences,
                    () -> {
                        call.resolve(jsResult);
                    });
        });
    }

    private Collection<CustomMarker> prepareCustomMarkersForCluster(String mapId, JSArray jsMarkers) {
        final int n = jsMarkers.length();
        Collection<CustomMarker> customMarkers = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            JSObject jsObject = JSObjectDefaults.getJSObjectByIndex(jsMarkers, i);
            CustomMarker customMarker = new CustomMarker();
            customMarker.updateFromJSObject(jsObject);
            customMarkers.add(customMarker);
        }
        return customMarkers;
    }

    private JSObject prepareResultForCluster(String mapId, Collection<CustomMarker> customMarkers) {
        final JSArray jsArrResult = new JSArray();
        for (CustomMarker customMarker : customMarkers) {
            jsArrResult.put(CustomMarker.getResultForMarker(customMarker, mapId));
        }
        JSObject jsResult = new JSObject();
        jsResult.put("result", jsArrResult);
        return jsResult;
    }

    @PluginMethod()
    public void addPolygon(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            CustomPolygon customPolygon = new CustomPolygon();
            customPolygon.updateFromJSObject(call.getData());
            customMapView.addPolygon(customPolygon, (shapePolygon) -> {
                call.resolve(customPolygon.getResultFor(shapePolygon, customMapView.getId()));
            });
        });
    }

    @PluginMethod()
    public void getPolygon(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            String polygonId = call.getString("id", "");
            ShapePolygon polygon = customMapView.getPolygon(polygonId);
            if (polygon != null) {
                CustomPolygon customPolygon = new CustomPolygon();
                call.resolve(customPolygon.getResultFor(polygon, customMapView.getId()));
            } else {
                call.reject("polygon not found when get");
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void updatePolygon(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            String id = call.getString("id", "");
            CustomPolygon customPolygon = new CustomPolygon();
            customPolygon.updateFromJSObject(call.getData());
            if (customMapView.updatePolygon(id, customPolygon)) {
                call.resolve();
            } else {
                call.reject("polygon is not found when update");
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void removePolygon(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            final String id = call.getString("id");
            if (customMapView.removePolygon(id)) {
                call.resolve();
            } else {
                call.reject("polygon is not found when remove");
            }
        });
    }

    @PluginMethod()
    public void addPolyline(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            CustomPolyline customPolyline = new CustomPolyline();
            customPolyline.updateFromJSObject(call.getData());
            ShapePolyline polyline = customMapView.addPolyline(customPolyline);
            call.resolve(customPolyline.getResultFor(polyline, customMapView.getId()));
        });
    }

    @PluginMethod()
    public void getPolyline(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            String polylineId = call.getString("id", "");
            ShapePolyline id = customMapView.getPolyline(polylineId);
            if (id != null) {
                CustomPolyline customPolyline = new CustomPolyline();
                call.resolve(customPolyline.getResultFor(id, customMapView.getId()));
            } else {
                call.reject("polyline is not found when get");
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void updatePolyline(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            String id = call.getString("id", "");
            CustomPolyline customPolyline = new CustomPolyline();
            customPolyline.updateFromJSObject(call.getData());
            if (customMapView.updatePolyline(id, customPolyline)) {
                call.resolve();
            } else {
                call.reject("polyline is not found when update");
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void removePolyline(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            final String id = call.getString("id");
            if (customMapView.removePolyline(id)) {
                call.resolve();
            } else {
                call.reject("polyline is not found when remove");
            }
        });
    }

    @PluginMethod()
    public void addCircle(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            CustomCircle customCircle = new CustomCircle();
            customCircle.updateFromJSObject(call.getData());
            ShapeCircle circle = customMapView.addCircle(customCircle);
            call.resolve(customCircle.getResultFor(circle, customMapView.getId()));
        });
    }

    @PluginMethod()
    public void getCircle(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            String circleId = call.getString("id", "");
            ShapeCircle circle = customMapView.getCircle(circleId);
            if (circle != null) {
                CustomCircle customCircle = new CustomCircle();
                call.resolve(customCircle.getResultFor(circle, customMapView.getId()));
            } else {
                call.reject("circle not found when get");
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void updateCircle(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            String circleId = call.getString("id", "");
            CustomCircle customCircle = new CustomCircle();
            customCircle.updateFromJSObject(call.getData());
            if (customMapView.updateCircle(circleId, customCircle)) {
                call.resolve();
            } else {
                call.reject("circle is not found when update");
            }
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void removeCircle(final PluginCall call) {
        callMapViewMethodInUiThread(call, (customMapView) -> {
            final String id = call.getString("id");
            if (customMapView.removeCircle(id)) {
                call.resolve();
            } else {
                call.reject("circle is not found when remove");
            }
        });
    }

    private <T> void callMapViewMethodInUiThread(final PluginCall call,
                                                 final Consumer<CustomMapView> consumer) {
        final String mapId = call.getString("mapId");
        getBridge().getActivity().runOnUiThread(() -> {
            CustomMapView customMapView = customMapViews.get(mapId);
            if (customMapView != null) {
                consumer.accept(customMapView);
            } else {
                call.reject("map not found");
            }
        });
    }
}
