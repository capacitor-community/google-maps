import { Polyline } from "../../definitions";

export interface DidTapPolylineResult {
  polyline: Polyline;
}

export type DidTapPolylineCallback = (
  result: DidTapPolylineResult | null,
  err?: any
) => void;
