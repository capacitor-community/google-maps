package com.hemangkumar.capacitorgooglemaps.model;


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


public class OwnIconRenderer extends DefaultClusterRenderer<CustomMarker> {

    // == fields ==
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
    private final ImageView mClusterImageView;
    private final ImageView mImageView;
    private final int mDimension;

    private Context context;


    // == constructor ==
    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    public OwnIconRenderer(Context context, Activity activity, GoogleMap map,
                           ClusterManager<CustomMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;

        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);


        View multiProfile = activity.getLayoutInflater().inflate(R.layout.multi_profile, null);
        mClusterImageView = multiProfile.findViewById(R.id.image);
        mClusterImageView.setImageResource(R.drawable.cluster_icon);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterIconGenerator.setBackground(null);


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
                .zIndex(0)
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
//        mImageView.setImageResource(ic);
        Bitmap icon = mIconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<CustomMarker> cluster, MarkerOptions markerOptions) {
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        markerOptions.icon(getClusterIcon(cluster));
        markerOptions.zIndex(1);
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
        //getting number of items in cluster
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }


}
