package com.hemangkumar.capacitorgooglemaps;

import android.util.Size;
import android.util.SizeF;

import com.getcapacitor.JSObject;

class IconDescriptor {
    public final String url;
    public final Size sizeInPixels;
    public final SizeF sizeInMm;

    private IconDescriptor(String url, Size sizeInPixels, SizeF sizeInMm) {
        this.url = url;
        this.sizeInPixels = sizeInPixels;
        this.sizeInMm = sizeInMm;
    }

    public static IconDescriptor createInstance(JSObject jsObject) {
        final JSObject icon = JSObjectDefaults.getJSObjectSafe(jsObject, "icon", new JSObject());
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
}
