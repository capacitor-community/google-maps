import { LatLng } from "./../../definitions";
export interface ViewBoundsOptions {
  /**
   * The identifier of the map to which this method should be applied.
   *
   * @since 2.0.0
   */
  mapId: string;
}
export interface Bounds {
  /**
   * @since 2.0.0
   */
  farLeft: LatLng;
  /**
   * @since 2.0.0
   */
  farRight: LatLng;
  /**
   * @since 2.0.0
   */
  nearLeft: LatLng;
  /**
   * @since 2.0.0
   */
  nearRight: LatLng;
  /**
   * @since 2.0.0
   */
  center: LatLng;
}
export interface ViewBoundsResult {
  /**
   * @since 2.0.0
   */
  bounds: Bounds;
}