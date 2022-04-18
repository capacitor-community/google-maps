import { PolylinePreferences } from "./PolylinePreferences";
import { LatLng } from "../../../../definitions";

export interface Polyline {
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
  preferences?: PolylinePreferences;
}
