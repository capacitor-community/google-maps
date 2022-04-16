package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentActivity;

import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.BitmapDescriptor;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;

import java.util.UUID;

public class CustomMarker {
    private static class ResultAdapter {
        private JSObject tag;
        private final String markerId;
        private final MarkerOptions markerOptions;

        public ResultAdapter(Marker marker) {
            try {
                tag = (JSObject) marker.getTag();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tag = tag != null ? tag : new JSObject();
            }
            markerId = marker.getId();
            markerOptions = new MarkerOptions();
            markerOptions.position(marker.getPosition());
            markerOptions.title(marker.getTitle());
            markerOptions.snippet(marker.getSnippet());
            markerOptions.alpha(marker.getAlpha());
            markerOptions.flat(marker.isFlat());
            markerOptions.zIndex(marker.getZIndex());
            markerOptions.draggable(marker.isDraggable());
        }

        public ResultAdapter(CustomMarker customMarker) {
            try {
                tag = (JSObject) customMarker.getTag();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                tag = tag != null ? tag : new JSObject();
            }
            markerId = customMarker.markerId;
            markerOptions = customMarker.getMarkerOptions();
        }

        public JSObject getTag() {
            return tag;
        }

        public MarkerOptions getMarkerOptions() {
            return markerOptions;
        }

        public String getMarkerId() {
            return markerId;
        }
    }

    // generate id for the just added marker,
    // put this marker into a hashmap with the corresponding id,
    // so we can retrieve the marker by id later on
    public final String markerId = UUID.randomUUID().toString();

    private final MarkerOptions markerOptions = new MarkerOptions();
    private JSObject tag = new JSObject();
    private IconDescriptor iconDescriptor;

    public void updateFromJSObject(JSObject marker) {
        final JSObject position = JSObjectDefaults.getJSObjectSafe(marker, "position", new JSObject());
        final Double latitude = JSObjectDefaults.getDoubleSafe(position, "latitude", 0d);
        final Double longitude = JSObjectDefaults.getDoubleSafe(position, "longitude", 0d);
        LatLng latLng = new LatLng(latitude, longitude);

        final JSObject preferences = JSObjectDefaults.getJSObjectSafe(marker, "preferences", new JSObject());
        final String title = preferences.getString("title", "");
        final String snippet = preferences.getString("snippet", "");
        final Float opacity = JSObjectDefaults.getFloatSafe(preferences, "opacity", 1f);
        final Boolean isFlat = JSObjectDefaults.getBooleanSafe(preferences,"isFlat", false);
        final Boolean isDraggable = JSObjectDefaults.getBooleanSafe(preferences,"isDraggable", false);
        final Integer zIndex = JSObjectDefaults.getIntegerSafe(preferences,"zIndex", 0);

        final JSObject anchor = JSObjectDefaults.getJSObjectSafe(preferences, "anchor", new JSObject());
        final Float anchorX = JSObjectDefaults.getFloatSafe(anchor, "x", 0.5f);
        final Float anchorY = JSObjectDefaults.getFloatSafe(anchor, "y", 1f);

        markerOptions.position(latLng);
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        markerOptions.alpha(opacity);
        markerOptions.flat(isFlat);
        markerOptions.draggable(isDraggable);
        markerOptions.zIndex(zIndex);
        markerOptions.anchor(anchorX, anchorY);

        JSObject jsIconDescriptor = preferences.getJSObject("icon");
        if (jsIconDescriptor != null) {
            iconDescriptor = new IconDescriptor(jsIconDescriptor);
        } else {
            iconDescriptor = null;
        }
        this.setMetadata(JSObjectDefaults.getJSObjectSafe(preferences, "metadata", new JSObject()));
    }

    public void updateMarkerOptions(@NonNull MarkerOptions another) {
        another
                .position(markerOptions.getPosition())
                .title(markerOptions.getTitle())
                .snippet(markerOptions.getSnippet())
                .alpha(markerOptions.getAlpha())
                .flat(markerOptions.isFlat())
                .draggable(markerOptions.isDraggable())
                .zIndex(markerOptions.getZIndex())
                .anchor(markerOptions.getAnchorU(), markerOptions.getAnchorV())
                .icon(markerOptions.getIcon());
    }

    public BitmapDescriptor getBitmapDescriptor() {
        return markerOptions.getIcon();
    }

    public void asyncLoadIcon(
            @NonNull FragmentActivity activity,
            @Nullable Runnable onLoaded) {
        new AsyncIconLoader(iconDescriptor, activity)
                .load((bitmap) -> {
                    if (bitmap != null) {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    } else {
                        markerOptions.icon(null);
                    }
                    if (onLoaded != null) {
                        onLoaded.run();
                    }
                });
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public Object getTag() {
        return tag;
    }

    public void addToMap(FragmentActivity activity, GoogleMap googleMap, @Nullable Consumer<Marker> consumer) {
        asyncLoadIcon(
                activity,
                () -> {
                    Marker marker = googleMap.addMarker(markerOptions);
                    marker.setTag(tag);

                    if (consumer != null) {
                        consumer.accept(marker);
                    }
                });
    }

    private void setMetadata(@NonNull JSObject jsObject) {
        JSObject tag = new JSObject();
        // set id to tag
        tag.put("markerId", this.markerId);
        // set anchor to tag (because it cannot be retrieved from a marker instance)
        JSObject anchorResult = new JSObject();
        anchorResult.put("x", markerOptions.getAnchorU());
        anchorResult.put("y", markerOptions.getAnchorV());
        tag.put("anchor", anchorResult);
        // then set metadata to tag
        tag.put("metadata", jsObject);
        // save in tag variable
        this.tag = tag;
    }

    public static JSObject getResultForMarker(Marker marker, String mapId) {
        return getResultForAdapter(new ResultAdapter(marker), mapId);
    }

    public static JSObject getResultForMarker(CustomMarker marker, String mapId) {
        return getResultForAdapter(new ResultAdapter(marker), mapId);
    }

    private static JSObject getResultForAdapter(ResultAdapter r, String mapId) {
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
        positionResult.put("latitude", r.getMarkerOptions().getPosition().latitude);
        positionResult.put("longitude", r.getMarkerOptions().getPosition().longitude);

        // get preferences
        preferencesResult.put("title", r.getMarkerOptions().getTitle());
        preferencesResult.put("snippet", r.getMarkerOptions().getSnippet());
        preferencesResult.put("opacity", r.getMarkerOptions().getAlpha());
        preferencesResult.put("isFlat", r.getMarkerOptions().isFlat());
        preferencesResult.put("isDraggable", r.getMarkerOptions().isDraggable());
        preferencesResult.put("zIndex", r.getMarkerOptions().getZIndex());
        // anchor values
        JSObject anchorResult = JSObjectDefaults.getJSObjectSafe(tag, "anchor", new JSObject());
        preferencesResult.put("anchor", anchorResult);
        // metadata
        JSObject metadata = JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
        preferencesResult.put("metadata", metadata);

        return result;
    }
}
