import { LatLng } from "./../../definitions";

export interface DidLongPressMapResult {
  position: LatLng;
}

export type DidLongPressMapCallback = (
  result: DidLongPressMapResult | null,
  err?: any
) => void;
