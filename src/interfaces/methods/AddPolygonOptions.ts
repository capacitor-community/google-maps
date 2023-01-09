import { PolygonPreferences, LatLng } from "../../definitions";

export interface AddPolygonOptions {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * @since 2.0.0
   */
  points: LatLng[];
  /**
   * @since 2.0.0
   */
  preferences?: PolygonPreferences;
}
