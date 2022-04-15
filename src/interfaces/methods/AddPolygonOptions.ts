import { PolygonPreferences, LatLng } from "../../definitions";

export interface AddPolygonOptions {
  mapId: string;
  points: LatLng[];
  preferences?: PolygonPreferences;
}
