import {
  MapAppearance,
  MapControls,
  MapGestures,
  CameraPosition,
} from "./../../../definitions";

export interface MapPreferences {
  /**
   * See CameraPosition
   *
   * @since 2.0.0
   */
  cameraPosition: CameraPosition;
  /**
   * See MapGestures
   *
   * @since 2.0.0
   */
  gestures: MapGestures;
  /**
   * See MapControls
   *
   * @since 2.0.0
   */
  controls: MapControls;
  /**
   * See MapAppearance
   *
   * @since 2.0.0
   */
  appearance: MapAppearance;

  maxZoom: number; // @todo: Sets a preferred upper bound for the camera zoom.

  minZoom: number; // @todo: Sets a preferred lower bound for the camera zoom.

  padding: any; // @todo: Sets padding on the map.

  liteMode: boolean;
}
