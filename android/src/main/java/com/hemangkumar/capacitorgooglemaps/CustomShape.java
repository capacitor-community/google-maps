package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Dash;
import com.google.android.libraries.maps.model.Dot;
import com.google.android.libraries.maps.model.Gap;
import com.google.android.libraries.maps.model.JointType;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.PatternItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class CustomShape<T extends Shape> {
    public final String id = UUID.randomUUID().toString();
    protected JSObject tag = new JSObject();

    public void updateFromJSObject(JSObject jsObject) {
        ShapeOptions options = newOptions();
        ShapeTraits traits = getShapeTraits();
        if (traits.hasPoints()) {
            loadPoints(jsObject, options::add);
        }
        if (traits.hasCenter()) {
            JSObject jsCenterLatLng = JSObjectDefaults.getJSObjectSafe(
                    jsObject, "center", new JSObject());
            options.center(loadLatLng(jsCenterLatLng));
        }
        if (traits.hasRadius()) {
            options.radius(JSObjectDefaults.getDoubleSafe(
                    jsObject, "radius", 1000d));
        }
        JSObject jsPreferences = jsObject.getJSObject("preferences");
        if (jsPreferences != null) {
            loadHoles(jsPreferences);
            initPlainFields(jsPreferences);
            saveMetadataToTag(jsPreferences);
        }
    }

    public void updateShape(T shape) {
        ShapeOptions options = getOptions();
        shape.setPoints(options.getPoints());
        shape.setHoles(options.getHoles());
        shape.setStrokePattern(options.getStrokePattern());
        shape.setStrokeJointType(options.getStrokeJointType());
        shape.setStrokeWidth(options.getStrokeWidth());
        shape.setStrokeColor(options.getStrokeColor());
        shape.setClickable(options.isClickable());
        shape.setFillColor(options.getFillColor());
        shape.setGeodesic(options.isGeodesic());
        shape.setVisible(options.isVisible());
        shape.setZIndex(options.getZIndex());
        shape.setPattern(options.getPattern());
        shape.setJointType(options.getJointType());
        shape.setWidth(options.getWidth());
        shape.setColor(options.getColor());
        shape.setRadius(options.getRadius());
        shape.setCenter(options.getCenter());

        saveCurrentMetadataToTag(shape.getTag());
    }

    public JSObject getResultFor(Shape shape, String mapId) {
        // initialize JSObjects to return
        JSObject jsResult = new JSObject();
        JSObject jsShape = new JSObject();
        JSObject jsPreferences = new JSObject();
        final String shapeName = shape.getShapeName();
        jsResult.put(shapeName, jsShape);

        ShapeTraits traits = getShapeTraits();

        if (traits.hasPoints()) {
            jsShape.put("points", latLongsToJSArray(shape.getPoints()));
        }
        if (traits.hasGeodesic()) {
            jsPreferences.put("isGeodesic", shape.isGeodesic());
        }
        if (traits.hasCenter()) {
            jsShape.put("center", latLngToJSObject(shape.getCenter()));
        }
        if (traits.hasStrokeWidth()) {
            jsPreferences.put("strokeWidth", shape.getStrokeWidth());
        }
        if (traits.hasStrokeColor()) {
            jsPreferences.put("strokeColor", colorToString(shape.getStrokeColor()));
        }
        if (traits.hasFillColor()) {
            jsPreferences.put("fillColor", colorToString(shape.getFillColor()));
        }
        if (traits.hasWidth()) {
            jsPreferences.put("width", shape.getWidth());
        }
        if (traits.hasColor()) {
            jsPreferences.put("color", colorToString(shape.getColor()));
        }
        if (traits.hasJointType()) {
            jsPreferences.put("jointType", getJointTypeName(shape.getJointType()));
        }
        if (traits.hasPattern()) {
            // preferences.pattern
            JSArray jsPattern = patternToJSArray(shape.getPattern());
            if (jsPattern.length() > 0) {
                jsPreferences.put("pattern", jsPattern);
            }
        }
        if (traits.hasStrokeJointType()) {
            jsPreferences.put("strokeJointType", getJointTypeName(shape.getStrokeJointType()));
        }
        if (traits.hasStrokePatterns()) {
            // preferences.strokePattern
            JSArray jsStrokePattern = patternToJSArray(shape.getStrokePattern());

            if (jsStrokePattern.length() > 0) {
                jsPreferences.put("strokePattern", jsStrokePattern);
            }
        }
        if (traits.hasHoles()) {
            // preferences.holes
            JSArray jsHoles = new JSArray();
            for (List<LatLng> hole : shape.getHoles()) {
                JSArray jsHole = latLongsToJSArray(hole);
                jsHoles.put(jsHole);
            }
            if (jsHoles.length() > 0) {
                jsPreferences.put("holes", jsHoles);
            }
        }

        // metadata
        JSObject tag = (JSObject) shape.getTag();
        jsPreferences.put("metadata", getMetadata(tag));
        // other preferences
        jsPreferences.put("zIndex", shape.getZIndex());
        jsPreferences.put("visibility", shape.isVisible());
        jsPreferences.put("isClickable", shape.isClickable());

        jsShape.put("preferences", jsPreferences);

        // map id
        jsShape.put("mapId", mapId);

        // id
        String id = tag.optString("id", shape.getId());
        jsShape.put("id", id);

        return jsResult;
    }

    protected abstract ShapeOptions getOptions();

    protected abstract ShapeTraits getShapeTraits();

    protected abstract ShapeOptions newOptions();

    public abstract T addToMap(GoogleMap googleMap);

    protected void saveMetadataToTag(JSObject preferences) {
        JSObject jsMetadata = JSObjectDefaults.getJSObjectSafe(
                preferences, "metadata", new JSObject());
        JSObject tag = new JSObject();
        tag.put("id", id);
        tag.put("metadata", jsMetadata);
        this.tag = tag;
    }

    protected JSObject getMetadata() {
        return JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
    }

    protected void saveCurrentMetadataToTag(Object tag) {
        JSObject jsTag;
        if (tag instanceof JSObject) {
            jsTag = (JSObject) tag;
        } else {
            jsTag = new JSObject();
            jsTag.put("id", id);
        }
        jsTag.put("metadata", getMetadata());
    }

    protected static JSObject getMetadata(JSObject tag) {
        return JSObjectDefaults.getJSObjectSafe(tag, "metadata", new JSObject());
    }

    protected static String colorToString(int color) {
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

    protected static List<PatternItem> parsePatternItems(final JSArray jsPatterns) {
        int n = jsPatterns.length();
        List<PatternItem> strokePattern = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            JSObject jsPatterItem = JSObjectDefaults.getJSObjectByIndex(jsPatterns, i);
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
                case "Gap":
                    item = new Gap(length);
                    break;
                default:
                    continue;
            }
            strokePattern.add(item);
        }
        return strokePattern;
    }

    protected static void loadPoints(final JSObject jsPoly, Consumer<LatLng> consumer) {
        JSArray jsPoints = JSObjectDefaults.getJSArray(jsPoly, "points", new JSArray());
        int n = jsPoints.length();
        for (int i = 0; i < n; i++) {
            JSObject jsLatLng = JSObjectDefaults.getJSObjectByIndex(jsPoints, i);
            consumer.accept(loadLatLng(jsLatLng));
        }
    }

    protected static LatLng loadLatLng(JSObject jsLatLng) {
        double latitude = jsLatLng.optDouble("latitude", 0d);
        double longitude = jsLatLng.optDouble("longitude", 0d);
        return new LatLng(latitude, longitude);
    }

    protected static String getJointTypeName(int jt) {
        switch (jt) {
            case JointType.BEVEL:
                return "BEVEL";
            case JointType.ROUND:
                return "ROUND";
            default:
                return "DEFAULT";
        }
    }

    protected static int parseJointTypeName(String strokeJointTypeName) {
        switch (strokeJointTypeName) {
            case "BEVEL":
                return JointType.BEVEL;
            case "ROUND":
                return JointType.ROUND;
            default:
                return JointType.DEFAULT;
        }
    }

    protected static JSArray patternToJSArray(Collection<PatternItem> patterns) {
        JSArray jsStrokePattern = new JSArray();
        if (patterns == null) {
            return jsStrokePattern;
        }
        final String pattern = "pattern";
        final String length = "length";
        for (PatternItem patternItem : patterns) {
            JSObject jsPatternItem = new JSObject();
            if (patternItem instanceof Dot) {
                jsPatternItem.put(pattern, "Dot");
            } else if (patternItem instanceof Dash) {
                jsPatternItem.put(pattern, "Dash");
                jsPatternItem.put(length, ((Dash) patternItem).length);
            } else if (patternItem instanceof Gap) {
                jsPatternItem.put(pattern, "Gap");
                jsPatternItem.put(length, ((Gap) patternItem).length);
            }
            if (jsPatternItem.length() > 0) {
                jsStrokePattern.put(jsPatternItem);
            }
        }
        return jsStrokePattern;
    }

    protected static JSArray latLongsToJSArray(Collection<LatLng> positions) {
        JSArray jsPositions = new JSArray();
        for (LatLng pos : positions) {
            JSObject jsPos = latLngToJSObject(pos);
            jsPositions.put(jsPos);
        }
        return jsPositions;
    }

    @NonNull
    protected static JSObject latLngToJSObject(LatLng latLng) {
        JSObject jsPos = new JSObject();
        jsPos.put("latitude", latLng.latitude);
        jsPos.put("longitude", latLng.longitude);
        return jsPos;
    }

    private void initPlainFields(final JSObject jsPreferences) {
        ShapeTraits traits = getShapeTraits();
        ShapeOptions options = getOptions();
        if (traits.hasStrokeWidth()) {
            final float strokeWidth = (float) jsPreferences.optDouble("strokeWidth", 6);
            options.strokeWidth(strokeWidth);
        }
        if (traits.hasStrokeColor()) {
            final int strokeColor = Color.parseColor(jsPreferences.optString("strokeColor", "#000000"));
            options.strokeColor(strokeColor);
        }
        if (traits.hasFillColor()) {
            final int fillColor = Color.parseColor(jsPreferences.optString("fillColor", "#300000FF"));
            options.fillColor(fillColor);
        }
        if (traits.hasGeodesic()) {
            final boolean isGeodesic = jsPreferences.optBoolean("isGeodesic", false);
            options.geodesic(isGeodesic);
        }
        if (traits.hasStrokePatterns()) {
            final List<PatternItem> strokePattern = parsePatternItems(JSObjectDefaults.getJSArray(
                    jsPreferences, "strokePattern", new JSArray()));
            if (!strokePattern.isEmpty()) {
                options.strokePattern(strokePattern);
            }
        }
        if (traits.hasStrokeJointType()) {
            options.strokeJointType(
                    parseJointTypeName(jsPreferences.optString("strokeJointType", ""))
            );
        }
        if (traits.hasWidth()) {
            final float width = (float) jsPreferences.optDouble("width", 6);
            options.width(width);
        }
        if (traits.hasColor()) {
            final int color = Color.parseColor(jsPreferences.optString("color", "#000000"));
            options.color(color);
        }
        if (traits.hasPattern()) {
            final List<PatternItem> patternItems = parsePatternItems(JSObjectDefaults.getJSArray(
                    jsPreferences, "pattern", new JSArray()));
            if (!patternItems.isEmpty()) {
                options.pattern(patternItems);
            }
        }


        final float zIndex = (float) jsPreferences.optDouble("zIndex", 0);
        final boolean visibility = jsPreferences.optBoolean("visibility", true);
        final boolean isClickable = jsPreferences.optBoolean("isClickable", false);
        options.zIndex(zIndex);
        options.visible(visibility);
        options.clickable(isClickable);
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
            getOptions().addHole(holeList);
        }
    }

}
