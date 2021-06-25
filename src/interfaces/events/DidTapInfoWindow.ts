import { LatLng, Marker } from "./../../definitions";

export interface DidTapInfoWindowResult {
  position: LatLng;
  marker: Marker;
}

export type DidTapInfoWindowCallback = (
  result: DidTapInfoWindowResult | null,
  err?: any
) => void;
