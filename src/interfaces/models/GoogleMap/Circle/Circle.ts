import { CirclePreferences, LatLng } from "./../../../../definitions";

export interface Circle {
  mapId: string;
  id: string;
  position: LatLng;
  radius: number;
  preferences?: CirclePreferences;
}