import { PolygonPreferences, LatLng, Polygon } from "../../definitions";

export interface PolygonOptions {
  mapId: string;
  points: LatLng[];
  preferences?: PolygonPreferences;
}
  
export interface PolygonResult {
  polygon: Polygon;
}