package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.PatternItem;
import com.google.android.libraries.maps.model.Polyline;
import com.google.android.libraries.maps.model.PolylineOptions;

import java.util.List;

public class CustomPolyline extends CustomShape<Polyline> {
    private PolylineOptions polylineOptions = new PolylineOptions();

    @Override
    public void updateFromJSObject(JSObject polyline) {
        polylineOptions = new PolylineOptions();
        loadPoints(polyline, polylineOptions::add);
        JSObject preferences = polyline.getJSObject("preferences");
        if (preferences != null) {
            initPlainFields(preferences);
            saveMetadataToTag(preferences);
        }
    }

    @Override
    protected String getObjectIdTagName() {
        return "polylineId";
    }

    public void updateObject(Polyline polyline) {
        polyline.setPattern(polylineOptions.getPattern());
        polyline.setJointType(polylineOptions.getJointType());
        polyline.setWidth(polylineOptions.getWidth());
        polyline.setColor(polylineOptions.getColor());
        polyline.setClickable(polylineOptions.isClickable());
        polyline.setGeodesic(polylineOptions.isGeodesic());
        polyline.setVisible(polylineOptions.isVisible());
        polyline.setZIndex(polylineOptions.getZIndex());

        saveCurrentMetadataToTag(polyline.getTag());
    }

    @Override
    public Polyline addToMap(GoogleMap googleMap) {
        Polyline polyline = googleMap.addPolyline(polylineOptions);
        polyline.setTag(tag);
        return polyline;
    }

    public static JSObject getResultForPolyline(Polyline polyline, String mapId) {
        JSObject tag = (JSObject) polyline.getTag();

        // initialize JSObjects to return
        JSObject jsResult = new JSObject();
        JSObject jsPolyline = new JSObject();
        JSObject jsPreferences = new JSObject();

        jsResult.put("polyline", jsPolyline);
        jsPolyline.put("points", latLonsToJSArray(polyline.getPoints()));
        jsPolyline.put("preferences", jsPreferences);

        // get map id
        jsPolyline.put("mapId", mapId);

        // get id
        String polylineId = tag.optString("polylineId", polyline.getId());
        jsPolyline.put("polylineId", tag.optString("polylineId", polyline.getId()));

        // metadata
        jsPreferences.put("metadata", getMetadata(tag));
        // other preferences
        jsPreferences.put("width", polyline.getWidth());
        jsPreferences.put("color", colorToString(polyline.getColor()));
        jsPreferences.put("zIndex", polyline.getZIndex());
        jsPreferences.put("visibility", polyline.isVisible());
        jsPreferences.put("isGeodesic", polyline.isGeodesic());
        jsPreferences.put("isClickable", polyline.isClickable());
        jsPreferences.put("pattern", patternToJSArray(polyline.getPattern()));
        jsPreferences.put("jointType", getJointTypeName(polyline.getJointType()));
        // preferences.pattern
        JSArray jsPattern = patternToJSArray(polyline.getPattern());
        if (jsPattern.length() > 0) {
            jsPreferences.put("pattern", jsPattern);
        }
        return jsResult;
    }

    private void initPlainFields(final JSObject jsPreferences) {
        final float width = (float) jsPreferences.optDouble("width", 6);
        final int color = Color.parseColor(jsPreferences.optString("color", "#000000"));
        final float zIndex = (float) jsPreferences.optDouble("zIndex", 0);
        final boolean visibility = jsPreferences.optBoolean("visibility", true);
        final boolean isGeodesic = jsPreferences.optBoolean("isGeodesic", false);
        final boolean isClickable = jsPreferences.optBoolean("isClickable", false);
        final List<PatternItem> pattern = parsePatternItems(JSObjectDefaults.getJSArray(
                jsPreferences, "pattern", new JSArray()));

        polylineOptions
                .width(width)
                .color(color)
                .zIndex(zIndex)
                .visible(visibility)
                .geodesic(isGeodesic)
                .clickable(isClickable)
                .jointType(
                        parseJointTypeName(jsPreferences.optString("jointType", ""))
                );
        if (!pattern.isEmpty()) {
            polylineOptions.pattern(pattern);
        }
    }
}