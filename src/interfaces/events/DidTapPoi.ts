import { LatLng, PointOfInterest } from "./../../definitions";

export interface DidTapPoiResult {
  position: LatLng;
  poi: PointOfInterest;
}

export type DidTapPoiCallback = (
  result: DidTapPoiResult | null,
  err?: any
) => void;
