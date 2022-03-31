import { LatLng, Marker, MarkerPreferences, Icon } from "./../../definitions";

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
  /**
   * @since x.x.x
   */	
  icon?: Icon;
}

export interface AddMarkerResult {
  /**
   * @since 2.0.0
   */
  marker: Marker;
}
