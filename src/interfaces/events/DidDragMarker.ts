import { LatLng, Marker } from "./../../definitions";

export interface DidDragMarkerResult {
  position: LatLng;
  marker: Marker;
}

export type DidDragMarkerCallback = (
  result: DidDragMarkerResult | null,
  err?: any
) => void;
