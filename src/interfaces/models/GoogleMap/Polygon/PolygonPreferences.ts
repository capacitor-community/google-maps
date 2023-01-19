import { LatLng } from "../../../../definitions";

export interface PolygonPreferences {
  /**
   * A hole is a region inside the polygon that is not filled.
   * A hole is specified in exactly the same way as the path of the polygon itself.
   * A hole must be fully contained within the outline.
   * Multiple holes can be specified, however overlapping holes are not supported.
   *
   * @since 2.0.0
   */
  holes?: LatLng[][];
  /**
   * Line segment width in screen pixels.
   * The width is constant and independent of the camera's zoom level.
   *
   * @default 10
   * @since 2.0.0
   */
  strokeWidth?: number;
  /**
   * Line segment color in HEX format (with transparency).
   *
   * @default #000000 (black)
   * @since 2.0.0
   */
  strokeColor?: string;
  /**
   * Fill color in HEX format (with transparency).
   *
   * @default #00000000 (transparent)
   * @since 2.0.0
   */
  fillColor?: string;
  /**
   * The z-index specifies the stack order of this polygon, relative to other polygons on the map.
   * A polygon with a higher z-index is drawn on top of those with lower indices.
   * Markers are always drawn above tile layers and other non-marker overlays (ground overlays,
   * polylines, polygons, and other shapes) regardless of the z-index of the other overlays.
   * Markers are effectively considered to be in a separate z-index group compared to other overlays.
   *
   * @default 0
   * @since 2.0.0
   */
  zIndex?: number;
  /**
   * Sets the visibility of this polygon.
   * When not visible, a polygon is not drawn, but it keeps all its other properties.
   *
   * @default true
   * @since 2.0.0
   */
  isVisible?: boolean;
  /**
   * If `true`, then each segment is drawn as a geodesic.
   * If `false`, each segment is drawn as a straight line on the Mercator projection.
   *
   * @default false
   * @since 2.0.0
   */
  isGeodesic?: boolean;
  /**
   * If you want to handle events fired when the user clicks the polygon, set this property to `true`.
   * You can change this value at any time.
   *
   * @default false
   * @since 2.0.0
   */
  isClickable?: boolean;
  /**
   * You can use this property to associate an arbitrary object with this overlay.
   * The Google Maps SDK neither reads nor writes this property.
   * Note that metadata should not hold any strong references to any Maps objects,
   * otherwise a retain cycle may be created (preventing objects from being released).
   *
   * @default {}
   * @since 2.0.0
   */
  metadata?: { [key: string]: any };
}
