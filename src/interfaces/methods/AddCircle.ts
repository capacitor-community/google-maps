import { LatLng, CirclePreferences } from "./../../definitions";

export interface AddCircleOptions {
  mapId: string;
  position: LatLng;
  radius: number;
  preferences?: CirclePreferences;
}