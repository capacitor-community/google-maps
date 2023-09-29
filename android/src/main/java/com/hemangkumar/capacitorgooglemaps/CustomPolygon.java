package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.util.WebColor;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomPolygon  {
    // generate id for the just added polygon,
    // put this polygon into a hashmap with the corresponding id,
    // so we can retrieve the polygon by id later on
    public final String polygonId = UUID.randomUUID().toString();

    private final PolygonOptions polygonOptions = new PolygonOptions();
    protected JSObject tag = new JSObject();

    public void updateFromJSObject(JSObject polygon) {
        this.setPath(polygon.optJSONArray("path"));

        final JSObject preferences = JSObjectDefaults.getJSObjectSafe(polygon, "preferences", new JSObject());

        this.setBasicFields(preferences);
        this.setHoles(preferences.optJSONArray("holes"));
        this.setMetadata(JSObjectDefaults.getJSObjectSafe(preferences, "metadata", new JSObject()));
    }

    public void addToMap(GoogleMap googleMap, @Nullable Consumer<Polygon> consumer) {
        final Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setTag(tag);

        if (consumer != null) {
            consumer.accept(polygon);
        }
    }

    private void setPath(@Nullable JSONArray path) {
        if (path != null) {
            List<LatLng> latLngList = getLatLngList(path);
            this.polygonOptions.addAll(latLngList);
        }
    }

    private void setHoles(@Nullable JSONArray holes) {
        if (holes != null) {
            for (int i = 0; i < holes.length(); i++) {
                // For each hole, get the path.
                JSONArray path = holes.optJSONArray(i);
                if (path != null) {
                    List<LatLng> latLngList = getLatLngList(path);
                    this.polygonOptions.addHole(latLngList);
                }
            }
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
        final float strokeWidth = (float) preferences.optDouble("strokeWidth", 10);
        final int strokeColor = WebColor.parseColor(preferences.optString("strokeColor", "#000000"));
        final int fillColor = WebColor.parseColor(preferences.optString("fillColor", "#00000000"));
        final float zIndex = (float) preferences.optDouble("zIndex", 0);
        final boolean isVisible = preferences.optBoolean("isVisible", true);
        final boolean isGeodesic = preferences.optBoolean("isGeodesic", false);
        final boolean isClickable = preferences.optBoolean("isClickable", false);

        polygonOptions.strokeWidth(strokeWidth);
        polygonOptions.strokeColor(strokeColor);
        polygonOptions.fillColor(fillColor);
        polygonOptions.zIndex(zIndex);
        polygonOptions.visible(isVisible);
        polygonOptions.geodesic(isGeodesic);
        polygonOptions.clickable(isClickable);
    }

    private void setMetadata(@NonNull JSObject jsObject) {
        JSObject tag = new JSObject();
        tag.put("id", this.polygonId);
        tag.put("metadata", jsObject);
        this.tag = tag;
    }

    public JSObject getResultForPolygon(Polygon polygon, String mapId) {
        JSObject tag = null;

        try {
            tag = (JSObject) polygon.getTag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tag = tag != null ? tag : new JSObject();
        }

        // initialize JSObjects to return
        JSObject result = new JSObject();
        JSObject polygonResult = new JSObject();
        JSObject preferencesResult = new JSObject();

        result.put("polygon", polygonResult);
        polygonResult.put("preferences", preferencesResult);

        // get map id
        polygonResult.put("mapId", mapId);

        // get id
        String polygonId = tag.optString("polygonId", polygon.getId());
        polygonResult.put("polygonId", polygonId);

        // get path values
        JSArray path = latLngsToJSArray(polygon.getPoints());
        polygonResult.put("path", path);

        // get preferences
        preferencesResult.put("strokeWidth", polygon.getStrokeWidth());
        preferencesResult.put("strokeColor", colorToString(polygon.getStrokeColor()));
        preferencesResult.put("fillColor", colorToString(polygon.getFillColor()));
        preferencesResult.put("zIndex", polygon.getZIndex());
        preferencesResult.put("isVisible", polygon.isVisible());
        preferencesResult.put("isGeodesic", polygon.isGeodesic());
        preferencesResult.put("isClickable", polygon.isClickable());
        // holes
        JSArray holesResult = new JSArray();
        for (List<LatLng> hole : polygon.getHoles()) {
            JSArray holeArray = latLngsToJSArray(hole);
            holesResult.put(holeArray);
        }
        if (holesResult.length() > 0) {
            preferencesResult.put("holes", holesResult);
        }
        // metadata
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
