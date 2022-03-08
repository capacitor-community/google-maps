import { Marker } from "./../../definitions";

export interface DidEndDraggingMarkerResult {
  marker: Marker;
}

export type DidEndDraggingMarkerCallback = (
  result: DidEndDraggingMarkerResult | null,
  err?: any
) => void;
