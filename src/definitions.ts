import { PluginListenerHandle } from '@capacitor/core';

import { LatLng } from './types/common/latlng.interface';
import { PolylineOptions } from './types/shapes/polyline.interface';
import { PolygonOptions } from './types/shapes/polygon.interface';
import { CircleOptions } from './types/shapes/circle.interface';

export interface CapacitorGoogleMapsPlugin {
  /** Creates map view and displays it */
  create(options: {
    width: number;
    height: number;
    x: number;
    y: number;
    latitude?: number;
    longitude?: number;
    zoom?: number;
    liteMode?: boolean;
  }): Promise<any>;

  /** [iOS only] Initializes GoogleMaps with API key */
  initialize(options: { key: string }): Promise<any>;

  /** Adds a marker on the map */
  addMarker(options: {
    latitude: number;
    longitude: number;
    opacity?: number;
    title?: string;
    snippet?: string;
    isFlat?: boolean;
    iconUrl?: string;
    draggable?: boolean;
    metadata?: object;
  }): Promise<any>;

  /** Removes a marker on the map */
  removeMarker(options: { id: number }): Promise<any>;

  /** Repositions the camera */
  setCamera(options: {
    viewingAngle?: number;
    bearing?: number;
    zoom?: number;
    latitude?: number;
    longitude?: number;
    animate?: boolean;
    animationDuration?: number;
    coordinates?: LatLng[];
  }): Promise<any>;

  /** Sets the map type  */
  setMapType(options: { type: string }): Promise<any>;

  /** Allows indoor maps to be enabled or disabled  */
  setIndoorEnabled(options: { enabled: boolean }): Promise<any>;

  /** Allows traffic information to be enabled or disabled  */
  setTrafficEnabled(options: { enabled: boolean }): Promise<any>;

  /** [iOS Only] To hide accessiblity elements  */
  accessibilityElementsHidden(options: { hidden: boolean }): Promise<any>;

  /** Adds padding around the map */
  padding(options: {
    top: number;
    left: number;
    right: number;
    bottom: number;
  }): Promise<any>;

  /** Clear any views like Marker, Shapes from the map */
  clear(): Promise<any>;

  /** Destroy the mapView, use in ionViewDidLeave and similar */
  close(): Promise<any>;

  /** Hide the mapView, use when preventing any overlapping on other views */
  hide(): Promise<any>;

  /** Show the hidden mapView */
  show(): Promise<any>;

  /** Map UI Settings */
  settings(options: {
    allowScrollGesturesDuringRotateOrZoom?: boolean;
    compassButton?: boolean;
    consumesGesturesInView?: boolean;
    indoorPicker?: boolean;
    myLocationButton?: boolean;
    rotateGestures?: boolean;
    scrollGestures?: boolean;
    tiltGestures?: boolean;
    zoomGestures?: boolean;
  }): Promise<any>;

  /** Get Google Map address for a set of lat lng */
  reverseGeocodeCoordinate(options: {
    latitude: number;
    longitude: number;
  }): Promise<any>;

  /** Enable user's current location */
  enableCurrentLocation(options: { enabled: boolean }): Promise<any>;

  /** Get user location */
  myLocation(options: any): Promise<any>;

  /** Get view bounds in latlng. This polygon can be a trapezoid instead of a rectangle,
   * because a camera can have tilt. If the camera is directly over the center of the
   * camera, the shape is rectangular, but if the camera is tilted, the shape will
   * appear to be a trapezoid whose smallest side is closest to the point of view. */
  viewBounds(): Promise<any>;

  /** Add styles to map with a style JSON string format specific by Google */
  setMapStyle(options: { jsonString: string }): Promise<any>;

  /** Shapes */
  addPolyline(options: PolylineOptions): Promise<any>;
  addCircle(options: CircleOptions): Promise<any>;
  addPolygon(options: PolygonOptions): Promise<any>;

  /** Map click listeners */
  setOnMarkerClickListener(): Promise<any>;
  setOnMapClickListener(): Promise<any>;
  setOnPoiClickListener(): Promise<any>;
  requestLocationPermission(): Promise<any>;
  setOnMyLocationClickListener(): Promise<any>;
  setOnMyLocationButtonClickListener(): Promise<any>;

  addListener(
    eventName: 'didTap',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'didBeginDragging',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'didEndDragging',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'didTapAt',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'didTapPOIWithPlaceID',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'didChange',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'onMapReady',
    listenerFunc: (results: any) => void
  ): PluginListenerHandle;
}
