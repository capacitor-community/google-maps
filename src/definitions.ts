import { PluginListenerHandle } from "@capacitor/core";

export type CallbackID = string;

export interface CapacitorGoogleMapsPlugin {
  initialize(options: InitializeOptions): Promise<void>;

  /** Creates map view and displays it */
  create(options?: CreateOptions): Promise<CreateResult>;

  elementFromPointResult(options: ElementFromPointResultOptions): Promise<void>;

  /** Adds a marker on the map */
  addMarker(options: AddMarkerOptions): Promise<MarkerResult>;

  didTapMarker(
    options: DidTapMarkerOptions,
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

export interface MarkerResult {
  position: Position;
  marker: Marker;
}

export interface DidTapMarkerOptions {
  mapId: string;
  preventDefault?: boolean;
}

export type DidTapMarkerCallback = (
  message: MarkerResult | null,
  err?: any
) => void;

export interface DidRequestElementFromPointResult {
  eventChainId: string;
  point?: {
    x: number;
    y: number;
  };
}
