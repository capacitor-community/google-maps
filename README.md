<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Google Maps</h3>
<p align="center"><strong><code>@capacitor-community/capacitor-googlemaps-native</code></strong></p>
<p align="center">Capacitor Plugin using native Google Maps SDK for Android and iOS.</p>
<p align="center">
<img align="center" height="300" src="https://user-images.githubusercontent.com/13018570/86005089-cd5fe880-ba31-11ea-9e35-11a9f0e782c7.png">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Capacitor%20V3%20Support-yes-green?logo=Capacitor&style=flat-square" />
  <img src="https://img.shields.io/maintenance/yes/2021?style=flat-square" />
  <a href="https://img.shields.io/github/workflow/status/capacitor-community/capacitor-googlemaps-native/Publish"><img src="https://img.shields.io/github/workflow/status/capacitor-community/capacitor-googlemaps-native/Build?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/capacitor-googlemaps-native"><img src="https://img.shields.io/npm/l/@capacitor-community/capacitor-googlemaps-native?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/capacitor-googlemaps-native"><img src="https://img.shields.io/npm/dw/@capacitor-community/gcapacitor-googlemaps-native?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/capacitor-googlemaps-native"><img src="https://img.shields.io/npm/v/@capacitor-community/capacitor-googlemaps-native?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
<a href="#contributors-"><img src="https://img.shields.io/badge/all%20contributors-3-orange?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

## Purpose

Maps SDK for Android & iOS bring better performance and offline caching compared to JS SDK and they're free to use.

## Maintainers

| Maintainer | GitHub | Mail |
| -----------| -------| -------|
| Hemang Kumar | [hemangsk](https://github.com/hemangsk) | <a href="mailto:hemangsk@gmail.com">hemangsk@gmail.com</a> |

## Support Development
If you like this plugin and use it on your projects, please consider donating to support the development. Thank you!

<table>
        <tr>
                <td>
                        <a href="https://paypal.me/HEMANGKUMAR"><img src="https://img.shields.io/badge/Paypal-Support%20via%20Paypal-blue?style=for-the-badge&logo=paypal"></a>
                </td>
                <td>
                        <a href="https://www.buymeacoffee.com/hemang"><img src="https://img.buymeacoffee.com/button-api/?text=Buy me a coffee&emoji=&slug=hemang&button_colour=5F7FFF&font_colour=ffffff&font_family=Inter&outline_colour=000000&coffee_colour=FFDD00"></a>
                </td>
        </tr>
</table>

## Project Status

| Features  | Android | &nbsp; &nbsp; iOS &nbsp; &nbsp; | Current Status | Pending |
| ------------- | ------------- |  ------------- | ------------- | ------------- |
| Map Objects  | <h3 align="center">&#10003;</h3> | <h3 align="center">&#10003;</h3> | <li>``create()`` |
| Markers  | <h5 align="center">WIP</h5> | <h5 align="center">WIP</h5> | <li>``addMarker()`` is implemented which allows you to show a marker with default tooltip design. <li>Marker icons can be set using URL. <li>Event: ``didTap`` | Info windows |
| Business & POIs  | <h3 align="center">&#10003;</h3>  | <h3 align="center">&#10003;</h3>  | <li>Tap on any places of interest <li>Event: ``didTapPOIWithPlaceID`` |
| Lite Mode  | <h3 align="center">&#10003;</h3>  | <h3 align="center">&#10005;</h3>  | <li>``create(liteMode?: boolean)`` | Not available for iOS
| Street View  | <h3 align="center">&#10005;</h3>  | <h5 align="center">WIP</h5>  | <li>``createStreetView()``  |
| Launch URL  | <h3 align="center">&#10005;</h3>  | <h3 align="center">&#10005;</h3> | |
| Controls & Gestures  | <h5 align="center">WIP</h5>  | <h5 align="center">WIP</h5>  | <li>``settings()`` allow to set all the map UI settings. | Allow users to get current state of map settings.
| Events  | <h5 align="center">WIP</h5> | <h5 align="center">WIP</h5>  | |
| Camera & View  | <h3 align="center">&#10003;</h3>  | <h3 align="center">&#10003;</h3>  |<li>``setCamera()`` | Allow users to get current camera position
| Location  | <h5 align="center">WIP</h5>  | <h5 align="center">WIP</h5>  | <li>android: ``enableCurrentLocation()`` ``onMyLocationButtonClick``, ``onMyLocationClick`` <li>iOS: ``enableCurrentLocation()``, ``myLocation()``| API wrapping needs improvement so that it becomes consistent for both platforms |
| Drawing on Map  | <h5 align="center">WIP</h5>  | <h5 align="center">WIP</h5>  | | Shapes, Ground Overlays, Tile Overlays
| Utility Library  | <h3 align="center">&#10005;</h3>  | <h3 align="center">&#10005;</h3>  | |

## Getting Started

### Installation

#### Install package from npm
```
npm i --save @capacitor-community/capacitor-googlemaps-native
npx cap sync
```

### Set up Google API Keys

- [Android](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
- [iOS](https://developers.google.com/maps/documentation/ios-sdk/get-api-key)

You'll have two API keys by the end of this step. Lets proceed:

### Add API key to your App

- [Android](https://developers.google.com/maps/documentation/android-sdk/get-api-key) in AndroidManifest.xml:
```
<application>
...

<meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_ANDROID_MAPS_API_KEY"/>
...
</application>
```
- On iOS, this step is little different and mentioned below.

### Importing & Initializing the plugin

```javascript
import { CapacitorGoogleMaps } from '@capacitor-community/capacitor-googlemaps-native';

/* initialize() is important for iOS,
  Android doesn't need any initialization.
*/
await CapacitorGoogleMaps.initialize({
 key: "YOUR_IOS_API_KEY"
});
```

### Usage

#### An example with Angular

`component.html`

```
<div id="map" #map></div>
```

`component.css`

```
#map {
    margin: 2em 1em;
    height: 250px;
    border: 1px solid black;
  }
```

`component.ts`

```typescript
@ViewChild('map') mapView: ElementRef;

async ionViewDidEnter() {
    const boundingRect = this.mapView.nativeElement.getBoundingClientRect() as DOMRect;

    CapacitorGoogleMaps.create({
      width: Math.round(boundingRect.width),
      height: Math.round(boundingRect.height),
      x: Math.round(boundingRect.x),
      y: Math.round(boundingRect.y),
      latitude: -33.86,
      longitude: 151.20,
      zoom: 12
    });

    CapacitorGoogleMaps.addListener("onMapReady", async function() {

      /*
        We can do all the magic here when map is ready
      */

      CapacitorGoogleMaps.addMarker({
        latitude: -33.86,
        longitude: 151.20,
        title: "Custom Title",
        snippet: "Custom Snippet",
      });

      CapacitorGoogleMaps.setMapType({
        "type": "normal"
      })
    })
}

ionViewDidLeave() {
    CapacitorGoogleMaps.close();
}
```

## Known Issues

<li> Right now, its not possible to allow Map view in the template to scroll along with the Page, it remains at its fixed position.
