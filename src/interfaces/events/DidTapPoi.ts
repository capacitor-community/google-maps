import { PointOfInterest } from "./../../definitions";

export interface DidTapPoiResult {
  poi: PointOfInterest;
}

export type DidTapPoiCallback = (
  result: DidTapPoiResult | null,
  err?: any
) => void;
