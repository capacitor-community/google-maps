package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.getcapacitor.JSObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

class AsyncIconLoader {

    private static final int PICTURE_DOWNLOAD_TIMEOUT = 3000;
    private static final int FAST_CACHE_SIZE_ENTRIES = 32;

    private static final LruCache<String, Bitmap> bitmapCache = new LruCache<>(FAST_CACHE_SIZE_ENTRIES);

    public interface OnIconReady {
        void onReady(@Nullable Bitmap bitmap);
    }

    private final IconDescriptor iconDescriptor;
    private final FragmentActivity activity;

    public AsyncIconLoader(JSObject jsIconDescriptor, FragmentActivity activity) {
        this.iconDescriptor = new IconDescriptor(jsIconDescriptor);
        this.activity = activity;
    }

    public void load(OnIconReady onIconReady) {

        if (iconDescriptor == null || TextUtils.isEmpty(iconDescriptor.url)) {
            onIconReady.onReady(null);
            return;
        }
        String url = iconDescriptor.url.toLowerCase(Locale.ROOT);
        Bitmap cachedBitmap = bitmapCache.get(url);
        if (cachedBitmap != null) {
            onIconReady.onReady(cachedBitmap);
            return;
        }
        if (url.endsWith(".svg")) {
            loadSvg(onIconReady);
        } else {
            loadBitmap(onIconReady);
        }
    }

    private void loadBitmap(final OnIconReady onIconReady) {
        RequestBuilder<Bitmap> builder = Glide.with(activity)
                .asBitmap()
                .load(iconDescriptor.url)
                .timeout(PICTURE_DOWNLOAD_TIMEOUT);
        scaleImageOptional(builder).into(
                new CustomTarget<Bitmap>() {
                    // It will be called when the resource load has finished.
                    @Override
                    public void onResourceReady(
                            @NonNull Bitmap bitmap,
                            @Nullable Transition<? super Bitmap> transition) {
                        bitmapCache.put(iconDescriptor.url, bitmap);
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

    private void loadSvg(final OnIconReady onIconReady) {
        Glide.with(activity).downloadOnly().load(iconDescriptor.url).into(new CustomTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                try {
                    try (InputStream inputStream = new FileInputStream(resource)) {
                        SVG svg = SVG.getFromInputStream(inputStream);
                        svg.setDocumentWidth(iconDescriptor.size.getWidth());
                        svg.setDocumentHeight(iconDescriptor.size.getHeight());
                        Picture picture = svg.renderToPicture();
                        Bitmap bitmap = pictureToBitmap(picture);
                        bitmapCache.put(iconDescriptor.url, bitmap);
                        onIconReady.onReady(bitmap);
                    }
                } catch (IOException | SVGParseException exception) {
                    onIconReady.onReady(null);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                onIconReady.onReady(null);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                onIconReady.onReady(null);
            }
        });
    }

    private <T> RequestBuilder<T> scaleImageOptional(
            RequestBuilder<T> builder) {
        return builder.override(iconDescriptor.size.getWidth(), iconDescriptor.size.getHeight());
    }

    private static Bitmap pictureToBitmap(Picture picture) {
        PictureDrawable pictureDrawable = new PictureDrawable(picture);
        return pictureDrawableToBitmap(pictureDrawable);
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
