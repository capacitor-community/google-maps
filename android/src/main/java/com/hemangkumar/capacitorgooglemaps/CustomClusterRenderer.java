package com.hemangkumar.capacitorgooglemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.BitmapDescriptor;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

    public CustomClusterRenderer(AppCompatActivity activity, GoogleMap map, ClusterManager<CustomClusterItem> clusterManager) {
        super(activity, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull CustomClusterItem item, @NonNull MarkerOptions markerOptions) {
        item.getCustomMarker().updateMarkerOptions(markerOptions);
    }

    @Override
    protected void onClusterItemUpdated(@NonNull CustomClusterItem item, @NonNull Marker marker) {
        super.onClusterItemUpdated(item, marker);
        BitmapDescriptor icon = item.getCustomMarker().getBitmapDescriptor();
        marker.setIcon(icon);
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<CustomClusterItem> cluster, @NonNull MarkerOptions markerOptions) {
        // TODO: consider adding anchor(.5, .5) (Individual markers will overlap more often)
        markerOptions.icon(getDescriptorForCluster(cluster));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
