package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Circle;
import com.google.android.libraries.maps.model.CircleOptions;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.List;

public class CustomCircle extends CustomShape<Circle> {
    private CircleOptions circleOptions = new CircleOptions();

    @Override
    public void updateFromJSObject(JSObject jsCircle) {
        circleOptions = new CircleOptions();
        JSObject jsCenterLatLng = JSObjectDefaults.getJSObjectSafe(
                jsCircle, "center", new JSObject());
        circleOptions.center(loadLatLng(jsCenterLatLng));
        circleOptions.radius(JSObjectDefaults.getDoubleSafe(
                jsCircle, "radius", 1000d));
        JSObject jsPreferences = jsCircle.getJSObject("preferences");
        if (jsPreferences != null) {
            initPlainFields(jsPreferences);
            saveMetadataToTag(jsPreferences);
        }
    }

    @Override
    public void updateShape(Circle circle) {
        circle.setStrokePattern(circleOptions.getStrokePattern());
        circle.setStrokeWidth(circleOptions.getStrokeWidth());
        circle.setStrokeColor(circleOptions.getStrokeColor());
        circle.setClickable(circleOptions.isClickable());
        circle.setFillColor(circleOptions.getFillColor());
        circle.setVisible(circleOptions.isVisible());
        circle.setZIndex(circleOptions.getZIndex());

        saveCurrentMetadataToTag(circle.getTag());
    }

    public static JSObject getResultForCircle(Circle circle, String mapId) {
        JSObject tag = (JSObject) circle.getTag();

        // initialize JSObjects to return
        JSObject jsResult = new JSObject();
        JSObject jsCircle = new JSObject();
        JSObject jsPreferences = new JSObject();

        jsResult.put("circle", jsCircle);
        jsCircle.put("center", latLngToJSObject(circle.getCenter()));
        jsCircle.put("preferences", jsPreferences);

        // get map id
        jsCircle.put("mapId", mapId);

        // get id
        String circleId = tag.optString("circleId", circle.getId());
        // todo: AGalilov: DRY
        jsCircle.put("circleId", circleId);

        // metadata
        jsPreferences.put("metadata", getMetadata(tag));
        // other preferences
        jsPreferences.put("strokeWidth", circle.getStrokeWidth());
        jsPreferences.put("strokeColor", colorToString(circle.getStrokeColor()));
        jsPreferences.put("fillColor", colorToString(circle.getFillColor()));
        jsPreferences.put("zIndex", circle.getZIndex());
        jsPreferences.put("visibility", circle.isVisible());
        jsPreferences.put("isClickable", circle.isClickable());
        // preferences.strokePattern
        JSArray jsStrokePattern = patternToJSArray(circle.getStrokePattern());
        if (jsStrokePattern.length() > 0) {
            jsPreferences.put("strokePattern", jsStrokePattern);
        }
        return jsResult;
    }

    @Override
    public Circle addToMap(GoogleMap googleMap) {
        Circle circle = googleMap.addCircle(circleOptions);
        circle.setTag(tag);
        return circle;
    }

    @Override
    protected String getObjectIdTagName() {
        return "circleId";
    }

    // todo: AGalilov: DRY!
    private void initPlainFields(final JSObject jsPreferences) {
        // todo: Move to parent class
        final float strokeWidth = (float) jsPreferences.optDouble("strokeWidth", 6);
        final int strokeColor = Color.parseColor(jsPreferences.optString("strokeColor", "#000000"));
        final int fillColor = Color.parseColor(jsPreferences.optString("fillColor", "#300000FF"));
        final float zIndex = (float) jsPreferences.optDouble("zIndex", 0);
        final boolean visibility = jsPreferences.optBoolean("visibility", true);
        final boolean isClickable = jsPreferences.optBoolean("isClickable", false);
        final List<PatternItem> strokePattern = parsePatternItems(JSObjectDefaults.getJSArray(
                jsPreferences, "strokePattern", new JSArray()));
        // todo: AGalilov: wrap ***Options class with common interface for PolygonOptions and CircleOptions
        circleOptions.strokeWidth(strokeWidth);
        circleOptions.strokeColor(strokeColor);
        circleOptions.fillColor(fillColor);
        circleOptions.zIndex(zIndex);
        circleOptions.visible(visibility);
        circleOptions.clickable(isClickable);
        if (!strokePattern.isEmpty()) {
            circleOptions.strokePattern(strokePattern);
        }
    }
}
