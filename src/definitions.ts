import { PluginListenerHandle } from "@capacitor/core";

export interface CapacitorGoogleMapsPlugin {
  initialize(options: InitializeOptions): Promise<void>;

  /** Creates map view and displays it */
  create(options?: CreateOptions): Promise<CreateResult>;

  elementFromPointResult(options: ElementFromPointResultOptions): Promise<void>;

  /** Adds a marker on the map */
  addMarker(options: AddMarkerOptions): Promise<AddMarkerResult>;

  addListener(
    eventName: "didRequestElementFromPoint",
    listenerFunc: (result: DidRequestElementFromPointResult) => void
  ): PluginListenerHandle;
}

export interface Marker {
  id: string;
  title?: string;
  snippet?: string;
  opacity: number;
  isFlat: boolean;
  isDraggable: boolean;
  metadata?: object;
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
  mapId: string;
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

export interface AddMarkerResult {
  position: Position;
  marker: Marker;
}

export interface DidRequestElementFromPointResult {
  eventChainId: string;
  point?: {
    x: number;
    y: number;
  };
}
