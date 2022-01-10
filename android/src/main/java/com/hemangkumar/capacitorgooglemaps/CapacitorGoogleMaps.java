package com.hemangkumar.capacitorgooglemaps;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.google.android.libraries.maps.CameraUpdateFactory;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.GoogleMapOptions;
import com.google.android.libraries.maps.MapView;
import com.google.android.libraries.maps.OnMapReadyCallback;
import com.google.android.libraries.maps.UiSettings;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;
import com.google.android.libraries.maps.model.CameraPosition;
import com.google.android.libraries.maps.model.CircleOptions;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.MapStyleOptions;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.android.libraries.maps.model.PointOfInterest;
import com.google.android.libraries.maps.model.PolygonOptions;
import com.google.android.libraries.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


@CapacitorPlugin(
        name = "CapacitorGoogleMaps",
        permissions = {
                @Permission(
                        strings = { Manifest.permission.ACCESS_FINE_LOCATION },
                        alias = "geolocation"
                ),
                @Permission(strings = { Manifest.permission.INTERNET }, alias = "internet"),
        }
)
public class CapacitorGoogleMaps extends Plugin implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private MapView mapView;
    GoogleMap googleMap;
    Integer mapViewParentId;
    Integer DEFAULT_WIDTH = 500;
    Integer DEFAULT_HEIGHT = 500;
    Float DEFAULT_ZOOM = 12.0f;
    private HashMap<String, Marker> mHashMap = new HashMap<>();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        notifyListeners("onMapReady", null);
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
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    protected void handleOnStop() {
        super.handleOnStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    protected void handleOnPause() {
        if (mapView != null) {
            mapView.onPause();
        }
        super.handleOnPause();
    }

    @Override
    protected void handleOnDestroy() {
        if (mapView != null) {
            mapView.onDestroy();
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
    public void create(PluginCall call) {
        final Integer width = call.getInt("width", DEFAULT_WIDTH);
        final Integer height = call.getInt("height", DEFAULT_HEIGHT);
        final Integer x = call.getInt("x", 0);
        final Integer y = call.getInt("y", 0);

        final Float zoom = call.getFloat("zoom", DEFAULT_ZOOM);
        final Double latitude = call.getDouble("latitude");
        final Double longitude = call.getDouble("longitude");

        final boolean liteMode = call.getBoolean("enabled", false);

        final CapacitorGoogleMaps ctx = this;
        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng latLng = new LatLng(latitude, longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(zoom)
                        .build();

                GoogleMapOptions googleMapOptions = new GoogleMapOptions();
                googleMapOptions.camera(cameraPosition);
                googleMapOptions.liteMode(liteMode);

                if (mapViewParentId != null){
                    View viewToRemove = ((ViewGroup) getBridge().getWebView().getParent()).findViewById(mapViewParentId);
                    if (viewToRemove != null){
                        ((ViewGroup) getBridge().getWebView().getParent()).removeViewInLayout(viewToRemove);
                    }
                }

                FrameLayout mapViewParent = new FrameLayout(getBridge().getContext());
                mapViewParentId = View.generateViewId();
                mapViewParent.setId(mapViewParentId);

                mapView = new MapView(getBridge().getContext(), googleMapOptions);

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(getScaledPixels(width), getScaledPixels(height));
                lp.topMargin = getScaledPixels(y);
                lp.leftMargin = getScaledPixels(x);

                mapView.setLayoutParams(lp);

                mapViewParent.addView(mapView);

                ((ViewGroup) getBridge().getWebView().getParent()).addView(mapViewParent);

                mapView.onCreate(null);
                mapView.onStart();
                mapView.getMapAsync(ctx);
            }
        });
        call.resolve();
    }

    @PluginMethod()
    public void addMarker(final PluginCall call) {
        final Double latitude = call.getDouble("latitude", 0d);
        final Double longitude = call.getDouble("longitude", 0d);
        final Float opacity = call.getFloat("opacity", 1.0f);
        final String title = call.getString("title", "");
        final String snippet = call.getString("snippet", "");
        final Boolean isFlat = call.getBoolean("isFlat", true);
        final JSObject metadata = call.getObject("metadata");
        final String url = call.getString("iconUrl", "");
        final Boolean draggable = call.getBoolean("draggable", false);

        if (googleMap == null){
            call.reject("Map is not ready");
            return;
        }

        Bitmap imageBitmap = getBitmapFromURL(url);

        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LatLng latLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.alpha(opacity);
                markerOptions.title(title);
                markerOptions.snippet(snippet);
                markerOptions.flat(isFlat);
                markerOptions.draggable(draggable);

                if (imageBitmap != null) {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(imageBitmap));
                }

                Marker marker = googleMap.addMarker(markerOptions);

                // set metadata to marker
                marker.setTag(metadata);

                // get auto-generated id of the just added marker,
                // put this marker into a hashmap with the corresponding id,
                // so we can retrieve the marker by id later on
                mHashMap.put(marker.getId(), marker);

                // initialize JSObject to return when resolving this call
                JSObject result = new JSObject();
                JSObject markerResult = new JSObject();

                // get marker specific values
                markerResult.put("id", marker.getId());
                result.put("marker", markerResult);

                call.resolve(result);
            }
        });
    }

    @PluginMethod()
    public void removeMarker(final PluginCall call) {
        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final String id = call.getString("id", null);

                if (id == null) {
                    // todo
                    return;
                }

                Marker marker = mHashMap.get(id);

                if (marker != null) {
                    marker.remove();
                    mHashMap.remove(id);
                }
            }
        });
    }

    @PluginMethod()
    public void addPolyline(final PluginCall call) {
        final JSArray points = call.getArray("points", new JSArray());

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                PolylineOptions polylineOptions = new PolylineOptions();

                for (int i = 0; i < points.length(); i++) {
                    try {
                        JSONObject point = points.getJSONObject(i);
                        LatLng latLng = new LatLng(point.getDouble("latitude"), point.getDouble("longitude"));
                        polylineOptions.add(latLng);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                googleMap.addPolyline(polylineOptions);

                call.resolve();
            }
        });
    }

    @PluginMethod()
    public void addPolygon(final PluginCall call) {
        final JSArray points = call.getArray("points", new JSArray());

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                PolygonOptions polygonOptions = new PolygonOptions();

                for (int i = 0; i < points.length(); i++) {
                    try {
                        JSONObject point = points.getJSONObject(i);
                        LatLng latLng = new LatLng(point.getDouble("latitude"), point.getDouble("longitude"));
                        polygonOptions.add(latLng);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                googleMap.addPolygon(polygonOptions);
                call.resolve();
            }
        });
    }

    @PluginMethod()
    public void addCircle(final PluginCall call) {
        final int radius = call.getInt("radius", 0);
        final JSONObject center = call.getObject("center", new JSObject());

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.radius(radius);
                try {
                    circleOptions.center(new LatLng(center.getDouble("latitude"), center.getDouble("longitude")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                googleMap.addCircle(circleOptions);

                call.resolve();
            }
        });
    }

    @PluginMethod()
    public void setMapType(PluginCall call) {

        String specifiedMapType = call.getString("type", "normal");
        Integer mapType;

        switch (specifiedMapType) {
            case "hybrid":
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                break;
            case "satellite":
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                break;
            case "terrain":
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;
            case "none":
                mapType = GoogleMap.MAP_TYPE_NONE;
                break;
            default:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
        }

        final Integer selectedMapType = mapType;
        getBridge().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setMapType(selectedMapType);
            }

        });

        call.resolve();
    }

    @PluginMethod()
    public void setIndoorEnabled(PluginCall call) {
        final Boolean indoorEnabled = call.getBoolean("enabled", false);
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setIndoorEnabled(indoorEnabled);
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setTrafficEnabled(PluginCall call) {
        final Boolean trafficEnabled = call.getBoolean("enabled", false);
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setTrafficEnabled(trafficEnabled);
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void padding(PluginCall call) {
        final Integer top = call.getInt("top", 0);
        final Integer left = call.getInt("left", 0);
        final Integer bottom = call.getInt("bottom", 0);
        final Integer right = call.getInt("right", 0);

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setPadding(left, top, right, bottom);
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void clear(PluginCall call) {
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.clear();
                mHashMap.clear();
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void close(PluginCall call) {
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mapViewParentId != null){
                    View viewToRemove = ((ViewGroup) getBridge().getWebView().getParent()).findViewById(mapViewParentId);
                    if (viewToRemove != null){
                        ((ViewGroup) getBridge().getWebView().getParent()).removeViewInLayout(viewToRemove);
                    }
                }
            }
        });
    }

    @PluginMethod()
    public void hide(PluginCall call) {
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mapViewParentId != null){
                    View viewToHide = ((ViewGroup) getBridge().getWebView().getParent()).findViewById(mapViewParentId);
                    if (viewToHide != null){
                        viewToHide.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    @PluginMethod()
    public void show(PluginCall call) {
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                if (mapViewParentId != null){
                    View viewToShow = ((ViewGroup) getBridge().getWebView().getParent()).findViewById(mapViewParentId);
                    if (viewToShow != null){
                        viewToShow.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @PluginMethod()
    public void viewBounds(final PluginCall call) {
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                JSObject result = new JSObject();
                JSObject bounds = new JSObject();
                JSObject farLeft = new JSObject();
                JSObject farRight = new JSObject();
                JSObject nearLeft = new JSObject();
                JSObject nearRight = new JSObject();

                farLeft.put("latitude",googleMap.getProjection().getVisibleRegion().farLeft.latitude);
                farLeft.put("longitude",googleMap.getProjection().getVisibleRegion().farLeft.longitude);
                farRight.put("latitude",googleMap.getProjection().getVisibleRegion().farRight.latitude);
                farRight.put("longitude",googleMap.getProjection().getVisibleRegion().farRight.longitude);
                nearLeft.put("latitude",googleMap.getProjection().getVisibleRegion().nearLeft.latitude);
                nearLeft.put("longitude",googleMap.getProjection().getVisibleRegion().nearLeft.longitude);
                nearRight.put("latitude",googleMap.getProjection().getVisibleRegion().nearRight.latitude);
                nearRight.put("longitude",googleMap.getProjection().getVisibleRegion().nearRight.longitude);

                bounds.put("farLeft",farLeft);
                bounds.put("farRight",farRight);
                bounds.put("nearLeft",nearLeft);
                bounds.put("nearRight",nearRight);
                result.put("bounds",bounds);

                call.resolve(result);
            }
        });
    }

    @PluginMethod()
    public void reverseGeocodeCoordinate(final PluginCall call) {
        final Double latitude = call.getDouble("latitude", 0.0);
        final Double longitude = call.getDouble("longitude", 0.0);

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                /*
                 * TODO: Check if can be done without adding Places SDK
                 *
                 */

                Geocoder geocoder = new Geocoder(getContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 5);

                    JSObject results = new JSObject();

                    int index = 0;
                    for (Address address : addressList) {
                        JSObject addressObject = new JSObject();
                        addressObject.put("administrativeArea", address.getAdminArea());
                        addressObject.put("lines", address.getAddressLine(0));
                        addressObject.put("country", address.getCountryName());
                        addressObject.put("locality", address.getLocality());
                        addressObject.put("subLocality", address.getSubLocality());
                        addressObject.put("thoroughFare", address.getThoroughfare());

                        results.put(String.valueOf(index++), addressObject);
                    }
                    call.resolve(results);
                } catch (IOException e) {
                    call.error("Error in Geocode!");
                }
            }
        });
    }

    @PluginMethod()
    public void settings(final PluginCall call) {

        final boolean allowScrollGesturesDuringRotateOrZoom = call.getBoolean("allowScrollGesturesDuringRotateOrZoom", true);

        final boolean compassButton = call.getBoolean("compassButton", false);
        final boolean zoomButton = call.getBoolean("zoomButton", true);
        final boolean myLocationButton = call.getBoolean("myLocationButton", false);

        boolean consumesGesturesInView = call.getBoolean("consumesGesturesInView", true);
        final boolean indoorPicker = call.getBoolean("indoorPicker", false);

        final boolean rotateGestures = call.getBoolean("rotateGestures", true);
        final boolean scrollGestures = call.getBoolean("scrollGestures", true);
        final boolean tiltGestures = call.getBoolean("tiltGestures", true);
        final boolean zoomGestures = call.getBoolean("zoomGestures", true);

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                UiSettings googleMapUISettings = googleMap.getUiSettings();
                googleMapUISettings.setScrollGesturesEnabledDuringRotateOrZoom(allowScrollGesturesDuringRotateOrZoom);
                googleMapUISettings.setCompassEnabled(compassButton);
                googleMapUISettings.setIndoorLevelPickerEnabled(indoorPicker);
                googleMapUISettings.setMyLocationButtonEnabled(myLocationButton);
                googleMapUISettings.setRotateGesturesEnabled(rotateGestures);
                googleMapUISettings.setScrollGesturesEnabled(scrollGestures);
                googleMapUISettings.setTiltGesturesEnabled(tiltGestures);
                googleMapUISettings.setZoomGesturesEnabled(zoomGestures);
                googleMapUISettings.setZoomControlsEnabled(zoomButton);
                googleMapUISettings.setMyLocationButtonEnabled(true);
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setCamera(PluginCall call) {



        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {

                float viewingAngle = call.getFloat("viewingAngle", googleMap.getCameraPosition().tilt);
                float bearing = call.getFloat("bearing", googleMap.getCameraPosition().bearing);
                Float zoom = call.getFloat("zoom", googleMap.getCameraPosition().zoom);
                Double latitude = call.getDouble("latitude", googleMap.getCameraPosition().target.latitude);
                Double longitude = call.getDouble("longitude", googleMap.getCameraPosition().target.longitude);

                Boolean animate = call.getBoolean("animate", false);
                Double animationDuration = call.getDouble("animationDuration", 1000.0);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude))
                        .zoom(zoom)
                        .tilt(viewingAngle)
                        .bearing(bearing)
                        .build();

                if (animate) {
                    /*
                     * TODO: Implement animationDuration
                     * */
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setMapStyle(PluginCall call) {
        /*
            https://mapstyle.withgoogle.com/
        */
        final String mapStyle = call.getString("jsonString", "");

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                MapStyleOptions mapStyleOptions = new MapStyleOptions(mapStyle);
                googleMap.setMapStyle(mapStyleOptions);
            }
        });
    }

    @PluginMethod()
    public void setOnMyLocationButtonClickListener(PluginCall call) {
        final CapacitorGoogleMaps ctx = this;

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        ctx.onMyLocationButtonClick();
                        return false;
                    }
                });
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setOnMyLocationClickListener(PluginCall call) {
        final CapacitorGoogleMaps ctx = this;
        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                    @Override
                    public void onMyLocationClick(@NonNull Location location) {
                        ctx.onMyLocationClick(location);
                    }
                });
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setOnMarkerClickListener(PluginCall call) {

        final CapacitorGoogleMaps ctx = this;

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        ctx.onMarkerClick(marker);
                        return false;
                    }
                });
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setOnPoiClickListener(PluginCall call) {

        final CapacitorGoogleMaps ctx = this;

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
                    @Override
                    public void onPoiClick(PointOfInterest pointOfInterest) {
                        ctx.onPoiClick(pointOfInterest);
                    }
                });
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setOnMapClickListener(PluginCall call) {

        final CapacitorGoogleMaps ctx = this;

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        ctx.onMapClick(latLng);
                    }
                });
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void setOnMarkerDragListener(PluginCall call) {

        final CapacitorGoogleMaps ctx = this;

        getBridge().executeOnMainThread(new Runnable() {
            @Override
            public void run() {
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        ctx.onMarkerDragStart(marker);
                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {
                        // not implemented
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        ctx.onMarkerDragEnd(marker);
                    }
                });
            }
        });

        call.resolve();
    }

    @PluginMethod()
    public void enableCurrentLocation(final PluginCall call) {

        final boolean enableLocation = call.getBoolean("enabled", false);
        final CapacitorGoogleMaps context = this;
        getBridge().executeOnMainThread(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    call.error("Permission for location not granted");
                } else {
                    googleMap.setMyLocationEnabled(enableLocation);
                    googleMap.setOnMyLocationClickListener(context);
                    googleMap.setOnMyLocationButtonClickListener(context);
                    call.resolve();
                }
            }
        });
    }

    public void onPoiClick(PointOfInterest pointOfInterest) {
        JSObject result = new JSObject();
        JSObject location = new JSObject();
        JSObject coordinates = new JSObject();

        coordinates.put("latitude", pointOfInterest.latLng.latitude);
        coordinates.put("longitude", pointOfInterest.latLng.longitude);

        location.put("coordinates", coordinates);

        result.put("name", pointOfInterest.name);
        result.put("placeID", pointOfInterest.placeId);
        result.put("result", location);

        notifyListeners("didTapPOIWithPlaceID", result);
    }

    public void onMapClick(LatLng latLng) {
        JSObject result = new JSObject();
        JSObject location = new JSObject();
        JSObject coordinates = new JSObject();

        coordinates.put("latitude", latLng.latitude);
        coordinates.put("longitude", latLng.longitude);

        location.put("coordinates", coordinates);

        result.put("result", location);

        notifyListeners("didTapAt", result);
    }

    public void onMarkerClick(Marker marker) {
        JSObject result = new JSObject();
        JSObject location = new JSObject();
        JSObject coordinates = new JSObject();
        JSObject metadata = (JSObject) marker.getTag();

        coordinates.put("latitude", marker.getPosition().latitude);
        coordinates.put("longitude", marker.getPosition().longitude);

        location.put("coordinates", coordinates);

        result.put("id", marker.getId());
        result.put("title", marker.getTitle());
        result.put("snippet", marker.getSnippet());
        result.put("result", location);
        result.put("metadata", metadata);

        notifyListeners("didTap", result);
    }

    public void onMarkerDragStart(Marker marker) {
        JSObject result = new JSObject();
        JSObject location = new JSObject();
        JSObject coordinates = new JSObject();
        JSObject metadata = (JSObject) marker.getTag();

        coordinates.put("latitude", marker.getPosition().latitude);
        coordinates.put("longitude", marker.getPosition().longitude);

        location.put("coordinates", coordinates);

        result.put("id", marker.getId());
        result.put("title", marker.getTitle());
        result.put("snippet", marker.getSnippet());
        result.put("result", location);
        result.put("metadata", metadata);

        notifyListeners("didBeginDragging", result);
    }

    public void onMarkerDragEnd(Marker marker) {
        JSObject result = new JSObject();
        JSObject location = new JSObject();
        JSObject coordinates = new JSObject();
        JSObject metadata = (JSObject) marker.getTag();

        coordinates.put("latitude", marker.getPosition().latitude);
        coordinates.put("longitude", marker.getPosition().longitude);

        location.put("coordinates", coordinates);

        result.put("id", marker.getId());
        result.put("title", marker.getTitle());
        result.put("snippet", marker.getSnippet());
        result.put("result", location);
        result.put("metadata", metadata);

        notifyListeners("didEndDragging", result);
    }

    public boolean onMyLocationButtonClick() {
        /*
         *  TODO: Add handler
         */
        return false;
    }

    public void onMyLocationClick(Location location) {
        JSObject result = new JSObject();

        result.put("latitude", location.getLatitude());
        result.put("longitude", location.getLongitude());

        notifyListeners("onMyLocationClick", result);
    }

    public int getScaledPixels(float pixels) {
        // Get the screen's density scale
        final float scale = getBridge().getActivity().getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return Bitmap.createScaledBitmap(bitmap, getScaledPixels(bitmap.getWidth()), this.getScaledPixels(bitmap.getHeight()), true);
        } catch (IOException e) {
            return null;
        }
    }
}
