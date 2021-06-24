/**
 * A data class representing a pair of latitude and longitude coordinates, stored as degrees.
 */
export interface LatLng {
  /**
   * Latitude, in degrees. This value is in the range [-90, 90].
   *
   * @since 2.0.0
   */
  latitude: number;
  /**
   * Longitude, in degrees. This value is in the range [-180, 180].
   *
   * @since 2.0.0
   */
  longitude: number;
}
