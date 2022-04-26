package com.hemangkumar.capacitorgooglemaps;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.fragment.app.FragmentActivity;

import com.google.android.libraries.maps.GoogleMap;
import com.google.android.libraries.maps.Projection;
import com.google.android.libraries.maps.model.BitmapDescriptorFactory;
import com.google.android.libraries.maps.model.GroundOverlay;
import com.google.android.libraries.maps.model.GroundOverlayOptions;
import com.google.android.libraries.maps.model.LatLng;
import com.google.android.libraries.maps.model.LatLngBounds;
import com.google.android.libraries.maps.model.Polygon;

import java.util.ArrayList;
import java.util.List;

public class CustomPolygon extends CustomShape<ShapePolygon> {
    private static final int OVERLAY_MAX_SIZE = 512;
    private final ShapePolygonTraits traits = new ShapePolygonTraits();
    private ShapePolygonOptions options;

    @Override
    protected ShapeOptions getOptions() {
        return options;
    }

    @Override
    protected ShapeTraits getShapeTraits() {
        return traits;
    }

    @Override
    protected ShapeOptions newOptions() {
        options = new ShapePolygonOptions();
        return options;
    }

    public void addToMap(FragmentActivity activity, GoogleMap googleMap, @Nullable Consumer<ShapePolygon> consumer) {
        final Polygon polygon = googleMap.addPolygon(options.getNativeOptions());
        polygon.setTag(tag);
        IconDescriptor patternIconDescriptor = options.getPatternIconDescriptor();
        if (patternIconDescriptor != null) {
            new AsyncIconLoader(patternIconDescriptor, activity)
                    .load((bitmap) -> {
                        ShapePolygon shapePolygon;
                        if (bitmap != null) {
                            GroundOverlayOptions overlayOptions = fillPoly(
                                    googleMap.getProjection(),
                                    options.getPoints(),
                                    bitmap,
                                    0.0f);
                            // put this overlay above the polygon
                            overlayOptions.zIndex(options.getZIndex() + 1);
                            GroundOverlay overlay = googleMap.addGroundOverlay(overlayOptions);
                            shapePolygon = new ShapePolygon(polygon, overlay);
                        } else {
                            shapePolygon = new ShapePolygon(polygon);
                        }
                        if (consumer != null) {
                            consumer.accept(shapePolygon);
                        }
                    });
        } else {
            if (consumer != null) {
                consumer.accept(new ShapePolygon(polygon));
            }
        }
    }

    private GroundOverlayOptions fillPoly(Projection projection,
                                          List<LatLng> points,
                                          Bitmap tileBitmap,
                                          float transparency) {
        LatLngBounds bounds = getPolygonBounds(points);

        double boundHeight = bounds.northeast.latitude - bounds.southwest.latitude;
        double boundWidth = bounds.northeast.longitude - bounds.southwest.longitude;

        Point northEast = projection.toScreenLocation(bounds.northeast);
        Point southWest = projection.toScreenLocation(bounds.southwest);

        int screenBoundHeight = southWest.y - northEast.y;
        int screenBoundWidth = northEast.x - southWest.x;

        // determine overlay bitmap size
        double k = (double) screenBoundWidth / Math.abs((double) screenBoundHeight);
        int overlayWidth;
        int overlayHeight;
        if (Math.abs(boundWidth) > Math.abs(boundHeight)) {
            overlayWidth = OVERLAY_MAX_SIZE;
            overlayHeight = (int) (overlayWidth * k);
        } else {
            overlayHeight = OVERLAY_MAX_SIZE;
            overlayWidth = (int) (overlayHeight * k);
        }

        // create overlay bitmap
        Bitmap overlayBitmap = createOverlayBitmap(
                overlayWidth, overlayHeight, bounds, points, tileBitmap);

        return new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromBitmap(overlayBitmap))
                .transparency(transparency)
                .positionFromBounds(bounds);
    }

    private Bitmap createOverlayBitmap(int width,
                                       int height,
                                       LatLngBounds bounds,
                                       List<LatLng> points,
                                       Bitmap tileBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Path path = new Path();
        BitmapShader shader = new BitmapShader(tileBitmap,
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10);
        paint.setAntiAlias(true);
        paint.setShader(shader);

        List<Point> screenPoints = new ArrayList<>(points.size());

        double boundHeight = bounds.northeast.latitude - bounds.southwest.latitude;
        double boundWidth = bounds.northeast.longitude - bounds.southwest.longitude;
        double kx = width / boundWidth;
        double ky = height / boundHeight;
        for (int i = 0; i < points.size(); i++) {
            LatLng latLng = points.get(i);
            Point screenPoint = new Point();
            screenPoint.x = (int) (kx * (latLng.longitude - bounds.southwest.longitude));
            screenPoint.y = height - (int) (ky * (latLng.latitude - bounds.southwest.latitude));
            screenPoints.add(screenPoint);
        }

        path.moveTo(screenPoints.get(0).x, screenPoints.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(screenPoints.get(i).x, screenPoints.get(i).y);
        }
        path.close();

        canvas.drawPath(path, paint);

        return bitmap;
    }

    private LatLngBounds getPolygonBounds(List<LatLng> polygon) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < polygon.size(); i++) {
            builder.include(polygon.get(i));
        }
        LatLngBounds bounds = builder.build();
        return bounds;
    }
}
