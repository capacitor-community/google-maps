import { ClusteredMarkerPreferences, ClusterIcon, Marker } from "./../../definitions";

export interface AddClusterOptions {
  mapId: string;
  markers: ClusteredMarkerPreferences[];
  clusterIcons?: ClusterIcon[]
}

export interface AddClusterResult {
  marker: Marker[];
}
