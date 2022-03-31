package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.getcapacitor.JSObject;
import com.google.android.libraries.maps.model.BitmapDescriptor;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;

class AsyncIconLoader {

    public interface OnIconReady {
        void onReady(@Nullable BitmapDescriptor bitmapDescriptor);
    }

    private final IconDescriptor iconDescriptor;
    private final FragmentActivity activity;

    public AsyncIconLoader(final JSObject markerData, FragmentActivity activity) {
        iconDescriptor = IconDescriptor.createInstance(markerData);
        this.activity = activity;
    }


    public void load(@NonNull OnIconReady onIconReady) {

        if (TextUtils.isEmpty(iconDescriptor.url)) {
            onIconReady.onReady(null);
            return;
        }

        RequestBuilder<Bitmap> builder = Glide.with(activity)
                .asBitmap()
                .load(iconDescriptor.url);

        scaleBitmapOptional(builder).into(
                new CustomTarget<Bitmap>() {
                    // It will be called when the resource load has finished.
                    @Override
                    public void onResourceReady(
                            @NonNull Bitmap bitmap,
                            @Nullable Transition<? super Bitmap> transition) {
                        onIconReady.onReady(BitmapDescriptorFactory.fromBitmap(bitmap));
                    }

                    // It is called when a load is cancelled and its resources are freed.
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Show default marker
                        onIconReady.onReady(null);
                    }

                    // It is called when can't get image from network AND from a local cache.
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Show default marker
                        onIconReady.onReady(null);
                    }
                }
        );
    }

    private RequestBuilder<Bitmap> scaleBitmapOptional(RequestBuilder<Bitmap> builder) {
        if (iconDescriptor.sizeInMm.getHeight() > 0 && iconDescriptor.sizeInMm.getWidth() > 0) {
            // Scale image to provided size in Millimeters
            final float mmPerInch = 25.4f;
            DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
            int newWidthPixels = (int) (iconDescriptor.sizeInMm.getWidth() * metrics.xdpi / mmPerInch);
            int newHeightPixels = (int) (iconDescriptor.sizeInMm.getWidth() * metrics.ydpi / mmPerInch);
            builder = builder.override(newWidthPixels, newHeightPixels).optionalFitCenter();
        } else if (iconDescriptor.sizeInPixels.getHeight() > 0 && iconDescriptor.sizeInPixels.getWidth() > 0) {
            // Scale image to provided size in Pixels
            builder = builder.override(iconDescriptor.sizeInPixels.getWidth(),
                    iconDescriptor.sizeInPixels.getHeight()).optionalFitCenter();
        }
        return builder;
    }
}
