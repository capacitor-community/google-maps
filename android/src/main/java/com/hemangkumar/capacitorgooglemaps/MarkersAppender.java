package com.hemangkumar.capacitorgooglemaps;

import android.app.Activity;

import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MarkersAppender {

    public static class AppenderException extends Exception {
        public AppenderException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private final Object syncRoot = new Object();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Throwable currentException = null;
    private final AtomicBoolean isException = new AtomicBoolean(false);

    public void addMarkers(final CustomMapView customMapView,
                           final JSArray jsMarkers,
                           final Activity activity,
                           Consumer<JSObject> resultConsumer) throws AppenderException {
        final List<CustomMarker> customMarkers = createCustomMarkers(jsMarkers);
        addCustomMarkers(customMarkers, customMapView, activity, resultConsumer);
    }

    private List<CustomMarker> createCustomMarkers(final JSArray jsMarkers) throws AppenderException {
        final int n = jsMarkers.length();
        final List<CustomMarker> customMarkers = new ArrayList<>(n);
        final Object syncRoot = new Object();
        final AtomicInteger nMarkersCounter = new AtomicInteger(0);
        isException.set(false);
        currentException = null;
        // prepare customMarkers as fast as possible. Really it doesn't increase the total
        // speed of this method :( noticeably.
        for (int i = 0; i < n; i++) {
            final int fi = i;
            if (isException.get()) {
                break;
            }
            executorService.execute(() -> {
                try {
                    JSONObject jsonObject = (JSONObject) jsMarkers.get(fi);
                    JSObject jsObject = JSObject.fromJSONObject(jsonObject);
                    CustomMarker customMarker = new CustomMarker();
                    customMarker.updateFromJSObject(jsObject);
                    synchronized (customMarkers) {
                        customMarkers.add(customMarker);
                    }
                    if (nMarkersCounter.addAndGet(1) == n) {
                        synchronized (syncRoot) {
                            syncRoot.notify();
                        }
                    }
                } catch (JSONException exception) {
                    currentException = exception;
                    isException.set(true);
                    synchronized (syncRoot) {
                        syncRoot.notify();
                    }
                }
            });
        }

        synchronized (syncRoot) {
            try {
                // Wait for customMarkers are populated
                // I follow https://www.baeldung.com/java-wait-notify#1-why-enclose-wait-in-a-while-loop
                while (nMarkersCounter.get() < n && !isException.get()) {
                    syncRoot.wait();
                }
            } catch (InterruptedException ignored) {
            }
        }

        if (isException.get()) {
            throw new AppenderException("exception in createCustomMarkers", currentException);
        }
        return customMarkers;
    }

    private void addCustomMarkers(final List<CustomMarker> customMarkers,
                                  final CustomMapView customMapView,
                                  final Activity activity,
                                  Consumer<JSObject> resultConsumer) {
        final int n = customMarkers.size();
        final List<JSObject> result = new ArrayList<>(n);
        final AtomicInteger nMarkersAdded = new AtomicInteger(0);
        final AtomicBoolean isMarkerAdded = new AtomicBoolean(false);

        executorService.execute(() -> {
            for (CustomMarker customMarker : customMarkers) {
                activity.runOnUiThread(() -> {
                    customMapView.addMarker(
                            customMarker,
                            (marker) -> {
                                result.add(
                                        (JSObject) CustomMarker.getResultForMarker(
                                                marker,
                                                customMapView.getId())
                                                .opt("marker")
                                );
                                synchronized (syncRoot) {
                                    isMarkerAdded.set(true);
                                    syncRoot.notify();
                                }
                                if (nMarkersAdded.addAndGet(1) == n) {
                                    JSObject jsResult = new JSObject();
                                    jsResult.put("mapId", customMapView.getId());
                                    JSArray jsMarkerOutputEntries = JSArray.from(result.toArray());
                                    jsResult.put("markers", jsMarkerOutputEntries);
                                    resultConsumer.accept(jsResult);
                                }
                            }
                    );
                }); // end of runOnUiThread
                synchronized (syncRoot) {
                    try {
                        // wait for Marker is rendered before the next iteration
                        // here is a background thread -> No UI freeze
                        while (!isMarkerAdded.get()) {
                            syncRoot.wait();
                            if (!isMarkerAdded.get()) {
                                continue;
                            }
                            isMarkerAdded.set(false);
                            break;
                        }
                    } catch (InterruptedException ignored) {
                        break;
                    }
                }
            } // end for
        }); // end of execute
    }
}
