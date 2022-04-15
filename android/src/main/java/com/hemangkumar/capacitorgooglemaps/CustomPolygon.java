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

        setMetadata(this.polygonId);
    }

    public void updatePolygon(Polygon polygon) {
        polygon.setHoles(holes);
        polygon.setStrokePattern(strokePattern);
        polygon.setStrokeJointType(strokeJointType);
        polygon.setStrokeWidth(strokeWidth);
        polygon.setStrokeColor(strokeColor);
        polygon.setClickable(isClickable);
        polygon.setFillColor(fillColor);
        polygon.setGeodesic(isGeodesic);
        polygon.setVisible(visibility);
        polygon.setZIndex(zIndex);
        Object tag = polygon.getTag();
        JSObject jsTag;
        if (tag instanceof JSObject) {
            jsTag = (JSObject) tag;
        } else {
            jsTag = new JSObject();
            jsTag.put("polygonId", polygonId);
        }
        jsTag.put("metadata", metadata);
    }

    public Polygon addToMap(GoogleMap googleMap, PolygonOptions polygonOptions) {
        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setTag(this.tag);
        return polygon;
    }

    public static JSObject getResultForPolygon(Polygon polygon, String mapId) {
        JSObject tag = (JSObject) polygon.getTag();

        // initialize JSObjects to return
        JSObject jsResult = new JSObject();
        JSObject jsPolygon = new JSObject();
        JSObject jsPreferences = new JSObject();
        JSArray jsPoints = new JSArray();

        jsResult.put("polygon", jsPolygon);
        jsPolygon.put("points", jsPoints);
        jsPolygon.put("preferences", jsPreferences);

        // get map id
        jsPolygon.put("mapId", mapId);

        // get id
        String polygonId = tag.optString("polygonId", polygon.getId());
        jsPolygon.put("polygonId", polygonId);

        // points
        for (LatLng point : polygon.getPoints()) {
            JSObject jsPoint = new JSObject();
            jsPoint.put("latitude", point.latitude);
            jsPoint.put("longitude", point.longitude);
            jsPoints.put(jsPoint);
        }

        // preferences.holes
        JSArray jsHoles = new JSArray();
        for (List<LatLng> hole : polygon.getHoles()) {
            JSArray jsHole = new JSArray();
            for (LatLng holePoint : hole) {
                JSObject jsHolePoint = new JSObject();
                jsHolePoint.put("latitude", holePoint.latitude);
                jsHolePoint.put("longitude", holePoint.longitude);
                jsHole.put(jsHolePoint);
            }
            jsHoles.put(jsHole);
        }
        if (jsHoles.length() > 0) {
            jsPreferences.put("holes", jsHoles);
        }
        // metadata
        JSObject metadata = JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
        jsPreferences.put("metadata", metadata);
        // other preferences
        jsPreferences.put("strokeWidth", polygon.getStrokeWidth());
        jsPreferences.put("strokeColor", colorToString(polygon.getStrokeColor()));
        jsPreferences.put("fillColor", colorToString(polygon.getFillColor()));
        jsPreferences.put("zIndex", polygon.getZIndex());
        jsPreferences.put("visibility", polygon.isVisible());
        jsPreferences.put("isGeodesic", polygon.isGeodesic());
        jsPreferences.put("isClickable", polygon.isClickable());
        switch (polygon.getStrokeJointType()) {
            case JointType.BEVEL:
                jsPreferences.put("strokeJointType", "BEVEL");
                break;
            case JointType.ROUND:
                jsPreferences.put("strokeJointType", "ROUND");
                break;
            case JointType.DEFAULT:
                jsPreferences.put("strokeJointType", "DEFAULT");
                break;
        }
        // preferences.strokePattern
        JSArray jsStrokePattern = new JSArray();
        for (PatternItem patternItem : polygon.getStrokePattern()) {
            JSObject jsPatternItem = new JSObject();
            if (patternItem instanceof Dot) {
                jsPatternItem.put("pattern", "Dot");
            } else if (patternItem instanceof Dash) {
                jsPatternItem.put("pattern", "Dash");
                jsPatternItem.put("length", ((Dash) patternItem).length);
            } else if (patternItem instanceof Gap) {
                jsPatternItem.put("pattern", "Gap");
                jsPatternItem.put("length", ((Gap) patternItem).length);
            }
            if (jsPatternItem.length() > 0) {
                jsStrokePattern.put(jsPatternItem);
            }
        }
        jsPreferences.put("strokePattern", jsStrokePattern);
        return jsResult;
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

    private void setMetadata(String polygonId) {
        JSObject tag = new JSObject();
        tag.put("polygonId", polygonId);
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
