import { PatternItem } from "../../../../definitions";

export interface CirclePreferences {
    strokeWidth?: number;
    strokeColor?: string; // Color format: '#AA00FF'
    fillColor?: string;
    zIndex?: number;
    visibility?: boolean;
    isClickable?: boolean;
    strokePattern?: PatternItem[];
    strokeJointType?: string; // 'BEVEL', 'DEFAULT', 'ROUND'
    metadata?: { [key: string]: any };
  }
  