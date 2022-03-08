import { Marker } from "./../../definitions";

export interface DidBeginDraggingMarkerResult {
  marker: Marker;
}

export type DidBeginDraggingMarkerCallback = (
  result: DidBeginDraggingMarkerResult | null,
  err?: any
) => void;
