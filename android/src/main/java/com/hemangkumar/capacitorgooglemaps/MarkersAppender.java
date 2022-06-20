package com.hemangkumar.capacitorgooglemaps;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;

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

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private Throwable currentException = null;
    private final AtomicBoolean isException = new AtomicBoolean(false);

    public void addMarkers(final CustomMapView customMapView,
                           final JSArray jsMarkers,
                           final Activity activity,
                           Consumer<JSObject> resultConsumer) throws AppenderException {
        final List<CustomMarker> customMarkers = createCustomMarkers(activity, jsMarkers);
        addCustomMarkers(customMarkers, customMapView, resultConsumer);
    }

    private List<CustomMarker> createCustomMarkers(final Activity activity, final JSArray jsMarkers) throws AppenderException {
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
                    customMarker.asyncLoadIcon(activity, (Void) -> {
                        synchronized (customMarkers) {
                            customMarkers.add(customMarker);
                        }
                        if (nMarkersCounter.addAndGet(1) == n) {
                            synchronized (syncRoot) {
                                syncRoot.notify();
                            }
                        }
                    });
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
                                  Consumer<JSObject> resultConsumer) {
        final int n = customMarkers.size();
        final AtomicInteger nMarkersAdded = new AtomicInteger(0);

        for (CustomMarker customMarker : customMarkers) {
            executorService.execute(() -> {
                int i = nMarkersAdded.addAndGet(1);

                int delayRandomized = (int)(Math.random() * 25) * 10;

                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    customMapView.addMarker(customMarker);
                }, delayRandomized);

                if (i == n) {
                    resultConsumer.accept(null);
                }
            });


        }
    }
}
