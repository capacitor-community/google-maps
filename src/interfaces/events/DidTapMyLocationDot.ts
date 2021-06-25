import { LatLng } from "./../../definitions";

export interface DidTapMyLocationDotResult {
  position: LatLng;
}

export type DidTapMyLocationDotCallback = (
  result: DidTapMyLocationDotResult | null,
  err?: any
) => void;
