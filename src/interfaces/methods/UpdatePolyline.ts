import { PolylinePreferences } from "../../definitions";

export interface UpdatePolylineOptions {
  mapId: string;
  id: string;
  preferences?: PolylinePreferences;
}