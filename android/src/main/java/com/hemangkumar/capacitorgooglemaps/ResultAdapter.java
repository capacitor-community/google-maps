package com.hemangkumar.capacitorgooglemaps;

import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;

class ResultAdapter {
    private JSObject tag;
    private final String markerId;
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final float opacity;
    private final boolean isFlat;
    private final float zindex;
    private final boolean isDraggable;

    public ResultAdapter(Marker marker) {
        try {
            tag = (JSObject) marker.getTag();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tag = tag != null ? tag : new JSObject();
        }
        markerId = marker.getId();
        position = marker.getPosition();
        title = marker.getTitle();
        snippet = marker.getSnippet();
        opacity = marker.getAlpha();
        isFlat = marker.isFlat();
        zindex = marker.getZIndex();
        isDraggable = marker.isDraggable();
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
        position = customMarker.getPosition();
        title = customMarker.getTitle();
        snippet = customMarker.getSnippet();
        opacity = customMarker.getOpacity();
        isFlat = customMarker.isFlat();
        zindex = customMarker.getZIndex();
        isDraggable = customMarker.isDraggable();
    }

    public JSObject getTag() {
        return tag;
    }

    public String getMarkerId() {
        return markerId;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }

    public float getOpacity() {
        return opacity;
    }

    public boolean isFlat() {
        return isFlat;
    }

    public float getZIndex() {
        return zindex;
    }

    public boolean isDraggable() {
        return isDraggable;
    }
}
