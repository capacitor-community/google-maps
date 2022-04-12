package com.hemangkumar.capacitorgooglemaps;

import com.getcapacitor.JSObject;

class CaptionPreferences {
    public static class Padding {
        public final int left;
        public final int top;
        public final int right;
        public final int bottom;

        public Padding(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public final int textSize;
    public final String textColor;
    public final Padding padding;

    public CaptionPreferences(JSObject jsCaptionPreferences) {
        JSObject jsPadding = JSObjectDefaults.getJSObjectSafe(
                jsCaptionPreferences, "padding", null);
        if (jsPadding != null) {
            int left = JSObjectDefaults.getIntegerSafe(jsPadding, "left", 5);
            int top = JSObjectDefaults.getIntegerSafe(jsPadding, "top", 0);
            int right = JSObjectDefaults.getIntegerSafe(jsPadding, "right", 5);
            int bottom = JSObjectDefaults.getIntegerSafe(jsPadding, "bottom", 16);
            padding = new Padding(left, top, right, bottom);
        } else {
            padding = null;
        }
        textSize = JSObjectDefaults.getIntegerSafe(jsCaptionPreferences, "textSize", 16);
        textColor = jsCaptionPreferences.getString("textColor", "#000000");
    }
}
