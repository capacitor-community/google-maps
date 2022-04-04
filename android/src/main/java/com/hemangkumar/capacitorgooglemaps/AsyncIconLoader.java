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

import java.util.concurrent.atomic.AtomicInteger;

class AsyncIconLoader {

    public interface OnIconReady {
        void onReady(@Nullable Bitmap bitmap, boolean allIconsAreLoaded);
    }

    private final IconDescriptor[] iconDescriptors;
    private final FragmentActivity activity;
    private final AtomicInteger nLoadedIcons = new AtomicInteger(0);

    public AsyncIconLoader(final JSObject[] icons, FragmentActivity activity) {
        iconDescriptors = new IconDescriptor[icons.length];
        for (int i = 0; i < icons.length; i++) {
            iconDescriptors[i] = IconDescriptor.createInstance(icons[i]);
        }
        this.activity = activity;
    }

    public AsyncIconLoader(final JSObject icon, FragmentActivity activity) {
        this(new JSObject[]{icon}, activity);
    }

    public void load(@NonNull OnIconReady onIconReady) {
        nLoadedIcons.set(iconDescriptors.length);
        for (IconDescriptor iconDescriptor : iconDescriptors) {
            loadOneIcon(iconDescriptor, onIconReady);
        }
    }

    private void loadOneIcon(IconDescriptor iconDescriptor, OnIconReady onIconReady) {

        if (TextUtils.isEmpty(iconDescriptor.url)) {
            onReady(null, onIconReady);
            return;
        }

        RequestBuilder<Bitmap> builder = Glide.with(activity)
                .asBitmap()
                .load(iconDescriptor.url);

        scaleBitmapOptional(builder, iconDescriptor).into(
                new CustomTarget<Bitmap>() {
                    // It will be called when the resource loadAll has finished.
                    @Override
                    public void onResourceReady(
                            @NonNull Bitmap bitmap,
                            @Nullable Transition<? super Bitmap> transition) {
                        onReady(bitmap, onIconReady);
                    }

                    // It is called when a loadAll is cancelled and its resources are freed.
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Use default marker
                        onReady(null, onIconReady);
                    }

                    // It is called when can't get image from network AND from a local cache.
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Use default marker
                        onReady(null, onIconReady);
                    }
                }
        );
    }

    private void onReady(Bitmap bitmap, OnIconReady onIconReady) {
        onIconReady.onReady(
                bitmap,
                nLoadedIcons.addAndGet(-1) <= 0);
    }

    private RequestBuilder<Bitmap> scaleBitmapOptional(
            RequestBuilder<Bitmap> builder,
            IconDescriptor iconDescriptor) {
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
