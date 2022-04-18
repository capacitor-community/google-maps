import { PolylinePreferences, LatLng } from "../../definitions";

export interface AddPolylineOptions {
  mapId: string;
  points: LatLng[];
  preferences?: PolylinePreferences;
}
