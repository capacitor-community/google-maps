import { PolylinePreferences } from "../../definitions";

export interface UpdatePolylineOptions {
  mapId: string;
  polylineId: string;
  preferences?: PolylinePreferences;
}