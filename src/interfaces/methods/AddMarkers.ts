import { LatLng, MarkerPreferences } from "./../../definitions";

export interface MarkerInputEntry {
   /**
    * @since 2.0.0
    */
   position: LatLng;
   /**
    * @since 2.0.0
    */
   preferences?: MarkerPreferences;
}

export interface AddMarkersOptions {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * @since 2.0.0
   */
  markers: MarkerInputEntry[];
}

export interface MarkerOutputEntry {
  /**
   * GUID representing the unique id of this marker
   *
   * @since 2.0.0
   */
   markerId: string;
   /**
    * @since 2.0.0
    */
   position: LatLng;
   /**
    * @since 2.0.0
    */
   preferences: MarkerPreferences;
}

export interface AddMarkersResult {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * @since 2.0.0
   */
  markers: MarkerOutputEntry[];
}
