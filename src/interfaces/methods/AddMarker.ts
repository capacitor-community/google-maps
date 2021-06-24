import { LatLng, Marker, MarkerPreferences } from "./../../definitions";

export interface AddMarkerOptions {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * @since 2.0.0
   */
  preferences?: MarkerPreferences;
  /**
   * @since 2.0.0
   */
  position: LatLng;
}

export interface AddMarkerResult {
  /**
   * @since 2.0.0
   */
  marker: Marker;
  /**
   * @since 2.0.0
   */
  position: LatLng;
}
