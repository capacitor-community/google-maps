import { Polygon } from "./../../definitions";

export interface DidTapPolygonResult {
  polygon: Polygon;
}

export type DidTapPolygonCallback = (
  result: DidTapPolygonResult | null,
  err?: any
) => void;
