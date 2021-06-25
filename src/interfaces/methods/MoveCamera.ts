import { CameraPosition, GoogleMap } from "./../../definitions";

export interface MoveCameraOptions {
  /**
   * @since 2.0.0
   */
  mapId: string;
  /**
   * See CameraPosition
   *
   * @since 2.0.0
   */
  cameraPosition: CameraPosition;
  /**
   * The duration of the animation in milliseconds.
   * If not specified, or equals or smaller than 0, the camera movement will be immediate
   *
   * @default 0
   * @since 2.0.0
   */
  duration: number;
}

export interface MoveCameraResult {
  /**
   * @since 2.0.0
   */
  googleMap: GoogleMap;
}
