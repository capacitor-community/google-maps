import { Marker } from "./../../definitions";

export interface DidTapMarkerResult {
  marker: Marker;
}

export type DidTapMarkerCallback = (
  result: DidTapMarkerResult | null,
  err?: any
) => void;
