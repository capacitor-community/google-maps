import { CameraPosition } from "./../../definitions";

export interface MoveCameraOptions {
  /**
   * The identifier of the map to which this method should be applied.
   *
   * @since 2.0.0
   */
  mapId: string;
  /**
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
  duration?: number;
  /**
   * By default the moveCamera method uses the current CameraPosition as the base.
   * That means that if, for example, the CameraPosition.target is not specified,
   * the current CameraPosition.target will be used.
   * Among other things, this default behaviour allows you to set the zoom without moving the map.
   * Or move the map without changing the current zoom.
   * If instead of this default behaviour, the previous CameraPosition (the one you gave the previous
   * time you called `moveCamera` or `createMap`) should be used as the base, this parameter should be set to `false`.
   * But be cautious when using this.
   * If the user made changes to the CameraPosition (e.g. by scrolling or zooming the map),
   * those changes will be undone because it will be overwritten by the last explicitly set CameraPosition.
   * The CameraPosition is only "explicitly set" with these methods: `createMap` and `moveCamera`.
   *
   * @default true
   * @since 2.0.0
   */
  useCurrentCameraPositionAsBase?: boolean;
}
