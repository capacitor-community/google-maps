package com.hemangkumar.capacitorgooglemaps.marker;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.hemangkumar.capacitorgooglemaps.capacitorgooglemaps.R;

import java.util.ArrayList;
import java.util.List;


public class OwnIconRenderer extends DefaultClusterRenderer<CustomMarker> {

    // -- constants --
    public static final int MIN_COUNT_ELEMENTS_IN_CLUSTER = 2;


    // cluster sizes must be equal buckets count
    private static final int[] CLUSTER_SIZES = { 64, 68, 72, 76, 80, 84, 88, 92 };
    private static final int[] BUCKETS =        { 5, 10, 15, 20, 25, 30, 35, 40 };


    // == fields ==
    private final IconGenerator mIconGenerator;
    private final ImageView mImageView;
    private final int mDimension;

    private Context context;
    private GoogleMap map;

    private List<IconGenerator> mClusterIconGenerators = new ArrayList<>();

    // == constructor ==
    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    public OwnIconRenderer(Context context, Activity activity, GoogleMap map,
                           ClusterManager<CustomMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.map = map;

        mIconGenerator = new IconGenerator(context);

        final float scale = context.getResources().getDisplayMetrics().density;

        for (int i = 0; i < BUCKETS.length; i++) {
            int dps = CLUSTER_SIZES[i];
            int pixels = (int) (dps * scale + 0.5f);

            View multiProfile = activity.getLayoutInflater().inflate(R.layout.multi_profile, null);
            ImageView mClusterImageView = multiProfile.findViewById(R.id.image);
            mClusterImageView.setImageResource(R.drawable.cluster_icon);

            multiProfile.setLayoutParams(new ViewGroup.LayoutParams(pixels, pixels));

            IconGenerator mClusterIconGenerator = new IconGenerator(context);

            mClusterIconGenerator.setContentView(multiProfile);
            mClusterIconGenerator.setBackground(null);

            mClusterIconGenerators.add(mClusterIconGenerator);
        }

        mImageView = new ImageView(context);
        mDimension = (int) context.getResources().getDimension(R.dimen.custom_profile_image);
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        int padding = (int) context.getResources().getDimension(R.dimen.custom_profile_padding);
        mImageView.setPadding(padding, padding, padding, padding);
        mIconGenerator.setContentView(mImageView);
        mIconGenerator.setBackground(null);

    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull CustomMarker customMarker,
                                               MarkerOptions markerOptions) {
        // Draw a single marker - set the info window to show their name
        markerOptions
                .icon(getItemIcon(customMarker))
                .title(customMarker.getTitle());
    }


    @Override
    protected void onClusterItemUpdated(@NonNull CustomMarker customMarker, Marker marker) {
        // Same implementation as onBeforeClusterItemRendered() (to update cached markers)
        marker.setIcon(getItemIcon(customMarker));
        marker.setTitle(customMarker.getTitle());
    }


    private BitmapDescriptor getItemIcon(CustomMarker customMarker) {
        if(customMarker.getMarkerCategory().getIcon() == null) {
            return null;
        }


        mImageView.setImageBitmap(customMarker.getMarkerCategory().getIcon());
        Bitmap icon = mIconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<CustomMarker> cluster, MarkerOptions markerOptions) {
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        markerOptions
                .zIndex((int) map.getCameraPosition().zoom)
                .icon(getClusterIcon(cluster));
    }


    @Override
    protected void onClusterUpdated(@NonNull Cluster<CustomMarker> cluster, Marker marker) {
        // Same implementation as onBeforeClusterRendered() (to update cached markers)
        marker.setIcon(getClusterIcon(cluster));
    }

    /**
     * Note: this
     * method runs on the UI thread. Don't spend too much time in here (like in this example).
     *
     * @param cluster cluster to draw a BitmapDescriptor for
     * @return a BitmapDescriptor representing a cluster
     */
    private BitmapDescriptor getClusterIcon(Cluster<CustomMarker> cluster) {
        int index = getBucket(cluster.getSize());
        Bitmap icon = mClusterIconGenerators.get(index).makeIcon(String.valueOf(cluster.getSize()));
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    /**
     * Gets the "bucket" for a particular cluster. By default, uses the number of
     * points within the
     * cluster, bucketed to some set points.
     */
    private int getBucket(@NonNull int sizeOfCluster) {
        int index = 0;
        while ((index+1 < BUCKETS.length) && (BUCKETS[index+1] <= sizeOfCluster)) {
            index++;
        }
        return index;
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() >= MIN_COUNT_ELEMENTS_IN_CLUSTER;
    }

}