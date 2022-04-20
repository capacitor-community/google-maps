import { LatLng, CirclePreferences } from "../../definitions";

export interface AddCircleOptions {
  mapId: string;
  center: LatLng;
  radius: number;
  preferences?: CirclePreferences;
}