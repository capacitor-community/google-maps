import { WebPlugin } from "@capacitor/core";

import {
  CapacitorGoogleMapsPlugin,
  CallbackID,
  InitializeOptions,
  CreateMapOptions,
  CreateMapResult,
  UpdateMapOptions,
  UpdateMapResult,
  RemoveMapOptions,
  ClearMapOptions,
  MoveCameraOptions,
  ElementFromPointResultOptions,
  AddMarkerOptions,
  AddMarkerResult,
  RemoveMarkerOptions,
  AddMarkersOptions,
  AddMarkersResult,
  AddClusterOptions,
  AddClusterResult,
  AddPolygonOptions,
  PolygonResult,
  UpdatePolygonOptions,
  GetPolygonOptions,
  AddPolylineOptions,
  UpdatePolylineOptions,
  PolylineResult,
  GetPolylineOptions,
  AddCircleOptions,
  GetCircleOptions,
  CircleResult,
  DidTapInfoWindowCallback,
  DidCloseInfoWindowCallback,
  DidTapMapCallback,
  DidLongPressMapCallback,
  DidTapMarkerCallback,
  DidTapPolygonCallback,
  DidTapPolylineCallback,
  DidBeginDraggingMarkerCallback,
  DidDragMarkerCallback,
  DidEndDraggingMarkerCallback,
  DidTapMyLocationButtonCallback,
  DidTapMyLocationDotCallback,
  DidTapPoiCallback,
  DidBeginMovingCameraCallback,
  DidMoveCameraCallback,
  DidEndMovingCameraCallback,
  DefaultEventOptions,
  DefaultEventWithPreventDefaultOptions,
} from "./definitions";

export class CapacitorGoogleMapsWeb
  extends WebPlugin
  implements CapacitorGoogleMapsPlugin
{
  constructor() {
    super({
      name: "CapacitorGoogleMaps",
      platforms: ["web"],
    });
  }

  async initialize(_options: InitializeOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async createMap(_options: CreateMapOptions): Promise<CreateMapResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async removeMap(_options: RemoveMapOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async clearMap(_options: ClearMapOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async updateMap(_options: UpdateMapOptions): Promise<UpdateMapResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async moveCamera(_options: MoveCameraOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addMarker(_options: AddMarkerOptions): Promise<AddMarkerResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async removeMarker(_options: RemoveMarkerOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addMarkers(_options: AddMarkersOptions): Promise<AddMarkersResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addCluster(_options: AddClusterOptions): Promise<AddClusterResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addPolygon(_options: AddPolygonOptions): Promise<PolygonResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async updatePolygon(_options: UpdatePolygonOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async getPolygon(_options: GetPolygonOptions): Promise<PolygonResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async removePolygon(_options: GetPolygonOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addPolyline(_options: AddPolylineOptions): Promise<PolylineResult> {
    throw this.unimplemented("Not implemented on web.");
  }
  
  async updatePolyline(_options: UpdatePolylineOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async getPolyline(_options: GetPolylineOptions): Promise<PolylineResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async removePolyline(_options: GetPolylineOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addCircle(_options: AddCircleOptions): Promise<CircleResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async getCircle(_options: GetCircleOptions): Promise<CircleResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async removeCircle(_options: GetCircleOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapInfoWindow(
    _options: DefaultEventOptions,
    _callback: DidTapInfoWindowCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didCloseInfoWindow(
    _options: DefaultEventOptions,
    _callback: DidCloseInfoWindowCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapMap(
    _options: DefaultEventOptions,
    _callback: DidTapMapCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didLongPressMap(
    _options: DefaultEventOptions,
    _callback: DidLongPressMapCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapMarker(
    _options: DefaultEventWithPreventDefaultOptions,
    _callback: DidTapMarkerCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapPolygon(
    _options: DefaultEventOptions,
    _callback: DidTapPolygonCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapPolyline(
    _options: DefaultEventOptions,
    _callback: DidTapPolylineCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didBeginDraggingMarker(
    _options: DefaultEventOptions,
    _callback: DidBeginDraggingMarkerCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didDragMarker(
    _options: DefaultEventOptions,
    _callback: DidDragMarkerCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didEndDraggingMarker(
    _options: DefaultEventOptions,
    _callback: DidEndDraggingMarkerCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapMyLocationButton(
    _options: DefaultEventWithPreventDefaultOptions,
    _callback: DidTapMyLocationButtonCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapMyLocationDot(
    _options: DefaultEventWithPreventDefaultOptions,
    _callback: DidTapMyLocationDotCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didTapPoi(
    _options: DefaultEventOptions,
    _callback: DidTapPoiCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didBeginMovingCamera(
    _options: DefaultEventOptions,
    _callback: DidBeginMovingCameraCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didMoveCamera(
    _options: DefaultEventOptions,
    _callback: DidMoveCameraCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async didEndMovingCamera(
    _options: DefaultEventOptions,
    _callback: DidEndMovingCameraCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }

  async elementFromPointResult(
    _options: ElementFromPointResultOptions
  ): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }
}
