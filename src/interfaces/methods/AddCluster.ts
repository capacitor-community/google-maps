import { ClusteredMarkerPreferences, Icon, Marker, CaptionPreferences } from "./../../definitions";

export interface AddClusterOptions {
  mapId: string;
  markers: ClusteredMarkerPreferences[];
  clusterIcon?: Icon;
  clusterCaptionPrefs?: CaptionPreferences;
}

export interface AddClusterResult {
  marker: Marker[];
}
