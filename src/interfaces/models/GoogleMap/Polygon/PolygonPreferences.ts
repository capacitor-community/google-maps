import { LatLng } from "../../../../definitions";

export interface PolygonPreferences {
  holes?: LatLng[][];
  strokeWidth?: number;
  strokeColor?: string; // Format: '#AA00FF'
  fillColor?: string;
  zIndex?: number;
  visibility?: boolean;
  isGeodesic?: boolean;
  isClickable?: boolean;
  strokePattern?: string[];
  strokeJointType?: string;
  tag?: any;
}
