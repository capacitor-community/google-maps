package com.hemangkumar.capacitorgooglemaps;

import android.app.Activity;

import androidx.core.util.Consumer;

import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarkersAppender {

    public void addMarkers(final CustomMapView customMapView,
                           final JSArray jsMarkers,
                           final Activity activity,
                           Consumer<JSObject> resultConsumer) {
        final BlockingQueue<CustomMarker> customMarkers = new LinkedBlockingQueue<>(
                Runtime.getRuntime().availableProcessors() * 2);
        createCustomMarkers(jsMarkers, customMarkers);
        addCustomMarkers(customMarkers, jsMarkers.length(), customMapView, activity, resultConsumer);
    }

    private void createCustomMarkers(JSArray jsMarkers, BlockingQueue<CustomMarker> customMarkers) {
        int n = jsMarkers.length();
        AtomicBoolean shouldStop = new AtomicBoolean(false);

        // prepare customMarkers as fast as possible. Really it doesn't increase the total
        // speed of this method :( noticeably.
        // todo: Maybe newWorkStealingPool? But it requires API Level 24.
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() - 1);
        for (int i = 0; i < n; i++) {
            final int index = i;
            if (shouldStop.get()) {
                break;
            }
            executorService.execute(() -> {
                try {
                    JSONObject jsonObject = (JSONObject) jsMarkers.get(index);
                    JSObject jsObject = JSObject.fromJSONObject(jsonObject);
                    CustomMarker customMarker = new CustomMarker();
                    customMarker.updateFromJSObject(jsObject);
                    customMarkers.put(customMarker);
                } catch (JSONException | InterruptedException exception) {
                    shouldStop.set(true);
                }
            });
        }
    }

    private void addCustomMarkers(final BlockingQueue<CustomMarker> customMarkers,
                                  int n,
                                  final CustomMapView customMapView,
                                  final Activity activity,
                                  Consumer<JSObject> resultConsumer) {
        List<JSObject> result = new ArrayList<>(n);
        AtomicBoolean isMarkerAdded = new AtomicBoolean(false);
        Object syncRoot = new Object();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            try {
                for (int i = 0; i < n; i++) {
                    final CustomMarker customMarker = customMarkers.take();
                    activity.runOnUiThread(() -> customMapView.addMarker(
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
                            }
                    )); // end of runOnUiThread
                    synchronized (syncRoot) {
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
                    }
                } // end for
            } catch (InterruptedException ignored) {
                // nothing
            }
            activity.runOnUiThread(() -> {
                JSObject jsResult = new JSObject();
                jsResult.put("mapId", customMapView.getId());
                JSArray jsMarkerOutputEntries = JSArray.from(result.toArray());
                jsResult.put("markers", jsMarkerOutputEntries);
                resultConsumer.accept(jsResult);
            });
        }); // end of execute
    }
}
