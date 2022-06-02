import { LatLng } from '../../../../definitions';

export interface PolygonOptions {
    mapId: string;
    points: LatLng[];
    tag?: any;
    strokeColor?: string;
    fillColor?: string;
    strokeWidth?: number;
    zIndex?: number;
    visibility?: boolean;
}
