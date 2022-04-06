package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.MarkerOptions;

public interface OnBeforeAddMarker {
    Object getTag(MarkerOptions opts);
}
