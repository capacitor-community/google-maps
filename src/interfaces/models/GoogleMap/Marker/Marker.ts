export interface Marker {
  /**
   * GUID representing the unique id of this marker
   *
   * @since 2.0.0
   */
  markerId: string;
  /**
   * A text string that's displayed in an info window when the user taps the marker.
   * You can change this value at any time.
   *
   * @since 2.0.0
   */
  title: string;
  /**
   * Additional text that's displayed below the title. You can change this value at any time.
   *
   * @since 2.0.0
   */
  snippet: string;
  /**
   * This is a value from 0 to 1, where 0 means the marker is completely transparent and 1 means the marker is completely opaque.
   *
   * @since 2.0.0
   */
  opacity: number;
  /**
   * Controls whether this marker should be flat against the Earth's surface (`true`) or a billboard facing the camera (`false`).
   *
   * @since 2.0.0
   */
  isFlat: boolean;
  /**
   * Controls whether this marker can be dragged interactively.
   * When a marker is draggable, it can be moved by the user by long pressing on the marker.
   *
   * @since 2.0.0
   */
  isDraggable: boolean;
  /**
   * You can use this property to associate an arbitrary object with this overlay.
   * The Google Maps SDK neither reads nor writes this property.
   * Note that metadata should not hold any strong references to any Maps objects,
   * otherwise a retain cycle may be created (preventing objects from being released).
   *
   * @since 2.0.0
   */
  metadata: object;
}
