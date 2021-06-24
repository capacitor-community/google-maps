import { LatLng, Marker } from "./../../definitions";

export interface DidCloseInfoWindowResult {
  position: LatLng;
  marker: Marker;
}

export type DidCloseInfoWindowCallback = (
  result: DidCloseInfoWindowResult | null,
  err?: any
) => void;
