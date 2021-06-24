import { LatLng, Marker } from "./../../definitions";

export interface DidTapMarkerResult {
  position: LatLng;
  marker: Marker;
}

export type DidTapMarkerCallback = (
  result: DidTapMarkerResult | null,
  err?: any
) => void;
