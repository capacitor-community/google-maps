package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.SizeF;

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

    private static class IconDescriptor {
        public final String url;
        public final Size sizeInPixels;
        public final SizeF sizeInMm;

        public IconDescriptor(String url, Size sizeInPixels, SizeF sizeInMm) {
            this.url = url;
            this.sizeInPixels = sizeInPixels;
            this.sizeInMm = sizeInMm;
        }
    }

    private final IconDescriptor iconDescriptor;
    private final FragmentActivity activity;

    public AsyncIconLoader(final JSObject markerData, FragmentActivity activity) {
        iconDescriptor = getIconDescriptor(markerData);
        this.activity = activity;
    }

    private IconDescriptor getIconDescriptor(JSObject markerData) {
        final JSObject icon = JSObjectDefaults.getJSObjectSafe(markerData, "icon", new JSObject());
        final String url = icon.getString("url", "");

        final JSObject jsSizeInPixels = JSObjectDefaults.getJSObjectSafe(
                icon,
                "target_size_px",
                new JSObject());

        final JSObject jsSizeInMm = JSObjectDefaults.getJSObjectSafe(
                icon,
                "target_size_mm",
                new JSObject());

        final String width = "width";
        final String height = "height";

        Size sizeInPixels = new Size(
                (int) Math.round(jsSizeInPixels.optDouble(width, 0)),
                (int) Math.round(jsSizeInPixels.optDouble(height, 0)));

        SizeF sizeInMm = new SizeF(
                (float) jsSizeInMm.optDouble(width, 0),
                (float) jsSizeInMm.optDouble(height, 0));

        return new IconDescriptor(url, sizeInPixels, sizeInMm);
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
