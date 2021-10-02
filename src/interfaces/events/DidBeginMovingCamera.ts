import { CameraMovementReason } from "./../../definitions";

export interface DidBeginMovingCameraResult {
  reason: CameraMovementReason;
}

export type DidBeginMovingCameraCallback = (
  result: DidBeginMovingCameraResult,
  err?: any
) => void;
