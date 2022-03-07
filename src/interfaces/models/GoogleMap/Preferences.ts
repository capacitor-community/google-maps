import {
  MapAppearance,
  MapControls,
  MapGestures,
} from "./../../../definitions";

export interface MapPreferences {
  /**
   * @since 2.0.0
   */
  gestures?: MapGestures;
  /**
   * @since 2.0.0
   */
  controls?: MapControls;
  /**
   * @since 2.0.0
   */
  appearance?: MapAppearance;

  maxZoom?: number; // @todo: Sets a preferred upper bound for the camera zoom.

  minZoom?: number; // @todo: Sets a preferred lower bound for the camera zoom.

  padding?: any; // @todo: Sets padding on the map.

  liteMode?: boolean; // @todo
}
