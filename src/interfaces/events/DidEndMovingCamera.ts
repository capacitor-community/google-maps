import { CameraPosition } from "./../../definitions";

export interface DidEndMovingCameraResult {
  cameraPosition: CameraPosition;
}

export type DidEndMovingCameraCallback = (
  result: DidEndMovingCameraResult,
  err?: any
) => void;
