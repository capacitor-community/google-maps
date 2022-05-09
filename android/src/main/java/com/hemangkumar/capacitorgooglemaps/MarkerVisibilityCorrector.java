package com.hemangkumar.capacitorgooglemaps;

import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.Marker;
import com.google.maps.android.PolyUtil;

import java.util.HashMap;
import java.util.Map;

public class MarkerVisibilityCorrector {
    private static final double EARTH_RADIUS = 6371.00; // Radius in Kilometers default
    private final Map<String, Marker> markers;
    private final Map<String, ShapePolygon> polygons;
    private final Map<String, ShapeCircle> circles;
    private final Map<String, Boolean> savedMarkerVisibilities = new HashMap<>();

    public MarkerVisibilityCorrector(final Map<String, Marker> markers,
                                     final Map<String, ShapePolygon> polygons,
                                     final Map<String, ShapeCircle> circles) {
        this.markers = markers;
        this.polygons = polygons;
        this.circles = circles;
    }

    public void clear() {
        savedMarkerVisibilities.clear();
    }

    public void remove(Marker marker) {
        savedMarkerVisibilities.remove(marker.getId());
    }

    public void correctMarkerVisibility() {
        for (Marker marker : markers.values()) {
            correctMarkerVisibility(marker);
        }
    }

    public void correctMarkerVisibility(Marker marker) {
        updateVisibility(isCoveredWithShape(marker), marker);
    }

    private boolean isCoveredWithShape(Marker marker) {
        for (Map.Entry<String, ShapePolygon> polygonEntry : polygons.entrySet()) {
            ShapePolygon polygon = polygonEntry.getValue();
            if (polygon.isAboveMarkers() &&
                    (PolyUtil.containsLocation(marker.getPosition(), polygon.getPoints(), polygon.isGeodesic())
                            || PolyUtil.isLocationOnEdge(marker.getPosition(), polygon.getPoints(), polygon.isGeodesic()))) {
                if (polygon.isVisible()) {
                    return true;
                }
            }
        }
        for (ShapeCircle circle : circles.values()) {
            if (circle.isAboveMarkers() &&
                    (calculateDistance(circle.getCenter(), marker.getPosition()) <= circle.getRadius())) {
                if (circle.isVisible()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateVisibility(boolean shouldHide, Marker marker) {
        if (shouldHide) {
            if (!savedMarkerVisibilities.containsKey(marker.getId())) {
                savedMarkerVisibilities.put(marker.getId(), marker.isVisible());
            }
            marker.setVisible(false);
        } else {
            Boolean visibility = savedMarkerVisibilities.remove(marker.getId());
            if (visibility != null) {
                marker.setVisible(visibility);
            }
        }
    }

    private static Double calculateDistance(LatLng p1, LatLng p2) {
        // http://www.codecodex.com/wiki/Calculate_Distance_Between_Two_Points_on_a_Globe
        double dLat = toRadians(p2.latitude - p1.latitude);
        double dLon = toRadians(p2.longitude - p1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(p1.latitude)) * Math.cos(toRadians(p2.latitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    private static double toRadians(double degree) {
        // Value degree * Pi/180
        return degree * Math.PI / 180;
    }
}
