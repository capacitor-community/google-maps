enum MapType {
  /**
   * No base map tiles.
   *
   * @since 2.0.0
   */
  None = 0,
  /**
   * Basic map.
   *
   * @since 2.0.0
   */
  Normal = 1,
  /**
   * Satellite imagery with no labels.
   *
   * @since 2.0.0
   */
  Satellite = 2,
  /**
   * Topographic data.
   *
   * @since 2.0.0
   */
  Terrain = 3,
  /**
   * Satellite imagery with roads and labels.
   *
   * @since 2.0.0
   */
  Hybrid = 4,
}

/**
 * Aggregates all appearance parameters such as showing 3d building, indoor maps, the my-location (blue) dot and traffic.
 * Additionally, it also holds parameters such as the type of map tiles and the overall styling of the base map.
 */
export interface MapAppearance {
  /**
   * Controls the type of map tiles that should be displayed.
   *
   * @default MapType.Normal
   * @since 2.0.0
   */
  type?: MapType;
  /**
   * Holds details about a style which can be applied to a map.
   * When set to `null` the default styling will be used.
   * With style options you can customize the presentation of the standard Google map styles, changing the visual display of features like roads, parks, and other points of interest.
   * As well as changing the style of these features, you can also hide features entirely.
   * This means that you can emphasize particular components of the map or make the map complement the content of your app.
   * For more information check: https://developers.google.com/maps/documentation/ios-sdk/style-reference
   * Or use the wizard for generating JSON: https://mapstyle.withgoogle.com/
   *
   * @default null
   * @since 2.0.0
   */
  style?: string | null;
  /**
   * If `true`, 3D buildings will be shown where available.
   *
   * @default true
   * @since 2.0.0
   */
  isBuildingsShown?: boolean;
  /**
   * If `true`, indoor maps are shown, where available.
   * If this is set to false, caches for indoor data may be purged and any floor currently selected by the end-user may be reset.
   *
   * @default true
   * @since 2.0.0
   */
  isIndoorShown?: boolean;
  /**
   * If `true`, the my-location (blue) dot and accuracy circle are shown.
   *
   * @default false
   * @since 2.0.0
   */
  isMyLocationDotShown?: boolean;
  /**
   * If `true`, the map draws traffic data, if available.
   * This is subject to the availability of traffic data.
   *
   * @default false
   * @since 2.0.0
   */
  isTrafficShown?: boolean;
}
