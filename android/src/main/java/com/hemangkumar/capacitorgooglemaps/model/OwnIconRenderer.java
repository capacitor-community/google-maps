package com.hemangkumar.capacitorgooglemaps.model;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.google.maps.android.ui.SquareTextView;
import com.hemangkumar.capacitorgooglemaps.capacitorgooglemaps.R;


public class OwnIconRenderer extends DefaultClusterRenderer<CustomMarker> {

    // == fields ==
    private final IconGenerator mIconGenerator;
    private final IconGenerator mClusterIconGenerator;
//    private final ImageView mImageView;
    private final ImageView mClusterImageView;
//    private final int mDimension;

    private Context context;


    private final float mDensity;


    // == constructor ==
    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    public OwnIconRenderer(Context context, Activity activity, GoogleMap map,
                           ClusterManager<CustomMarker> clusterManager) {
        super(context, map, clusterManager);

        this.context = context;

        mDensity = context.getResources().getDisplayMetrics().density;

        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);

//        mIconGenerator.setContentView(makeSquareTextView(context));
//        mIconGenerator.setTextAppearance(R.style.amu_ClusterIcon_TextAppearance);

        View multiProfile = activity.getLayoutInflater().inflate(R.layout.multi_profile, null);
        mClusterIconGenerator.setContentView(multiProfile);
        mClusterIconGenerator.setBackground(null);

        mClusterImageView = multiProfile.findViewById(R.id.image);

//        mImageView = new ImageView(context);
//        mDimension = (int) context.getResources().getDimension(R.dimen.custom_profile_image);
//        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
//        int padding = (int) context.getResources().getDimension(R.dimen.custom_profile_padding);
//        mImageView.setPadding(padding, padding, padding, padding);
//        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(@NonNull CustomMarker person,
                                               MarkerOptions markerOptions) {
        // Draw a single person - show their profile photo and set the info window to show their name
        markerOptions
//                .icon(getItemIcon(person))
                .title(person.getTitle());
    }

    @Override
    protected void onClusterItemUpdated(@NonNull CustomMarker person, Marker marker) {
        // Same implementation as onBeforeClusterItemRendered() (to update cached markers)
//        marker.setIcon(getItemIcon(person));
        marker.setTitle(person.getTitle());
    }

    /**
     * Get a descriptor for a single person (i.e., a marker outside a cluster) from their
     * profile photo to be used for a marker icon
     *
     * @param person person to return an BitmapDescriptor for
     * @return the person's profile photo as a BitmapDescriptor
     */
    private BitmapDescriptor getItemIcon(CustomMarker person) {
//        mImageView.setImageResource(person.profilePhoto);
        Bitmap icon = mIconGenerator.makeIcon();
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected void onBeforeClusterRendered(@NonNull Cluster<CustomMarker> cluster, MarkerOptions markerOptions) {
        // Draw multiple people.
        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
        markerOptions.icon(getClusterIcon(cluster));
    }

    @Override
    protected void onClusterUpdated(@NonNull Cluster<CustomMarker> cluster, Marker marker) {
        // Same implementation as onBeforeClusterRendered() (to update cached markers)
        marker.setIcon(getClusterIcon(cluster));
    }

    /**
     * Get a descriptor for multiple people (a cluster) to be used for a marker icon. Note: this
     * method runs on the UI thread. Don't spend too much time in here (like in this example).
     *
     * @param cluster cluster to draw a BitmapDescriptor for
     * @return a BitmapDescriptor representing a cluster
     */
    private BitmapDescriptor getClusterIcon(Cluster<CustomMarker> cluster) {
//        List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));
//        int width = mDimension;
//        int height = mDimension;
//
//        for (Person p : cluster.getItems()) {
//            // Draw 4 at most.
//            if (profilePhotos.size() == 4) break;
//            Drawable drawable = context.getResources().getDrawable(p.profilePhoto);
//            drawable.setBounds(0, 0, width, height);
//            profilePhotos.add(drawable);
//        }
//        MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
//        multiDrawable.setBounds(0, 0, width, height);
//
//        mClusterImageView.setImageDrawable(multiDrawable);
//        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//        return BitmapDescriptorFactory.fromBitmap(icon);

        mClusterImageView.setImageResource(R.drawable.cluster);
        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        return BitmapDescriptorFactory.fromBitmap(icon);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }



    private SquareTextView makeSquareTextView(Context context) {
        SquareTextView squareTextView = new SquareTextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(R.id.amu_text);
        int twelveDpi = (int) (12 * mDensity);
        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        return squareTextView;
    }

}
