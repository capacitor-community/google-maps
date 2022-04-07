package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;

public interface MarkerLifecycle {
    Object onBeforeAddMarker(MarkerOptions opts);
    void onBeforeDeleteMarker(Marker marker);
}
