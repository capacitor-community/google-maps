import { PolygonPreferences, LatLng } from "./../../definitions";

export interface AddPolygonOptions {
  mapId: string;
  points: LatLng[];
  holes?: LatLng[][];
  preferences?: PolygonPreferences[];
}
  
export interface AddPolygonResult {
 
}