package com.hemangkumar.capacitorgooglemaps;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.CameraPosition;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hemangkumar.capacitorgooglemaps.marker.CustomMarker;
import com.hemangkumar.capacitorgooglemaps.marker.MarkerCategory;
import com.hemangkumar.capacitorgooglemaps.mapsutility.Events;
import com.hemangkumar.capacitorgooglemaps.utils.BoundingRect;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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


    // -- constants --
    private String MARKER_CATEGORY_DIRECTORY = "marker-categories";

    // -- private fields --
    private String GOOGLE_MAPS_KEY;


    public static final HashMap<String, CustomMapView> customMapViews = new HashMap<>();
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

        initMarkerCategories();

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
    public void didTapSingleMarker(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_SINGLE_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapCluster(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_CLUSTER);
    }
    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapClusterInfoWindow(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_CLUSTER_INFO_WINDOW);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapMarker(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_MARKER);
    }

    @PluginMethod(returnType = PluginMethod.RETURN_CALLBACK)
    public void didTapClusterItemInfoWindow(final PluginCall call) {
        setCallbackIdForEvent(call, Events.EVENT_DID_TAP_CLUSTER_ITEM_INFO_WINDOW);
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

                    call.resolve(CustomMarker.getResultForMarker(customMarker, mapId));
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

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void addMarkerCategory(final PluginCall call) {
        final int id = JSObjectDefaults.getIntegerSafe(call.getData(), "id", -1);
        final String title = call.getString("title");
        final String encodedImage = call.getString("base64Data");

        if(id == -1) {
            call.reject("don't have id for category");
        }

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        // if will be added duplicate key id then
        // By definition, the put command replaces
        // the previous value associated with the
        // given key in the map
        new MarkerCategory(id, title, decodedByte);

        call.resolve();
    }

    /**
     * method for fetching marker categories from
     * existing asset folder
     */
    private void initMarkerCategories() {
        // default marker icon for zero category
        new MarkerCategory(0, "default", null);

        // getting map of names of categories and icons of this
        HashMap<String, Bitmap> markerCategoriesNamesAndIcons = fetchMarkersCategoriesFilesFromAssets(getContext());
        List<String> keys = new ArrayList<String>(markerCategoriesNamesAndIcons.keySet());
        Collections.sort(keys);
        int i = 1;
        for (String nameOfCategory :
                keys) {
            new MarkerCategory(i, nameOfCategory, markerCategoriesNamesAndIcons.get(nameOfCategory));
            i++;
        }

    }

    private HashMap<String, Bitmap> fetchMarkersCategoriesFilesFromAssets(Context context) {
        return getImagesFromAsset(MARKER_CATEGORY_DIRECTORY, context);
    }

    // method for reading pictures and their names from folder
    private HashMap<String, Bitmap> getImagesFromAsset(String path, Context context) {
        HashMap<String, Bitmap> stringBitmapHashMap = new HashMap<>();

        AssetManager assetManager = context.getAssets();

        // regex for getting full names of picture
        // (cool.tank.png) -> cool.tank and .png
        // (cool.tank) -> cool.tank and {null}
        String textGroups = "(.+?)(\\.[^.]*$|$)";
        Pattern textPattern = Pattern.compile(textGroups);

        try {
            // get array of names of files
            String[] namesOfFiles = context.getAssets().list(path);
            for (String nameOfFile :
                    namesOfFiles) {

                Matcher textMatcher = textPattern.matcher(nameOfFile);
                while(textMatcher.find()) {
                    File pathToFile = new File(path, nameOfFile);
                    InputStream istr = assetManager.open(pathToFile.getPath());
                    Bitmap bitmap = BitmapFactory.decodeStream(istr);
                    istr.close();
                    stringBitmapHashMap.put(textMatcher.group(1), bitmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBitmapHashMap;
    }


    @PluginMethod
    public void zoomInButtonClick(PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    customMapView.zoomIn();
                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod
    public void zoomOutButtonClick(PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    customMapView.zoomOut();
                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    @PluginMethod
    public void myLocationButtonClick(PluginCall call) {
        final String mapId = call.getString("mapId");

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @SuppressLint("ResourceType")
            @Override
            public void run() {
                CustomMapView customMapView = customMapViews.get(mapId);

                if (customMapView != null) {
                    locationRequest();
                    customMapView.locationButton.callOnClick();
                    call.resolve();
                } else {
                    call.reject("map not found");
                }
            }
        });
    }

    private void locationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());



        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                        getActivity(),
                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }



}





