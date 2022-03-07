/**
 * Aggregates all gesture parameters such as allowing for rotating, scrolling, tilting and zooming the map.
 */
export interface MapGestures {
  /**
   * If `true`, rotate gestures are allowed.
   * If enabled, users can use a two-finger rotate gesture to rotate the camera.
   * If disabled, users cannot rotate the camera via gestures.
   * This setting doesn't restrict the user from tapping the compass button to reset the camera orientation,
   * nor does it restrict programmatic movements and animation of the camera.
   *
   * @default true
   * @since 2.0.0
   */
  isRotateAllowed?: boolean;
  /**
   * If `true`, scroll gestures are allowed.
   * If enabled, users can swipe to pan the camera.
   * If disabled, swiping has no effect.
   * This setting doesn't restrict programmatic movement and animation of the camera.
   *
   * @default true
   * @since 2.0.0
   */
  isScrollAllowed?: boolean;
  /**
   * If `true`, scroll gestures can take place at the same time as a zoom or rotate gesture.
   * If enabled, users can scroll the map while rotating or zooming the map.
   * If disabled, the map cannot be scrolled while the user rotates or zooms the map using gestures.
   * This setting doesn't disable scroll gestures entirely, only during rotation and zoom gestures,
   * nor does it restrict programmatic movements and animation of the camera.
   *
   * @default true
   * @since 2.0.0
   */
  isScrollAllowedDuringRotateOrZoom?: boolean;
  /**
   * If `true`, tilt gestures are allowed.
   * If enabled, users can use a two-finger vertical down swipe to tilt the camera.
   * If disabled, users cannot tilt the camera via gestures.
   * This setting doesn't restrict users from tapping the compass button to reset the camera orientation,
   * nor does it restrict programmatic movement and animation of the camera.
   *
   * @default true
   * @since 2.0.0
   */
  isTiltAllowed?: boolean;
  /**
   * If `true`, zoom gestures are allowed.
   * If enabled, users can either double tap/two-finger tap or pinch to zoom the camera.
   * If disabled, these gestures have no effect.
   * This setting doesn't affect the zoom buttons,
   * nor does it restrict programmatic movement and animation of the camera.
   *
   * @default true
   * @since 2.0.0
   */
  isZoomAllowed?: boolean;
}
