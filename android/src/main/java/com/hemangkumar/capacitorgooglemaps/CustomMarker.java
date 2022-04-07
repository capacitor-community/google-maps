package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.getcapacitor.JSObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.UUID;

public class CustomMarker {
    // generate id for the just added marker,
    // put this marker into a hashmap with the corresponding id,
    // so we can retrieve the marker by id later on
    public String markerId = UUID.randomUUID().toString();

    private JSObject tag = new JSObject();
    private JSObject iconDescriptor;
    private LatLng latLng;
    private String title;
    private String snippet;
    private float opacity;
    private boolean isFlat;
    private boolean isDraggable;
    private float zIndex;
    private float anchorX, anchorY;
    private BitmapDescriptor bitmapDescriptor;

    public void updateFromJSObject(JSObject marker) {
        final JSObject position = JSObjectDefaults.getJSObjectSafe(marker, "position", new JSObject());
        final Double latitude = JSObjectDefaults.getDoubleSafe(position, "latitude", 0d);
        final Double longitude = JSObjectDefaults.getDoubleSafe(position, "longitude", 0d);
        latLng = new LatLng(latitude, longitude);

        final JSObject preferences = JSObjectDefaults.getJSObjectSafe(marker, "preferences", new JSObject());
        title = preferences.getString("title", "");
        snippet = preferences.getString("snippet", "");
        opacity = JSObjectDefaults.getFloatSafe(preferences, "opacity", 1f);
        isFlat = JSObjectDefaults.getBooleanSafe(preferences, "isFlat", false);
        isDraggable = JSObjectDefaults.getBooleanSafe(preferences, "isDraggable", false);
        zIndex = JSObjectDefaults.getIntegerSafe(preferences, "zIndex", 0);

        final JSObject anchor = JSObjectDefaults.getJSObjectSafe(preferences, "anchor", new JSObject());
        anchorX = JSObjectDefaults.getFloatSafe(anchor, "x", 0.5f);
        anchorY = JSObjectDefaults.getFloatSafe(anchor, "y", 1f);

        iconDescriptor = JSObjectDefaults.getJSObjectSafe(marker, "icon", JSObjectDefaults.EMPTY);

        this.setMetadata(JSObjectDefaults.getJSObjectSafe(preferences, "metadata", new JSObject()));
    }

    public void updateMarkerOptions(@NonNull MarkerOptions markerOptions) {
        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        markerOptions.alpha(opacity);
        markerOptions.flat(isFlat);
        markerOptions.draggable(isDraggable);
        markerOptions.zIndex(zIndex);
        markerOptions.anchor(anchorX, anchorY);
        markerOptions.icon(bitmapDescriptor);
    }

    public interface OnIconLoaded {
        void run();
    }

    public BitmapDescriptor getBitmapDescriptor() {
        return bitmapDescriptor;
    }

    public void asyncLoadIcon(
            @NonNull FragmentActivity activity,
            @Nullable OnIconLoaded onLoaded) {
        new AsyncIconLoader(iconDescriptor, activity)
                .load((bitmap) -> {
                    if (bitmap != null) {
                        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                    } else {
                        bitmapDescriptor = null;
                    }
                    if (onLoaded != null) {
                        onLoaded.run();
                    }
                });
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public float getOpacity() {
        return opacity;
    }

    public boolean isFlat() {
        return isFlat;
    }

    public float getZIndex() {
        return zIndex;
    }

    public LatLng getPosition() {
        return latLng;
    }

    public Object getTag() {
        return tag;
    }

    public boolean isDraggable() {
        return isDraggable;
    }

    public JSObject getIconDescriptor() {
        return iconDescriptor;
    }

    public Marker addToMap(GoogleMap googleMap, MarkerOptions markerOptions) {
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(this.tag);
        return marker;
    }

    private void setMetadata(@NonNull JSObject jsObject) {
        JSObject tag = new JSObject();
        // set id to tag
        tag.put("markerId", this.markerId);
        // set anchor to tag (because it cannot be retrieved from a marker instance)
        JSObject anchorResult = new JSObject();
        anchorResult.put("x", anchorX);
        anchorResult.put("y", anchorY);
        tag.put("anchor", anchorResult);
        // then set metadata to tag
        tag.put("metadata", jsObject);
        // save in tag variable
        this.tag = tag;
    }

    public static JSObject getResultForMarker(Marker marker, String mapId) {
        return getResultForMarker(new ResultFor(marker), mapId);
    }

    public static JSObject getResultForMarker(CustomMarker marker, String mapId) {
        return getResultForMarker(new ResultFor(marker), mapId);
    }

    private static JSObject getResultForMarker(ResultFor r, String mapId) {
        JSObject tag = r.getTag();

        // initialize JSObjects to return
        JSObject result = new JSObject();
        JSObject markerResult = new JSObject();
        JSObject positionResult = new JSObject();
        JSObject preferencesResult = new JSObject();

        result.put("marker", markerResult);
        markerResult.put("position", positionResult);
        markerResult.put("preferences", preferencesResult);

        // get map id
        markerResult.put("mapId", mapId);

        // get id
        String markerId = tag.optString("markerId", r.getMarkerId());
        markerResult.put("markerId", markerId);

        // get position values
        positionResult.put("latitude", r.getPosition().latitude);
        positionResult.put("longitude", r.getPosition().longitude);

        // get preferences
        preferencesResult.put("title", r.getTitle());
        preferencesResult.put("snippet", r.getSnippet());
        preferencesResult.put("opacity", r.getOpacity());
        preferencesResult.put("isFlat", r.isFlat());
        preferencesResult.put("isDraggable", r.isDraggable());
        preferencesResult.put("zIndex", r.getZIndex());
        // anchor values
        JSObject anchorResult = JSObjectDefaults.getJSObjectSafe(tag, "anchor", new JSObject());
        preferencesResult.put("anchor", anchorResult);
        // metadata
        JSObject metadata = JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
        preferencesResult.put("metadata", metadata);

        return result;
    }
}
