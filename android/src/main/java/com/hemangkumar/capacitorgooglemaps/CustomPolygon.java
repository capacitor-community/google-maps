package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Dash;
import com.google.android.libraries.maps.model.Dot;
import com.google.android.libraries.maps.model.Gap;
import com.google.android.libraries.maps.model.JointType;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polygon;
import com.google.android.libraries.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomPolygon {

    public final String polygonId = UUID.randomUUID().toString();
    private JSObject tag = new JSObject();
    private List<LatLng> points;
    private List<List<LatLng>> holes;
    private float strokeWidth;
    private int strokeColor;
    private int fillColor;
    private float zIndex;
    private boolean visibility;
    private boolean isGeodesic;
    private boolean isClickable;
    private int strokeJointType;
    private List<PatternItem> strokePattern;
    private JSObject metadata;

    public void updateFromJSObject(JSObject polygon) {
        loadPoints(polygon);

        JSObject preferences = JSObjectDefaults.getJSObjectSafe(
                polygon, "preferences", new JSObject());

        loadHoles(preferences);
        loadStrokePattern(preferences);
        initPlainFields(preferences);
    }

    public void updatePolygonOptions(PolygonOptions polygonOptions) {
        for (LatLng point : points) {
            polygonOptions.add(point);
        }

        for (Iterable<LatLng> hole : holes) {
            polygonOptions.addHole(hole);
        }

        polygonOptions
                .strokePattern(strokePattern)
                .strokeJointType(strokeJointType)
                .strokeWidth(strokeWidth)
                .strokeColor(strokeColor)
                .clickable(isClickable)
                .fillColor(fillColor)
                .geodesic(isGeodesic)
                .visible(visibility)
                .zIndex(zIndex);

        setMetadata();
    }

    public Polygon addToMap(GoogleMap googleMap, PolygonOptions polygonOptions) {
        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setTag(this.tag);
        return polygon;
    }

    public static JSObject getResultForPolygon(Polygon polygon, String mapId) {
        JSObject tag = (JSObject) polygon.getTag();

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

        // metadata
        JSObject metadata = JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
        preferencesResult.put("metadata", metadata);

        return result;
    }

    private void setMetadata() {
        JSObject tag = new JSObject();
        tag.put("polygonId", this.polygonId);
        tag.put("metadata", metadata);
        this.tag = tag;
    }

    private void initPlainFields(final JSObject preferences) {
        strokeWidth = (float) preferences.optDouble("strokeWidth", 2);
        strokeColor = Color.parseColor(preferences.optString("strokeColor", "#000000"));
        fillColor = Color.parseColor(preferences.optString("fillColor", "#300000FF"));
        zIndex = (float) preferences.optDouble("zIndex", 0);
        visibility = preferences.optBoolean("visibility", true);
        isGeodesic = preferences.optBoolean("isGeodesic", false);
        isClickable = preferences.optBoolean("isClickable", false);
        metadata = JSObjectDefaults.getJSObjectSafe(preferences, "metadata", new JSObject());

        switch (preferences.optString("strokeJointType", "DEFAULT")) {
            case "BEVEL":
                strokeJointType = JointType.BEVEL;
                break;
            case "ROUND":
                strokeJointType = JointType.ROUND;
                break;
            default:
                strokeJointType = JointType.DEFAULT;
        }
    }

    private void loadStrokePattern(final JSObject preferences) {
        JSArray jsStrokePattern = JSObjectDefaults.getJSArray(
                preferences, "strokePattern", new JSArray());
        int n = jsStrokePattern.length();
        strokePattern = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            JSObject jsPatterItem = JSObjectDefaults.getJSObjectByIndex(jsStrokePattern, i);
            String pattern = jsPatterItem.optString("pattern", "Gap");
            float length = (float) jsPatterItem.optDouble("length", 20);
            PatternItem item;
            switch (pattern) {
                case "Dash":
                    item = new Dash(length);
                    break;
                case "Dot":
                    item = new Dot();
                    break;
                default:
                    item = new Gap(length);

            }
            strokePattern.add(item);
        }
    }

    private void loadHoles(final JSObject preferences) {
        JSArray jsHoles = JSObjectDefaults.getJSArray(preferences, "holes", new JSArray());
        int n = jsHoles.length();
        holes = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            JSArray jsLatLngArr = JSObjectDefaults.getJSArray(jsHoles, i, new JSArray());
            int m = jsLatLngArr.length();
            List<LatLng> holeList = new ArrayList<>(m);
            holes.add(holeList);
            for (int j = 0; j < m; j++) {
                JSObject jsLatLon = JSObjectDefaults.getJSObjectByIndex(jsLatLngArr, j);
                double latitude = jsLatLon.optDouble("latitude", 0d);
                double longitude = jsLatLon.optDouble("longitude", 0d);
                holeList.add(new LatLng(latitude, longitude));
            }
        }
    }

    private void loadPoints(final JSObject polygon) {
        JSArray jsPoints = JSObjectDefaults.getJSArray(polygon, "points", new JSArray());
        int n = jsPoints.length();
        points = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            JSObject jsLatLng = JSObjectDefaults.getJSObjectByIndex(jsPoints, i);
            double latitude = jsLatLng.optDouble("latitude", 0d);
            double longitude = jsLatLng.optDouble("longitude", 0d);
            points.add(new LatLng(latitude, longitude));
        }
    }
}
