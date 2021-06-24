import { LatLng } from "./../../definitions";

export interface DidTapMapResult {
  position: LatLng;
}

export type DidTapMapCallback = (
  result: DidTapMapResult | null,
  err?: any
) => void;
