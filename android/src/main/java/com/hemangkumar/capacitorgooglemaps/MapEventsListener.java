package com.hemangkumar.capacitorgooglemaps;

import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.PointOfInterest;

import java.util.LinkedHashSet;
import java.util.Set;

class MapEventsListener implements
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMyLocationClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnPoiClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {

    private final Set<GoogleMap.OnInfoWindowClickListener> onInfoWindowClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnInfoWindowCloseListener> onInfoWindowCloseListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnMapClickListener> onMapClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnMapLongClickListener> onMapLongClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnMarkerClickListener> onMarkerClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnMarkerDragListener> onMarkerDragListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnMyLocationClickListener> onMyLocationClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnMyLocationButtonClickListener> onMyLocationButtonClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnPoiClickListener> onPoiClickListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnCameraMoveStartedListener> onCameraMoveStartedListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnCameraMoveListener> onCameraMoveListeners =
            new LinkedHashSet<>();

    private final Set<GoogleMap.OnCameraIdleListener> onCameraIdleListeners =
            new LinkedHashSet<>();

    @Override
    public void onCameraIdle() {
        for (GoogleMap.OnCameraIdleListener listener : onCameraIdleListeners) {
            listener.onCameraIdle();
        }
    }

    public void addOnCameraIdleListener(GoogleMap.OnCameraIdleListener listener) {
        onCameraIdleListeners.add(listener);
    }

    public void removeOnCameraIdleListener(GoogleMap.OnCameraIdleListener listener) {
        onCameraIdleListeners.remove(listener);
    }

    @Override
    public void onCameraMove() {
        for (GoogleMap.OnCameraMoveListener listener : onCameraMoveListeners) {
            listener.onCameraMove();
        }
    }

    public void addOnCameraMoveListener(GoogleMap.OnCameraMoveListener listener) {
        onCameraMoveListeners.add(listener);
    }

    public void removeOnCameraMoveListener(GoogleMap.OnCameraMoveListener listener) {
        onCameraMoveListeners.remove(listener);
    }

    @Override
    public void onCameraMoveStarted(int i) {
        for (GoogleMap.OnCameraMoveStartedListener listener : onCameraMoveStartedListeners) {
            listener.onCameraMoveStarted(i);
        }
    }

    public void addOnCameraMoveStartedListener(GoogleMap.OnCameraMoveStartedListener listener) {
        onCameraMoveStartedListeners.add(listener);
    }

    public void removeOnCameraMoveStartedListener(GoogleMap.OnCameraMoveStartedListener listener) {
        onCameraMoveStartedListeners.remove(listener);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        for (GoogleMap.OnInfoWindowClickListener listener : onInfoWindowClickListeners) {
            listener.onInfoWindowClick(marker);
        }
    }

    public void addOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener listener) {
        onInfoWindowClickListeners.add(listener);
    }

    public void removeOnInfoWindowClickListener(GoogleMap.OnInfoWindowClickListener listener) {
        onInfoWindowClickListeners.remove(listener);
    }

    @Override
    public void onInfoWindowClose(@NonNull Marker marker) {
        for (GoogleMap.OnInfoWindowCloseListener listener : onInfoWindowCloseListeners) {
            listener.onInfoWindowClose(marker);
        }
    }

    public void addOnInfoWindowCloseListener(GoogleMap.OnInfoWindowCloseListener listener) {
        onInfoWindowCloseListeners.add(listener);
    }

    public void removeOnInfoWindowCloseListener(GoogleMap.OnInfoWindowCloseListener listener) {
        onInfoWindowCloseListeners.remove(listener);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        for (GoogleMap.OnMapClickListener listener : onMapClickListeners) {
            listener.onMapClick(latLng);
        }
    }

    public void addOnMapClickListener(GoogleMap.OnMapClickListener listener) {
        onMapClickListeners.add(listener);
    }

    public void removeOnMapClickListener(GoogleMap.OnMapClickListener listener) {
        onMapClickListeners.remove(listener);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        for (GoogleMap.OnMapLongClickListener listener : onMapLongClickListeners) {
            listener.onMapLongClick(latLng);
        }
    }

    public void addOnMapLongClickListener(GoogleMap.OnMapLongClickListener listener) {
        onMapLongClickListeners.add(listener);
    }

    public void removeOnMapLongClickListener(GoogleMap.OnMapLongClickListener listener) {
        onMapLongClickListeners.remove(listener);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        boolean result = false;
        for (GoogleMap.OnMarkerClickListener listener : onMarkerClickListeners) {
            result |= listener.onMarkerClick(marker);
        }
        return result;
    }

    public void addOnMarkerClickListener(GoogleMap.OnMarkerClickListener listener) {
        onMarkerClickListeners.add(listener);
    }

    public void removeOnMarkerClickListener(GoogleMap.OnMarkerClickListener listener) {
        onMarkerClickListeners.remove(listener);
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        for (GoogleMap.OnMarkerDragListener listener : onMarkerDragListeners) {
            listener.onMarkerDrag(marker);
        }
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        for (GoogleMap.OnMarkerDragListener listener : onMarkerDragListeners) {
            listener.onMarkerDragEnd(marker);
        }
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        for (GoogleMap.OnMarkerDragListener listener : onMarkerDragListeners) {
            listener.onMarkerDragStart(marker);
        }
    }

    public void addOnMarkerDragListener(GoogleMap.OnMarkerDragListener listener) {
        onMarkerDragListeners.add(listener);
    }

    public void removeOnMarkerDragListener(GoogleMap.OnMarkerDragListener listener) {
        onMarkerDragListeners.remove(listener);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        boolean result = false;
        for (GoogleMap.OnMyLocationButtonClickListener listener : onMyLocationButtonClickListeners) {
            result |= listener.onMyLocationButtonClick();
        }
        return result;
    }

    public void addOnMyLocationButtonClickListener(GoogleMap.OnMyLocationButtonClickListener listener) {
        onMyLocationButtonClickListeners.add(listener);
    }

    public void removeOnMyLocationButtonClickListener(GoogleMap.OnMyLocationButtonClickListener listener) {
        onMyLocationButtonClickListeners.remove(listener);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        for (GoogleMap.OnMyLocationClickListener listener : onMyLocationClickListeners) {
            listener.onMyLocationClick(location);
        }
    }

    public void addOnMyLocationClickListener(GoogleMap.OnMyLocationClickListener listener) {
        onMyLocationClickListeners.add(listener);
    }

    public void removeOnMyLocationClickListener(GoogleMap.OnMyLocationClickListener listener) {
        onMyLocationClickListeners.remove(listener);
    }

    @Override
    public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
        for (GoogleMap.OnPoiClickListener listener : onPoiClickListeners) {
            listener.onPoiClick(pointOfInterest);
        }
    }

    public void addOnPoiClickListener(GoogleMap.OnPoiClickListener listener) {
        onPoiClickListeners.add(listener);
    }

    public void removeOnPoiClickListener(GoogleMap.OnPoiClickListener listener) {
        onPoiClickListeners.remove(listener);
    }
}
