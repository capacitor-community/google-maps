import { LatLng } from "../common/latlng.interface";
import { ViewID } from "../common/view.interface";

export interface PolygonOptions {
    id?: ViewID;
    points: LatLng[];
    tag?: any;
    strokeColor?: string;
    fillColor?: string;
    strokeWidth?: number;
    zIndex?: number;
    visibility?: boolean;
}
