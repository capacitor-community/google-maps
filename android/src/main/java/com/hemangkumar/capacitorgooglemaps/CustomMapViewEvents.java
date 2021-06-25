package com.hemangkumar.capacitorgooglemaps;

import com.getcapacitor.JSObject;

public interface CustomMapViewEvents {
    void onMapReady(String callbackId, JSObject result);

    void resultForCallbackId(String callbackId, JSObject result);
}
