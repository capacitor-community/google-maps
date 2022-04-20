package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polygon;
import com.google.android.libraries.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class CustomPolygon extends CustomShape<Polygon> {
    private PolygonOptions polygonOptions = new PolygonOptions();

    @Override
    public void updateFromJSObject(JSObject jsPolygon) {
        polygonOptions = new PolygonOptions();
        loadPoints(jsPolygon, polygonOptions::add);
        JSObject jsPreferences = jsPolygon.getJSObject("preferences");
        if (jsPreferences != null) {
            loadHoles(jsPreferences);
            initPlainFields(jsPreferences);
            saveMetadataToTag(jsPreferences);
        }
    }

    @Override
    protected String getObjectIdTagName() {
        return "polygonId";
    }

    @Override
    public void updateShape(Polygon polygon) {
        polygon.setHoles(polygonOptions.getHoles());
        polygon.setStrokePattern(polygonOptions.getStrokePattern());
        polygon.setStrokeJointType(polygonOptions.getStrokeJointType());
        polygon.setStrokeWidth(polygonOptions.getStrokeWidth());
        polygon.setStrokeColor(polygonOptions.getStrokeColor());
        polygon.setClickable(polygonOptions.isClickable());
        polygon.setFillColor(polygonOptions.getFillColor());
        polygon.setGeodesic(polygonOptions.isGeodesic());
        polygon.setVisible(polygonOptions.isVisible());
        polygon.setZIndex(polygonOptions.getZIndex());

        saveCurrentMetadataToTag(polygon.getTag());
    }

    @Override
    public Polygon addToMap(GoogleMap googleMap) {
        Polygon polygon = googleMap.addPolygon(polygonOptions);
        polygon.setTag(tag);
        return polygon;
    }

    // todo: AGalilov: DRY! move this code to the parent class
    public static JSObject getResultForPolygon(Polygon polygon, String mapId) {
        JSObject tag = (JSObject) polygon.getTag();

        // initialize JSObjects to return
        JSObject jsResult = new JSObject();
        JSObject jsPolygon = new JSObject();
        JSObject jsPreferences = new JSObject();

        jsResult.put("polygon", jsPolygon);
        jsPolygon.put("points", latLongsToJSArray(polygon.getPoints()));
        jsPolygon.put("preferences", jsPreferences);

        // get map id
        jsPolygon.put("mapId", mapId);

        // get id
        String polygonId = tag.optString("polygonId", polygon.getId());
        jsPolygon.put("polygonId", polygonId);

        // preferences.holes
        JSArray jsHoles = new JSArray();
        for (List<LatLng> hole : polygon.getHoles()) {
            JSArray jsHole = latLongsToJSArray(hole);
            jsHoles.put(jsHole);
        }
        if (jsHoles.length() > 0) {
            jsPreferences.put("holes", jsHoles);
        }
        // metadata
        jsPreferences.put("metadata", getMetadata(tag));
        // other preferences
        jsPreferences.put("strokeWidth", polygon.getStrokeWidth());
        jsPreferences.put("strokeColor", colorToString(polygon.getStrokeColor()));
        jsPreferences.put("fillColor", colorToString(polygon.getFillColor()));
        jsPreferences.put("zIndex", polygon.getZIndex());
        jsPreferences.put("visibility", polygon.isVisible());
        jsPreferences.put("isGeodesic", polygon.isGeodesic());
        jsPreferences.put("isClickable", polygon.isClickable());
        jsPreferences.put("strokeJointType", getJointTypeName(polygon.getStrokeJointType()));
        // preferences.strokePattern
        JSArray jsStrokePattern = patternToJSArray(polygon.getStrokePattern());
        if (jsStrokePattern.length() > 0) {
            jsPreferences.put("strokePattern", jsStrokePattern);
        }
        return jsResult;
    }

    // todo: AGalilov: DRY!
    private void initPlainFields(final JSObject jsPreferences) {
        // todo: Move to parent class
        final float strokeWidth = (float) jsPreferences.optDouble("strokeWidth", 6);
        final int strokeColor = Color.parseColor(jsPreferences.optString("strokeColor", "#000000"));
        final int fillColor = Color.parseColor(jsPreferences.optString("fillColor", "#300000FF"));
        final float zIndex = (float) jsPreferences.optDouble("zIndex", 0);
        final boolean visibility = jsPreferences.optBoolean("visibility", true);
        final boolean isGeodesic = jsPreferences.optBoolean("isGeodesic", false);
        final boolean isClickable = jsPreferences.optBoolean("isClickable", false);
        final List<PatternItem> strokePattern = parsePatternItems(JSObjectDefaults.getJSArray(
                jsPreferences, "strokePattern", new JSArray()));
        // todo: AGalilov: wrap ***Options class with common interface for PolygonOptions and CircleOptions
        polygonOptions.strokeWidth(strokeWidth);
        polygonOptions.strokeColor(strokeColor);
        polygonOptions.fillColor(fillColor);
        polygonOptions.zIndex(zIndex);
        polygonOptions.visible(visibility);
        polygonOptions.geodesic(isGeodesic);
        polygonOptions.clickable(isClickable);
        polygonOptions.strokeJointType(
                parseJointTypeName(jsPreferences.optString("strokeJointType", ""))
        );
        if (!strokePattern.isEmpty()) {
            polygonOptions.strokePattern(strokePattern);
        }
    }

    private void loadHoles(final JSObject preferences) {
        JSArray jsHoles = JSObjectDefaults.getJSArray(preferences, "holes", new JSArray());
        int n = jsHoles.length();
        for (int i = 0; i < n; i++) {
            JSArray jsLatLngArr = JSObjectDefaults.getJSArray(jsHoles, i, new JSArray());
            int m = jsLatLngArr.length();
            List<LatLng> holeList = new ArrayList<>(m);
            for (int j = 0; j < m; j++) {
                JSObject jsLatLon = JSObjectDefaults.getJSObjectByIndex(jsLatLngArr, j);
                holeList.add(loadLatLng(jsLatLon));
            }
            polygonOptions.addHole(holeList);
        }
    }
}
