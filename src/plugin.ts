import { Plugins } from '@capacitor/core';
const { CapacitorGoogleMaps } = Plugins;


export class GoogleMaps {

    initialize(options: {
        key: string,
    }): Promise<any> {
        return CapacitorGoogleMaps.initialize(options);
    }

    create(options: { element: HTMLElement, scrollElement: HTMLElement, latitude?: number; longitude?: number; zoom?: number; liteMode?: boolean; }): Promise<any> {

        let boundingRect = options.element.getBoundingClientRect();

        options.scrollElement.addEventListener("scroll", async () => {
            await CapacitorGoogleMaps.scrollTo({
                x: Math.round(boundingRect.x),
                y: Math.round(boundingRect.y)
            })
        })

        return CapacitorGoogleMaps.create({
            width: Math.round(boundingRect.width),
            height: Math.round(boundingRect.height),
            x: Math.round(boundingRect.x),
            y: Math.round(boundingRect.y),
            latitude: options.latitude,
            longitude: options.longitude,
            zoom: options.zoom,
            liteMode: options.liteMode,
        }) ;
    }

}