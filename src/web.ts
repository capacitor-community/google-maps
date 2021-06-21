import { WebPlugin } from "@capacitor/core";

import { CapacitorGoogleMapsPlugin } from "./definitions";

export class CapacitorGoogleMapsWeb extends WebPlugin
  implements CapacitorGoogleMapsPlugin {
  constructor() {
    super({
      name: "CapacitorGoogleMaps",
      platforms: ["web"],
    });
  }
}
