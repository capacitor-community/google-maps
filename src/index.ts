import { registerPlugin } from "@capacitor/core";
import { CapacitorGoogleMapsPlugin } from "./definitions";

const CapacitorGoogleMaps = registerPlugin<CapacitorGoogleMapsPlugin>(
  "CapacitorGoogleMaps",
  {
    web: () => import("./web").then((m) => new m.CapacitorGoogleMapsWeb()),
  }
);

export * from "./definitions";
export { CapacitorGoogleMaps };
