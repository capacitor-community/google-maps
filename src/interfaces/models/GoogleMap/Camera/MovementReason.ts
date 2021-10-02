export enum CameraMovementReason {
  /**
   * Camera motion initiated in response to user gestures on the map.
   * For example: pan, tilt, pinch to zoom, or rotate.
   *
   * @since 2.0.0
   */
  Gesture = 1,
  /**
   * Indicates that this is part of a programmatic change - for example, via methods such as `moveCamera`.
   * This may also be the case if a user has tapped on the My Location or compass buttons, which generate animations that change the camera.
   *
   * @since 2.0.0
   */
  Other = 2,
}
