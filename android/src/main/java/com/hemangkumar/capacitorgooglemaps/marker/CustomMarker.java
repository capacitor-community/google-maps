package com.hemangkumar.capacitorgooglemaps.marker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getcapacitor.JSObject;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.hemangkumar.capacitorgooglemaps.JSObjectDefaults;

import org.json.JSONException;

import java.util.Objects;
import java.util.UUID;

public class CustomMarker implements ClusterItem {
    // generate id for the just added marker,
    // put this marker into a hashmap with the corresponding id,
    // so we can retrieve the marker by id later on
    private String id = UUID.randomUUID().toString();

    private final MarkerOptions markerOptions = new MarkerOptions();
    private JSObject tag = new JSObject();

    private int markerCategoryId;

    public void updateFromJSObject(JSObject preferences) {
        final JSObject position = JSObjectDefaults.getJSObjectSafe(preferences, "position", new JSObject());
        final Double latitude = JSObjectDefaults.getDoubleSafe(position, "latitude", 0d);
        final Double longitude = JSObjectDefaults.getDoubleSafe(position, "longitude", 0d);
        LatLng latLng = new LatLng(latitude, longitude);

        final String title = preferences.getString("title", "");
        final String snippet = preferences.getString("snippet", "");
        final Float opacity = JSObjectDefaults.getFloatSafe(preferences, "opacity", 1f);
        final Boolean isFlat = JSObjectDefaults.getBooleanSafe(preferences,"isFlat", false);
        final Boolean isDraggable = JSObjectDefaults.getBooleanSafe(preferences,"isDraggable", false);
        final Integer markerCategoryId = JSObjectDefaults.getIntegerSafe(preferences, "category", 0);

        this.setMetadata(JSObjectDefaults.getJSObjectSafe(preferences, "metadata", new JSObject()));

        this.markerOptions.position(latLng);
        this.markerOptions.title(title);
        this.markerOptions.snippet(snippet);
        this.markerOptions.alpha(opacity);
        this.markerOptions.flat(isFlat);
        this.markerOptions.draggable(isDraggable);

        this.markerCategoryId = markerCategoryId;

        this.markerOptions.zIndex(21);
    }


    private void setMetadata(@NonNull JSObject jsObject) {
        JSObject tag = new JSObject();
        // set id to tag
        tag.put("id", this.id);
        // then set metadata to tag
        tag.put("metadata", jsObject);
        // save in tag variable
        this.tag = tag;
    }

    public static JSObject getResultForMarker(Marker marker, String mapId) {
        // initialize JSObjects to return
        JSObject result = new JSObject();
        JSObject positionResult = new JSObject();
        JSObject markerResult = new JSObject();

        // get map id
        positionResult.put("mapId", mapId);

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
        markerResult.put("metadata", new JSObject());

        JSObject tag = (JSObject) marker.getTag();
        if (tag != null) {
            // get and set markerId to marker
            String markerId = tag.getString("id");
            markerResult.put("id", markerId);

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

    public static JSObject getResultForMarker(CustomMarker marker, String mapId) {

        // initialize JSObjects to return
        JSObject result = new JSObject();
        JSObject positionResult = new JSObject();
        JSObject markerResult = new JSObject();

        // get map id
        positionResult.put("mapId", mapId);

        // get position values
        positionResult.put("latitude", marker.getPosition().latitude);
        positionResult.put("longitude", marker.getPosition().longitude);

        // get marker specific values
        markerResult.put("id", marker.getId());
        markerResult.put("title", marker.getTitle());
        markerResult.put("snippet", marker.getSnippet());
        markerResult.put("opacity", marker.markerOptions.getAlpha());
        markerResult.put("isFlat", marker.markerOptions.isFlat());
        markerResult.put("isDraggable", marker.markerOptions.isDraggable());
        markerResult.put("metadata", new JSObject());

        JSObject tag = (JSObject) marker.tag;
        if (tag != null) {
            // get and set markerId to marker
            String markerId = tag.getString("id");
            markerResult.put("id", markerId);

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

    public String getId() {
        return id;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return this.markerOptions.getPosition();
    }

    @Nullable
    @Override
    public String getTitle() {
        return this.markerOptions.getTitle();
    }

    @Nullable
    @Override
    public String getSnippet() {
        return this.markerOptions.getSnippet();
    }

    public MarkerCategory getMarkerCategory() {
        return MarkerCategory.getMarkerCategoryById(this.markerCategoryId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomMarker that = (CustomMarker) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
