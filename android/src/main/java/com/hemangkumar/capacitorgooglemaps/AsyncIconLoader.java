package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.text.TextUtils;

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

    public interface OnIconReady {
        void onReady(@Nullable Bitmap bitmap);
    }

    private final IconDescriptor iconDescriptor;
    private final FragmentActivity activity;

    public AsyncIconLoader(@Nullable IconDescriptor iconDescriptor, FragmentActivity activity) {
        this.iconDescriptor = iconDescriptor;
        this.activity = activity;
    }

    public void load(OnIconReady onIconReady) {

        if (iconDescriptor == null || TextUtils.isEmpty(iconDescriptor.url)) {
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
                .load(iconDescriptor.url)
                .timeout(3000);
        scaleImageOptional(builder).into(
                new CustomTarget<Bitmap>() {
                    // It will be called when the resource load has finished.
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
        Glide.with(activity).downloadOnly().load(iconDescriptor.url).into(new CustomTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                try {
                    try (InputStream inputStream = new FileInputStream(resource)) {
                        SVG svg = SVG.getFromInputStream(inputStream);
                        if (iconDescriptor.isSizeSet()) {
                            svg.setDocumentWidth(iconDescriptor.size.getWidth());
                            svg.setDocumentHeight(iconDescriptor.size.getHeight());
                        }
                        Picture picture = svg.renderToPicture();
                        Bitmap bitmap = pictureToBitmap(picture);
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
        if (iconDescriptor.isSizeSet()) {
            builder = builder.override(iconDescriptor.size.getWidth(), iconDescriptor.size.getHeight());
        }
        return builder;
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
