import { PolygonPreferences } from "./PolygonPreferences";
import { LatLng } from "../../../../definitions";

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
  polygonId: string;
  points: LatLng[];
  preferences: PolygonPreferences;
}
