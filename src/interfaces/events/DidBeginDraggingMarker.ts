import { LatLng, Marker } from "./../../definitions";

export interface DidBeginDraggingMarkerResult {
  position: LatLng;
  marker: Marker;
}

export type DidBeginDraggingMarkerCallback = (
  result: DidBeginDraggingMarkerResult | null,
  err?: any
) => void;
