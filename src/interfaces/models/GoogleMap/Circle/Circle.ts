import { CirclePreferences, LatLng } from "./../../../../definitions";

export interface Circle {
  mapId: string;
  circleId: string;
  position: LatLng;
  radius: number;
  preferences?: CirclePreferences;
}