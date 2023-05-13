import { Polyline, PolylinePreferences, LatLng } from "../../definitions";

export interface AddPolylineOptions {
  /**
   * GUID representing the map this polyline is a part of
   *
   * @since 2.0.0
   */
  mapId: string;
  /**
   * Line segments are drawn between consecutive points in the shorter of the two directions
   *
   * @since 2.0.0
   */
  path: LatLng[];
  /**
   * @since 2.0.0
   */
  preferences?: PolylinePreferences;
}

export interface AddPolylineResult {
  /**
   * @since 2.0.0
   */
  polyline: Polyline;
}
