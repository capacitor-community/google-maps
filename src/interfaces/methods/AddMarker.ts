import { LatLng, Marker, MarkerPreferences } from "./../../definitions";

export interface AddMarkerOptions {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * @since 2.0.0
   */
  position: LatLng;
  /**
   * @since 2.0.0
   */
  preferences?: MarkerPreferences;
}

export interface AddMarkerResult {
  /**
   * @since 2.0.0
   */
  marker: Marker;
}
