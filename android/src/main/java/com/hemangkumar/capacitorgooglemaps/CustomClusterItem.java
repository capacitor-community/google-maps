package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

class CustomClusterItem implements ClusterItem {

    private final CustomMarker customMarker;

    public CustomClusterItem(CustomMarker customMarker) {
        this.customMarker = customMarker;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return getCustomMarker().getPosition();
    }

    @Nullable
    @Override
    public String getTitle() {
        return getCustomMarker().getTitle();
    }

    @Nullable
    @Override
    public String getSnippet() {
        return getCustomMarker().getSnippet();
    }

    public CustomMarker getCustomMarker() {
        return customMarker;
    }
}
