import { ClusteredMarkerPreferences, ClusterIcon } from "./../../definitions";

export interface AddClusterOptions {
  mapId: string;
  markers: ClusteredMarkerPreferences[];
  clusterIcons?: ClusterIcon[]
}

export interface AddClusterResult {

}
