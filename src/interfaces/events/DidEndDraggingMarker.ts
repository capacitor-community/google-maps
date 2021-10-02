import { LatLng, Marker } from "./../../definitions";

export interface DidEndDraggingMarkerResult {
  position: LatLng;
  marker: Marker;
}

export type DidEndDraggingMarkerCallback = (
  result: DidEndDraggingMarkerResult | null,
  err?: any
) => void;
