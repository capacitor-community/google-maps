import { MarkerPreferences, LatLng } from "./../../../../definitions";

export interface Marker {
  /**
   * GUID representing the map this marker is part of
   *
   * @since 2.0.0
   */
  mapId: string;
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
