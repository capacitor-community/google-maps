import { registerPlugin } from "@capacitor/core";
import { CapacitorGoogleMapsPlugin } from "./definitions";

const CapacitorGoogleMaps = registerPlugin<CapacitorGoogleMapsPlugin>(
  "CapacitorGoogleMaps"
);
export * from "./definitions";
export { CapacitorGoogleMaps };
