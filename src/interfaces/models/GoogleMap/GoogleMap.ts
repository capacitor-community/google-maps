import { MapPreferences, CameraPosition } from "./../../../definitions";

export interface GoogleMap {
  /**
   * GUID representing the unique id of this map
   *
   * @since 2.0.0
   */
  mapId: string;
  /**
   * See CameraPosition
   *
   * @since 2.0.0
   */
  cameraPosition: CameraPosition;
  /**
   * See MapPreferences
   *
   * @since 2.0.0
   */
  preferences: MapPreferences;
}
