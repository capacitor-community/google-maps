package com.hemangkumar.capacitorgooglemaps;

import android.util.Size;
import android.util.SizeF;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;

class IconDescriptor {
    public final String url;
    public final Size sizeInPixels;
    public final SizeF sizeInMm;

    /**
     * Source of JSObject:
     * {
     *    url: 'https://www.google.com/favicon.ico',
     *      targetSizePx: {
     *        width: 64,
     *        height: 64
     *   }
     * }
     * @param jsIcon is a JSObject icon representation
     * @return IconDescriptor
     */
    public IconDescriptor(@NonNull final JSObject jsIcon) {
        url = jsIcon.getString("url", "");

        final JSObject jsSizeInPixels = JSObjectDefaults.getJSObjectSafe(
                jsIcon,
                "targetSizePx",
                new JSObject());

        final JSObject jsSizeInMm = JSObjectDefaults.getJSObjectSafe(
                jsIcon,
                "targetSizeMm",
                new JSObject());

        final String width = "width";
        final String height = "height";

        sizeInPixels = new Size(
                (int) Math.round(jsSizeInPixels.optDouble(width, 0)),
                (int) Math.round(jsSizeInPixels.optDouble(height, 0)));

        sizeInMm = new SizeF(
                (float) jsSizeInMm.optDouble(width, 0),
                (float) jsSizeInMm.optDouble(height, 0));
    }
}
