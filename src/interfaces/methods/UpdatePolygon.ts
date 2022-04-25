import { PolygonPreferences } from "../../definitions";

export interface UpdatePolygonOptions {
  mapId: string;
  id: string;
  preferences?: PolygonPreferences;
}