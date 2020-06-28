import { WebPlugin } from '@capacitor/core';


export class CapacitorGoogleMapsWeb extends WebPlugin {
  constructor() {
    super({
      name: 'CapacitorGoogleMaps',
      platforms: ['web']
    });
  }
}

const CapacitorGoogleMaps = new CapacitorGoogleMapsWeb();

export { CapacitorGoogleMaps };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(CapacitorGoogleMaps);
