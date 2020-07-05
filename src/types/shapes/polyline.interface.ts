import { LatLng } from "../common/latlng.interface";
import { ViewID } from "../common/view.interface";

export interface PolylineOptions {
    id?: ViewID;
    points: LatLng[];
    tag?: any;
    color?: string;
    width?: number;
    zIndex?: number;
    visibility?: boolean;
}
