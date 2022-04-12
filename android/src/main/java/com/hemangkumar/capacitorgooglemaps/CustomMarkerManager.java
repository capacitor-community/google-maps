package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.Marker;
import com.google.maps.android.collections.MarkerManager;

public class CustomMarkerManager extends MarkerManager {

    private final ProxyEventListener proxyEventListener;

    public CustomMarkerManager(
            GoogleMap map,
            ProxyEventListener proxyEventListener) {
        super(map);
        this.proxyEventListener = proxyEventListener;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        super.onInfoWindowClick(marker);
        proxyEventListener.onInfoWindowClick(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        super.onMarkerClick(marker);
        return proxyEventListener.onMarkerClick(marker);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        super.onMarkerDragStart(marker);
        proxyEventListener.onMarkerDragStart(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        super.onMarkerDrag(marker);
        proxyEventListener.onMarkerDrag(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        super.onMarkerDragEnd(marker);
        proxyEventListener.onMarkerDragEnd(marker);
    }
}
