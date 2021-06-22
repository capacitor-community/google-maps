import { PluginListenerHandle } from "@capacitor/core";

export type CallbackID = string;

export interface CapacitorGoogleMapsPlugin {
  initialize(options: InitializeOptions): Promise<void>;

  /** Creates map view and displays it */
  create(options?: CreateOptions): Promise<CreateResult>;

  elementFromPointResult(options: ElementFromPointResultOptions): Promise<void>;

  /** Adds a marker on the map */
  addMarker(options: AddMarkerOptions): Promise<MarkerAndPositionResult>;

  didCloseInfoWindow(
    options: DefaultEventOptions,
    callback: DidCloseInfoWindowCallback
  ): Promise<CallbackID>;

  didTapMap(
    options: DefaultEventOptions,
    callback: DidTapMapCallback
  ): Promise<CallbackID>;

  didTapMarker(
    options: DefaultEventWithPreventDefaultOptions,
    callback: DidTapMarkerCallback
  ): Promise<CallbackID>;

  addListener(
    eventName: "didRequestElementFromPoint",
    listenerFunc: (result: DidRequestElementFromPointResult) => void
  ): PluginListenerHandle;
}

export interface Marker {
  id: string;
  title: string;
  snippet: string;
  opacity: number;
  isFlat: boolean;
  isDraggable: boolean;
  metadata: object;
}

export interface Position {
  latitude: number;
  longitude: number;
}

export interface InitializeOptions {
  devicePixelRatio: number;
  key?: string /* Only required on iOS */;
}

export interface CreateOptions {
  latitude?: number;
  longitude?: number;
  zoom?: number;
  liteMode?: boolean;
  width?: number;
  height?: number;
  x?: number;
  y?: number;
}

export interface CreateResult {
  mapId: string;
}

export interface ElementFromPointResultOptions {
  eventChainId: string;
  mapId: string | null;
  isSameNode: boolean;
}

export interface AddMarkerOptions {
  mapId: string;
  latitude: number;
  longitude: number;
  title?: string;
  snippet?: string;
  opacity?: number;
  isFlat?: boolean;
  isDraggable?: boolean;
  metadata?: object;
}

export interface MarkerAndPositionResult {
  position: Position;
  marker: Marker;
}

export interface PositionResult {
  position: Position;
}

export interface DefaultEventOptions {
  mapId: string;
}

export interface DefaultEventWithPreventDefaultOptions {
  mapId: string;
  preventDefault?: boolean;
}

export type DidCloseInfoWindowCallback = (
  message: MarkerAndPositionResult | null,
  err?: any
) => void;

export type DidTapMapCallback = (
  message: PositionResult | null,
  err?: any
) => void;

export type DidTapMarkerCallback = (
  message: MarkerAndPositionResult | null,
  err?: any
) => void;

export interface DidRequestElementFromPointResult {
  eventChainId: string;
  point?: {
    x: number;
    y: number;
  };
}
