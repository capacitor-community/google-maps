import { Marker } from "./../../definitions";

export interface DidDragMarkerResult {
  marker: Marker;
}

export type DidDragMarkerCallback = (
  result: DidDragMarkerResult | null,
  err?: any
) => void;
