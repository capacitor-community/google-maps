import { registerPlugin } from "@capacitor/core";
import {
  CapacitorGoogleMapsPlugin,
  ElementFromPointResultOptions,
} from "./definitions";

const CapacitorGoogleMaps = registerPlugin<CapacitorGoogleMapsPlugin>(
  "CapacitorGoogleMaps",
  {
    web: () => import("./web").then((m) => new m.CapacitorGoogleMapsWeb()),
  }
);

CapacitorGoogleMaps.addListener("didRequestElementFromPoint", (data) => {
  const object: ElementFromPointResultOptions = {
    eventChainId: data?.eventChainId,
    mapId: null,
    isSameNode: false,
  };

  const { x, y } = data?.point || {};

  if (x && y) {
    const element = document.elementFromPoint(x, y);

    const mapId = element?.getAttribute?.("data-maps-id");
    if (mapId) {
      // if (ref.isSameNode(element)) {
      //   object.isSameNode = true;
      // }
      object.mapId = mapId;
      object.isSameNode = true;
    }
  }

  CapacitorGoogleMaps.elementFromPointResult(object);
});

export * from "./definitions";
export { CapacitorGoogleMaps };
