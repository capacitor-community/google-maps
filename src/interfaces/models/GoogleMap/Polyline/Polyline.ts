import { LatLng, PolylinePreferences } from "../../../../definitions";

export interface Polyline {
  /**
   * GUID representing the map this polyline is a part of
   *
   * @since 2.0.0
   */
  mapId: string;
  /**
   * GUID representing the unique id of this polyline
   *
   * @since 2.0.0
   */
  polylineId: string;
  /**
   * Line segments are drawn between consecutive points in the shorter of the two directions.
   *
   * @since 2.0.0
   */
  path: LatLng[];
  /**
   * @since 2.0.0
   */
  preferences?: PolylinePreferences;
}
