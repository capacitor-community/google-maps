import { ClusteredMarkerPreferences, ClusteredIcon } from "./../../definitions";

export interface AddClusterOptions {
  mapId: string;
  markers: ClusteredMarkerPreferences[];
  clusterIcons?: ClusteredIcon[]
}

export interface AddClusterResult {

}
