import { Marker } from "./../../definitions";

export interface DidTapInfoWindowResult {
  marker: Marker;
}

export type DidTapInfoWindowCallback = (
  result: DidTapInfoWindowResult | null,
  err?: any
) => void;
