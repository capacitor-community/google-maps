import { LatLng } from "./../../../../definitions";

/**
 * The map view is modeled as a camera looking down on a flat plane.
 * The position of the camera (and hence the rendering of the map) is specified by the following properties: target (latitude/longitude location), bearing, tilt, and zoom.
 * More information can be found here: https://developers.google.com/maps/documentation/android-sdk/views#the_camera_position
 */
export interface CameraPosition {
  /**
   * The camera target is the location of the center of the map, specified as latitude and longitude co-ordinates.
   *
   * @since 2.0.0
   */
  target?: LatLng;
  /**
   * The camera bearing is the direction in which a vertical line on the map points, measured in degrees clockwise from north.
   * Someone driving a car often turns a road map to align it with their direction of travel, while hikers using a map and compass usually orient the map so that a vertical line is pointing north.
   * The Maps API lets you change a map's alignment or bearing.
   * For example, a bearing of 90 degrees results in a map where the upwards direction points due east.
   *
   * @since 2.0.0
   */
  bearing?: number;
  /**
   * The tilt defines the camera's position on an arc between directly over the map's center position and the surface of the Earth, measured in degrees from the nadir (the direction pointing directly below the camera).
   * When you change the viewing angle, the map appears in perspective, with far-away features appearing smaller, and nearby features appearing larger.
   *
   * @since 2.0.0
   */
  tilt?: number;
  /**
   * The zoom level of the camera determines the scale of the map.
   * At larger zoom levels more detail can be seen on the screen,
   * while at smaller zoom levels more of the world can be seen on the screen.
   *
   * @since 2.0.0
   */
  zoom?: number;
}
