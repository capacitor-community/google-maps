import { PatternItem } from "../../../../definitions";

// todo: AGalilov: Do we need to create a common parent interface for PolylinePreferences and PolygonProferences? I think we don't need.
export interface PolylinePreferences {
  width?: number;
  color?: string; // Color format: '#AA00FF'
  zIndex?: number;
  visibility?: boolean;
  isGeodesic?: boolean;
  isClickable?: boolean;
  pattern?: PatternItem[];
  jointType?: string; // 'BEVEL', 'DEFAULT', 'ROUND'
  // spans?: todo: AGalilov: Do we need spans? 
  metadata?: { [key: string]: any };
}
