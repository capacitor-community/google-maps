/**
 * Aggregates all control parameters such as enabling the compass, my-location and zoom buttons as well as the toolbar.
 */
export interface MapControls {
  /**
   * If `true`, the compass button is enabled.
   * The compass is an icon on the map that indicates the direction of north on the map.
   * If enabled, it is only shown when the camera is rotated away from its default orientation (bearing of 0).
   * When a user taps the compass, the camera orients itself to its default orientation and fades away shortly after.
   * If disabled, the compass will never be displayed.
   *
   * @default true
   * @since 2.0.0
   */
  isCompassButtonEnabled?: boolean;
  // TODO:
  // /**
  //  * If `true`, the indoor level picker is enabled.
  //  * If the button is enabled, it is only shown when `MapAppearance.isIndoorShown === true`.
  //  * Additionally, it is only visible when the view is focused on a building with indoor floor data.
  //  *
  //  */
  // isIndoorLevelPickerEnabled: boolean;
  /**
   * (Android only)
   *
   * If `true`, the Map Toolbar is enabled.
   * If enabled, and the Map Toolbar can be shown in the current context, users will see a bar with various context-dependent actions, including 'open this map in the Google Maps app' and 'find directions to the highlighted marker in the Google Maps app'.
   *
   * @default false
   * @since 2.0.0
   */
  isMapToolbarEnabled?: boolean;
  /**
   * If `true`, the my-location button is enabled.
   * This is a button visible on the map that, when tapped by users, will center the map on the current user location.
   * If the button is enabled, it is only shown when `MapAppearance.isMyLocationDotShown === true`.
   *
   * @default true
   * @since 2.0.0
   */
  isMyLocationButtonEnabled?: boolean;
  /**
   * (Android only)
   * If `true`, the zoom controls are enabled.
   * The zoom controls are a pair of buttons (one for zooming in, one for zooming out) that appear on the screen when enabled.
   * When pressed, they cause the camera to zoom in (or out) by one zoom level.
   * If disabled, the zoom controls are not shown.
   *
   * @default false
   * @since 2.0.0
   */
  isZoomButtonsEnabled?: boolean;
}
