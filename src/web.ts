import { WebPlugin } from '@capacitor/core';

import { CapacitorGoogleMapsPlugin } from './definitions';
import { LatLng } from './types/common/latlng.interface';
import { CircleOptions } from './types/shapes/circle.interface';
import { PolygonOptions } from './types/shapes/polygon.interface';
import { PolylineOptions } from './types/shapes/polyline.interface';

export class CapacitorGoogleMapsWeb extends WebPlugin implements CapacitorGoogleMapsPlugin {
  constructor() {
    super({
      name: 'CapacitorGoogleMaps',
      platforms: ['web'],
    });
  }

  create(_options: { width: number; height: number; x: number; y: number; latitude?: number; longitude?: number; zoom?: number; liteMode?: boolean }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  initialize(_options: { key: string }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  addMarker(_options: { latitude: number; longitude: number; opacity?: number; title?: string; snippet?: string; isFlat?: boolean; url?: string }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  removeMarker(_options: { id: number; }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setCamera(_options: { viewingAngle?: number; bearing?: number; zoom?: number; latitude?: number; longitude?: number; animate?: boolean; animationDuration?: number; coordinates?: LatLng[] }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setMapType(_options: { type: string }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setIndoorEnabled(_options: { enabled: boolean }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setTrafficEnabled(_options: { enabled: boolean }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  accessibilityElementsHidden(_options: { hidden: boolean }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  padding(_options: { top: number; left: number; right: number; bottom: number }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  clear(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  close(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  hide(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  show(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  settings(_options: { allowScrollGesturesDuringRotateOrZoom?: boolean; compassButton?: boolean; consumesGesturesInView?: boolean; indoorPicker?: boolean; myLocationButton?: boolean; rotateGestures?: boolean; scrollGestures?: boolean; tiltGestures?: boolean; zoomGestures?: boolean }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  reverseGeocodeCoordinate(_options: { latitude: number; longitude: number }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  enableCurrentLocation(_options: { enabled: boolean }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  myLocation(_options: any): Promise<any> {
    throw new Error('Method not implemented.');
  }
  viewBounds(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setMapStyle(_options: { jsonString: string }): Promise<any> {
    throw new Error('Method not implemented.');
  }
  addPolyline(_options: PolylineOptions): Promise<any> {
    throw new Error('Method not implemented.');
  }
  addCircle(_options: CircleOptions): Promise<any> {
    throw new Error('Method not implemented.');
  }
  addPolygon(_options: PolygonOptions): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setOnMarkerClickListener(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setOnMapClickListener(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setOnPoiClickListener(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  requestLocationPermission(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setOnMyLocationClickListener(): Promise<any> {
    throw new Error('Method not implemented.');
  }
  setOnMyLocationButtonClickListener(): Promise<any> {
    throw new Error('Method not implemented.');
  }
}
