import { PolylinePreferences } from "../../definitions";

export interface UpdatePolylineOptions {
  mapId: string;
  polygonId: string;
  preferences?: PolylinePreferences;
}