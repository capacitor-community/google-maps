import { LatLng } from '../../../../definitions';

export interface CircleOptions {
    mapId: string;
    center: LatLng;
    radius: number;
    strokeColor?: string;
    fillColor?: string;
    strokeWidth?: number;
    zIndex?: number;
    visibility?: boolean;
}