import { GoogleMap, BoundingRect, MapPreferences } from "./../../definitions";

export interface UpdateMapOptions {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * @since 2.0.0
   */
  element?: HTMLElement;
  /**
   * @since 2.0.0
   */
  boundingRect?: BoundingRect;
  /**
   * @since 2.0.0
   */
  preferences?: MapPreferences;
}

export interface UpdateMapResult {
  /**
   * @since 2.0.0
   */
  googleMap: GoogleMap;
}
