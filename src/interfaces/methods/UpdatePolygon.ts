import { PolygonPreferences } from "../../definitions";

export interface UpdatePolygonOptions {
  mapId: string;
  polygonId: string;
  preferences?: PolygonPreferences;
}