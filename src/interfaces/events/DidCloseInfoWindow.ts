import { Marker } from "./../../definitions";

export interface DidCloseInfoWindowResult {
  marker: Marker;
}

export type DidCloseInfoWindowCallback = (
  result: DidCloseInfoWindowResult | null,
  err?: any
) => void;
