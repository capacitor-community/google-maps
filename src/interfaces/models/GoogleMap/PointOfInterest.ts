import { LatLng } from "./../../../definitions";

export interface PointOfInterest {
  /**
   * The name of the POI.
   *
   * @since 2.0.0
   */
  name: String;
  /**
   * The placeId of the POI.
   * Read more about what you can use a placeId for here: https://developers.google.com/maps/documentation/places/web-service/place-id
   *
   * @since 2.0.0
   */
  placeId: String;
  /**
   * @since 2.0.0
   */
  position: LatLng;
}
