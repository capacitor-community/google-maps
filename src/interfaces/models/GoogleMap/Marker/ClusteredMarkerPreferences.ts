import { LatLng, Icon, MarkerPreferences } from "../../../../definitions";

export interface ClusteredMarkerPreferences {
  position: LatLng;
  preferences?: MarkerPreferences;
  icon?: Icon;
}