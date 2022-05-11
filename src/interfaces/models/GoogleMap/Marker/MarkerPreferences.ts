import { MarkerIcon } from "./../../../../definitions";

export interface MarkerPreferences {
  /**
   * A text string that's displayed in an info window when the user taps the marker.
   * You can change this value at any time.
   *
   * @default null
   * @since 2.0.0
   */
  title?: string;
  /**
   * Additional text that's displayed below the title. You can change this value at any time.
   *
   * @default null
   * @since 2.0.0
   */
  snippet?: string;
  /**
   * This is a value from 0 to 1, where 0 means the marker is completely transparent and 1 means the marker is completely opaque.
   *
   * @default 1
   * @since 2.0.0
   */
  opacity?: number;
  /**
   * Controls whether this marker should be flat against the Earth's surface (`true`) or a billboard facing the camera (`false`).
   *
   * @default false
   * @since 2.0.0
   */
  isFlat?: boolean;
  /**
   * Controls whether this marker can be dragged interactively.
   * When a marker is draggable, it can be moved by the user by long pressing on the marker.
   *
   * @default false
   * @since 2.0.0
   */
  isDraggable?: boolean;
  /**
   * The z-index specifies the stack order of this marker, relative to other markers on the map.
   * A marker with a high z-index is drawn on top of markers with lower z-indexes.
   * Markers are always drawn above tile layers and other non-marker overlays (ground overlays,
   * polylines, polygons, and other shapes) regardless of the z-index of the other overlays.
   * Markers are effectively considered to be in a separate z-index group compared to other overlays.
   *
   * @default 0
   * @since 2.0.0
   */
  zIndex?: number;
  /**
   * Specifies the anchor to be at a particular point in the marker image.
   *
   * The anchor specifies the point in the icon image that is anchored to the marker's position on the Earth's surface.
   *
   * The anchor point is specified in the continuous space [0.0, 1.0] x [0.0, 1.0], where (0, 0) is the top-left corner of the image, and (1, 1) is the bottom-right corner.
   *
   * Read more about it here: https://developers.google.com/android/reference/com/google/android/gms/maps/model/MarkerOptions#anchor(float,%20float)
   *
   * @default { x: 0.5, y: 1 }
   * @since 2.0.0
   */
  anchor?: { x: number; y: number };
  /**
   * @default undefined
   * @since 2.0.0
   */
  icon?: MarkerIcon;
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
