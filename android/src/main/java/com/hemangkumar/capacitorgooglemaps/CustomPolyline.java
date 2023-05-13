package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.util.WebColor;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Polyline;
import com.google.android.libraries.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomPolyline  {
    // generate id for the just added polyline,
    // put this polyline into a hashmap with the corresponding id,
    // so we can retrieve the polyline by id later on
    public final String polylineId = UUID.randomUUID().toString();

    private final PolylineOptions polylineOptions = new PolylineOptions();
    protected JSObject tag = new JSObject();

    public void updateFromJSObject(JSObject polyline) {
        this.setPath(polyline.optJSONArray("path"));

        final JSObject preferences = JSObjectDefaults.getJSObjectSafe(polyline, "preferences", new JSObject());

        this.setBasicFields(preferences);
        this.setMetadata(JSObjectDefaults.getJSObjectSafe(preferences, "metadata", new JSObject()));
    }

    public void addToMap(GoogleMap googleMap, @Nullable Consumer<Polyline> consumer) {
        final Polyline polyline = googleMap.addPolyline(polylineOptions);
        polyline.setTag(tag);

        if (consumer != null) {
            consumer.accept(polyline);
        }
    }

    private void setPath(@Nullable JSONArray path) {
        if (path != null) {
            List<LatLng> latLngList = getLatLngList(path);
            this.polylineOptions.addAll(latLngList);
        }
    }

    private static List<LatLng> getLatLngList(@NonNull JSONArray latLngArray) {
        List<LatLng> latLngList = new ArrayList<>();

        for (int n = 0; n < latLngArray.length(); n++) {
            JSONObject latLngObject = latLngArray.optJSONObject(n);
            if (latLngObject != null) {
                LatLng latLng = getLatLng(latLngObject);
                latLngList.add(latLng);
            }
        }

        return latLngList;
    }

    private static LatLng getLatLng(@NonNull JSONObject latLngObject) {
        double latitude = latLngObject.optDouble("latitude", 0d);
        double longitude = latLngObject.optDouble("longitude", 0d);
        return new LatLng(latitude, longitude);
    }

    private void setBasicFields(@NonNull JSObject preferences) {
        final float width = (float) preferences.optDouble("width", 10);
        final int color = WebColor.parseColor(preferences.optString("color", "#000000"));
        final float zIndex = (float) preferences.optDouble("zIndex", 0);
        final boolean isVisible = preferences.optBoolean("isVisible", true);
        final boolean isGeodesic = preferences.optBoolean("isGeodesic", false);
        final boolean isClickable = preferences.optBoolean("isClickable", false);

        polylineOptions.color(color);
        polylineOptions.width(width);
        polylineOptions.zIndex(zIndex);
        polylineOptions.visible(isVisible);
        polylineOptions.geodesic(isGeodesic);
        polylineOptions.clickable(isClickable);
    }

    private void setMetadata(@NonNull JSObject jsObject) {
        JSObject tag = new JSObject();
        tag.put("id", this.polylineId);
        tag.put("metadata", jsObject);
        this.tag = tag;
    }

    public JSObject getResultForPolyline(Polyline polyline, String mapId) {
        JSObject tag = null;

        try {
            tag = (JSObject) polyline.getTag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tag = tag != null ? tag : new JSObject();
        }

        // initialize JSObjects to return
        JSObject result = new JSObject();
        JSObject polylineResult = new JSObject();
        JSObject preferencesResult = new JSObject();

        result.put("polyline", polylineResult);
        polylineResult.put("preferences", preferencesResult);

        // get map id
        polylineResult.put("mapId", mapId);

        // get id
        String polylineId = tag.optString("polylineId", polyline.getId());
        polylineResult.put("polylineId", polylineId);

        // get path values
        JSArray path = latLngsToJSArray(polyline.getPoints());
        polylineResult.put("path", path);

        // get preferences
        preferencesResult.put("width", polyline.getWidth());
        preferencesResult.put("strokeColor", colorToString(polyline.getColor()));
        preferencesResult.put("zIndex", polyline.getZIndex());
        preferencesResult.put("isVisible", polyline.isVisible());
        preferencesResult.put("isGeodesic", polyline.isGeodesic());
        preferencesResult.put("isClickable", polyline.isClickable());

        JSObject metadata = JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
        preferencesResult.put("metadata", metadata);

        return result;
    }

    private static JSArray latLngsToJSArray(Collection<LatLng> positions) {
        JSArray jsPositions = new JSArray();
        for (LatLng pos : positions) {
            JSObject jsPos = latLngToJSObject(pos);
            jsPositions.put(jsPos);
        }
        return jsPositions;
    }

    private static JSObject latLngToJSObject(LatLng latLng) {
        JSObject jsPos = new JSObject();
        jsPos.put("latitude", latLng.latitude);
        jsPos.put("longitude", latLng.longitude);
        return jsPos;
    }

    private static String colorToString(int color) {
        int r = ((color >> 16) & 0xff);
        int g = ((color >> 8) & 0xff);
        int b = ((color) & 0xff);
        int a = ((color >> 24) & 0xff);
        if (a != 255) {
            return String.format("#%02X%02X%02X%02X", a, r, g, b);
        } else {
            return String.format("#%02X%02X%02X", r, g, b);
        }
    }

}
