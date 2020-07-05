import { LatLng } from "../common/latlng.interface";
import { ViewID } from "../common/view.interface";

export interface CircleOptions {
    id?: ViewID;
    center: LatLng;
    radius: number;
    strokeColor?: string;
    fillColor?: string;
    strokeWidth?: number;
    zIndex?: number;
    visibility?: boolean;
}
