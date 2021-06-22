import { WebPlugin } from "@capacitor/core";

import {
  CapacitorGoogleMapsPlugin,
  CallbackID,
  InitializeOptions,
  CreateOptions,
  CreateResult,
  ElementFromPointResultOptions,
  AddMarkerOptions,
  MarkerAndPositionResult,
  DefaultEventOptions,
  DefaultEventWithPreventDefaultOptions,
  DidCloseInfoWindowCallback,
  DidTapMapCallback,
  DidTapMarkerCallback,
} from "./definitions";

export class CapacitorGoogleMapsWeb extends WebPlugin
  implements CapacitorGoogleMapsPlugin {
  constructor() {
    super({
      name: "CapacitorGoogleMaps",
      platforms: ["web"],
    });
  }

  async initialize(_options: InitializeOptions): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async create(_options: CreateOptions): Promise<CreateResult> {
    throw this.unimplemented("Not implemented on web.");
  }

  async elementFromPointResult(
    _options: ElementFromPointResultOptions
  ): Promise<void> {
    throw this.unimplemented("Not implemented on web.");
  }

  async addMarker(
    _options: AddMarkerOptions
  ): Promise<MarkerAndPositionResult> {
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

  async didTapMarker(
    _options: DefaultEventWithPreventDefaultOptions,
    _callback: DidTapMarkerCallback
  ): Promise<CallbackID> {
    throw this.unimplemented("Not implemented on web.");
  }
}
