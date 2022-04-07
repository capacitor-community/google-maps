package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

    private Bitmap bitmap;

    public CustomClusterRenderer(
            AppCompatActivity activity,
            GoogleMap map,
            ClusterManager<CustomClusterItem> clusterManager) {
        super(activity, map, clusterManager);
    }

    public void setIcon(Bitmap bitmap) {
        this.bitmap = bitmap;
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
        if (bitmap != null) {
            markerOptions.icon(getClusterIcon(cluster));
        } else {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }
    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<CustomClusterItem> cluster, Marker marker) {
        if (bitmap != null) {
            // Same implementation as onBeforeClusterRendered() (to update cached markers)
            marker.setIcon(getClusterIcon(cluster));
        } else {
            super.onClusterUpdated(cluster, marker);
        }
    }

    private BitmapDescriptor getClusterIcon(Cluster<CustomClusterItem> cluster) {
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
