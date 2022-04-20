package com.hemangkumar.capacitorgooglemaps;

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

public abstract class CustomShape<T> {
    public final String id = UUID.randomUUID().toString();
    protected JSObject tag = new JSObject();

    public abstract void updateFromJSObject(JSObject jsObject);

    public abstract void updateShape(T shape);

    public abstract T addToMap(GoogleMap googleMap);

    protected abstract String getObjectIdTagName();

    protected void saveMetadataToTag(JSObject preferences) {
        JSObject jsMetadata = JSObjectDefaults.getJSObjectSafe(
                preferences, "metadata", new JSObject());
        JSObject tag = new JSObject();
        tag.put(getObjectIdTagName(), id);
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
            jsTag.put(getObjectIdTagName(), id);
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
}
