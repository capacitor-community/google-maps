<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Google Maps</h3>
<p align="center"><strong><code>@capacitor-community/google-maps</code></strong></p>
<p align="center">Capacitor Plugin using native Google Maps SDK for Android and iOS.</p>
<p align="center">
<img align="center" height="300" src="https://user-images.githubusercontent.com/13018570/86005089-cd5fe880-ba31-11ea-9e35-11a9f0e782c7.png">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/capacitor%20v3%20support-no-red?logo=Capacitor&style=flat-square" />
  <img src="https://img.shields.io/maintenance/yes/2021?style=flat-square" />
  <a href="https://github.com/capacitor-community/google-maps/actions?query=workflow%3A%22Publish%22"><img src="https://img.shields.io/github/workflow/status/capacitor-community/google-maps/Test%20and%20Build%20Plugin?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/google-maps"><img src="https://img.shields.io/npm/l/@capacitor-community/google-maps?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/google-maps"><img src="https://img.shields.io/npm/dw/@capacitor-community/google-maps?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/google-maps"><img src="https://img.shields.io/npm/v/@capacitor-community/google-maps?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
<a href="#contributors-"><img src="https://img.shields.io/badge/all%20contributors-3-orange?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

## Maintainers

| Maintainer | GitHub | Social |
| -----------| -------| -------|
| Hemang Kumar | [hemangsk](https://github.com/hemangsk) | [@hemang_kumar](https://twitter.com/hemang_kumar) |

## Installation

```bash
npm i --save capacitor-googlemaps-native
npx cap sync
```

## Configuration

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

### Setup Plugin on Android

`your-plugin/android/src/main/java/MainActivity.java`
```java
...
/* Import */
import com.hemangkumar.capacitorgooglemaps.CapacitorGoogleMaps;
...

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

      this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{

      /* Add Plugin Class */
      add(CapacitorGoogleMaps.class);


     }});
  }
}

```
### Importing & Initializing the plugin

```javascript
const { CapacitorGoogleMaps } = Plugins;

/* initialize() is important for iOS,
  Android doesn't need any initialization.
*/
await CapacitorGoogleMaps.initialize({
 key: "YOUR_IOS_API_KEY"
});
```

## Usage

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

## API

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

## Known Issues

<li> Right now, its not possible to allow Map view in the template to scroll along with the Page, it remains at its fixed position.
