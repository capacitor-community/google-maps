package com.hemangkumar.capacitorgooglemaps;

import android.util.Size;
import android.util.SizeF;

class IconDescriptor {
    public final String url;
    public final Size sizeInPixels;
    public final SizeF sizeInMm;

    public IconDescriptor(String url, Size sizeInPixels, SizeF sizeInMm) {
        this.url = url;
        this.sizeInPixels = sizeInPixels;
        this.sizeInMm = sizeInMm;
    }
}
