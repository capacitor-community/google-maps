package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Executors;
import com.getcapacitor.JSObject;

import java.util.Locale;

class AsyncIconLoader {

    public interface OnIconReady {
        void onReady(@Nullable Bitmap bitmap);
    }

    private final IconDescriptor iconDescriptor;
    private final FragmentActivity activity;

    public AsyncIconLoader(JSObject jsIconDescriptor, FragmentActivity activity) {
        this.iconDescriptor = IconDescriptor.createInstance(jsIconDescriptor);
        this.activity = activity;
    }

    public void load(OnIconReady onIconReady) {

        if (TextUtils.isEmpty(iconDescriptor.url)) {
            onIconReady.onReady(null);
            return;
        }

        if (iconDescriptor.url.toLowerCase(Locale.ROOT).endsWith(".svg")) {
            loadSvg(onIconReady);
        } else {
            loadBitmap(onIconReady);
        }
    }

    private void loadBitmap(OnIconReady onIconReady) {
        RequestBuilder<Bitmap> builder = Glide.with(activity)
                .asBitmap()
                .load(iconDescriptor.url);

        scaleImageOptional(builder, iconDescriptor).into(
                new CustomTarget<Bitmap>() {
                    // It will be called when the resource loadAll has finished.
                    @Override
                    public void onResourceReady(
                            @NonNull Bitmap bitmap,
                            @Nullable Transition<? super Bitmap> transition) {
                        onIconReady.onReady(bitmap);
                    }

                    // It is called when a loadAll is cancelled and its resources are freed.
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Use default marker
                        onIconReady.onReady(null);
                    }

                    // It is called when can't get image from network AND from a local cache.
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Use default marker
                        onIconReady.onReady(null);
                    }
                }
        );
    }

    private void loadSvg(OnIconReady onIconReady) {
        RequestBuilder<PictureDrawable> builder = Glide.with(activity)
                .as(PictureDrawable.class)
                .load(iconDescriptor.url);

        scaleImageOptional(builder, iconDescriptor)
                .into(new CustomTarget<PictureDrawable>() {
                    @Override
                    public void onResourceReady(@NonNull PictureDrawable pictureDrawable,
                                                @Nullable Transition<? super PictureDrawable> transition) {
                        onIconReady.onReady(pictureDrawableToBitmap(pictureDrawable));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Use default marker
                        onIconReady.onReady(null);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        // Use default marker
                        onIconReady.onReady(null);
                    }
                });
    }

    private <T> RequestBuilder<T> scaleImageOptional(
            RequestBuilder<T> builder,
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

    private static Bitmap pictureDrawableToBitmap(PictureDrawable pictureDrawable) {
        Bitmap bmp = Bitmap.createBitmap(
                pictureDrawable.getIntrinsicWidth(),
                pictureDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bmp;
    }
}
