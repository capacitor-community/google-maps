import { ClusteredMarkerPreferences, Icon, Marker } from "./../../definitions";

export interface AddClusterOptions {
  mapId: string;
  markers: ClusteredMarkerPreferences[];
  clusterIcon?: Icon;
}

export interface AddClusterResult {
  marker: Marker[];
}
