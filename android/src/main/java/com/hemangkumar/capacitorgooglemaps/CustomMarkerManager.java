package com.hemangkumar.capacitorgooglemaps;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.collections.MarkerManager;

public class CustomMarkerManager extends MarkerManager {

    private final ProxyEventListener proxyEventListener;
    private final MarkerLifecycle markerLifecycle;

    public CustomMarkerManager(
            GoogleMap map,
            ProxyEventListener proxyEventListener,
            MarkerLifecycle markerLifecycle) {
        super(map);
        this.proxyEventListener = proxyEventListener;
        this.markerLifecycle = markerLifecycle;
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

    @Override
    public Collection newCollection() {
        return new CustomCollection();
    }

    public class CustomCollection extends MarkerManager.Collection {

        @Override
        public Marker addMarker(MarkerOptions opts) {
            Object tag = markerLifecycle.onBeforeAddMarker(opts);
            Marker marker = super.addMarker(opts);
            marker.setTag(tag);
            return marker;
        }

        @Override
        public boolean remove(Marker marker) {
            markerLifecycle.onBeforeDeleteMarker(marker);
            return super.remove(marker);
        }
    }
}
