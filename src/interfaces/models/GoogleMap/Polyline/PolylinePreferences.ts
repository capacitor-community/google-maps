export interface PolylinePreferences {
   
  /**
   * Line segment width in screen pixels.
   * The width is constant and independent of the camera's zoom level.
   *
   * @default 10
   * @since 2.0.0
   */
  width?: number;

  /**
   * Line segment color in HEX format (with transparency).
   *
   * @default #000000 (black)
   * @since 2.0.0
   */
  color?: string;

  /**
  * The z-index specifies the stack order of this polyline, relative to other polylines on the map.
   * A polyline with a higher z-index is drawn on top of those with lower indices.
   * Markers are always drawn above tile layers and other non-marker overlays (ground overlays,
   * polylines, polylines, and other shapes) regardless of the z-index of the other overlays.
   * Markers are effectively considered to be in a separate z-index group compared to other overlays.
   */
   zIndex?: number;

  /**
   * Sets the visibility of this polyline.
   * When not visible, a polyline is not drawn, but it keeps all its other properties.
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
   * If you want to handle events fired when the user clicks the polyline, set this property to `true`.
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
