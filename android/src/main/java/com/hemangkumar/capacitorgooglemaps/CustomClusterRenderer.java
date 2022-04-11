package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.model.BitmapDescriptor;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;
import com.google.android.libraries.maps.model.Marker;
import com.google.android.libraries.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.hemangkumar.capacitorgooglemaps.capacitorgooglemaps.R;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

class CustomClusterRenderer extends DefaultClusterRenderer<CustomClusterItem> {

    private Bitmap bitmap;
    private final IconGenerator iconGenerator;
    private final ImageView clusterImageView;
    private final TextView clusterText;
    private WeakHashMap<Marker, Object> clusterMarker = new WeakHashMap<>();

    public CustomClusterRenderer(
            AppCompatActivity activity,
            GoogleMap map,
            ClusterManager<CustomClusterItem> clusterManager) {
        super(activity, map, clusterManager);
        iconGenerator = new IconGenerator(activity.getApplicationContext());
        iconGenerator.setBackground(null);
        View clusterLayout = activity.getLayoutInflater().inflate(R.layout.cluster_layout, null);
        iconGenerator.setContentView(clusterLayout);
        clusterImageView = clusterLayout.findViewById(R.id.cluster_image);
        clusterText = clusterLayout.findViewById(R.id.amu_text);
    }

    public void setIcon(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCaptionPreferences(CaptionPreferences captionPreferences) {
        if (captionPreferences.padding != null) {
            final int top = captionPreferences.padding.top;
            clusterText.setPadding(
                    captionPreferences.padding.left,
                    top == -1 ? clusterText.getPaddingTop() : top,
                    captionPreferences.padding.right,
                    captionPreferences.padding.bottom);
        }
        clusterText.setTextSize(captionPreferences.textSize);
        clusterText.setTextColor(Color.parseColor(captionPreferences.textColor));
    }

    public boolean isItAClusterMarker(Marker marker) {
        return clusterMarker.containsKey(marker);
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
        BitmapDescriptor icon = getClusterIcon(cluster);
        if (icon != null) {
            markerOptions.icon(icon);
        } else {
            super.onBeforeClusterRendered(cluster, markerOptions);
        }
    }

    @Override
    protected void onClusterRendered(@NonNull Cluster<CustomClusterItem> cluster, @NonNull Marker marker) {
        clusterMarker.put(marker, this);
    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<CustomClusterItem> cluster, Marker marker) {
        clusterMarker.put(marker, this);
        BitmapDescriptor icon = getClusterIcon(cluster);
        if (icon != null) {
            // Same implementation as onBeforeClusterRendered() (to update cached markers)
            marker.setIcon(icon);
        } else {
            super.onClusterUpdated(cluster, marker);
        }
    }

    private BitmapDescriptor getClusterIcon(Cluster<CustomClusterItem> cluster) {
        if (bitmap == null) return null;
        clusterImageView.setImageBitmap(bitmap);
        Bitmap icon = iconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}
