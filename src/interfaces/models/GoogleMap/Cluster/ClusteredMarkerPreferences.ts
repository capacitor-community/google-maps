import { LatLng, MarkerPreferences } from "../../../../definitions";

export interface ClusteredMarkerPreferences {
  position: LatLng;
  preferences?: MarkerPreferences;
}