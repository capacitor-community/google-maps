import { Polygon, PolygonPreferences, LatLng } from "../../definitions";

export interface AddPolygonOptions {
  /**
   * GUID representing the map this polygon is a part of
   *
   * @since 2.0.0
   */
  mapId: string;
  /**
   * The path (outline) is specified by a list of vertices in clockwise or counterclockwise order.
   * It is not necessary for the start and end points to coincide;
   * if they do not, the polygon will be automatically closed.
   * Line segments are drawn between consecutive points in the shorter of the two directions (east or west).
   *
   * @since 2.0.0
   */
  path: LatLng[];
  /**
   * @since 2.0.0
   */
  preferences?: PolygonPreferences;
}

export interface AddPolygonResult {
  /**
   * @since 2.0.0
   */
  polygon: Polygon;
}
