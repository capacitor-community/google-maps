import { LatLng, PolygonPreferences } from "../../../../definitions";

export interface Polygon {
  /**
   * GUID representing the map this marker is part of
   *
   * @since 2.0.0
   */
  mapId: string;
  /**
   * GUID representing the unique id of this polygon
   *
   */
  id: string;
  points: LatLng[];
  preferences?: PolygonPreferences;
}
