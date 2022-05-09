import { LatLng, PatternItem, Icon } from "../../../../definitions";

export interface PolygonPreferences {
  holes?: LatLng[][];
  strokeWidth?: number;
  strokeColor?: string; // Color format: '#AA00FF'
  fillColor?: string;
  zIndex?: number;
  visibility?: boolean;
  isGeodesic?: boolean;
  isClickable?: boolean;
  strokePattern?: PatternItem[];
  strokeJointType?: string; // 'BEVEL', 'DEFAULT', 'ROUND'
  patternIcon?: Icon;
  aboveMarkers?: boolean;
  metadata?: { [key: string]: any };
}
