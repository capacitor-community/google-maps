import { LatLng } from '../../../../definitions';

export interface PolylineOptions {
    mapId: string;
    points: LatLng[];
    tag?: any;
    color?: string;
    width?: number;
    zIndex?: number;
    visibility?: boolean;
}
