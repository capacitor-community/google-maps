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
<a href="#contributors-"><img src="https://img.shields.io/badge/all_contributors-18-orange.svg?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

## Purpose

Maps SDK for Android & iOS bring better performance and offline caching compared to JS SDK and they're free to use.

## Maintainers

| Maintainer | GitHub | Mail |
| -----------| -------| -------|
| Hemang Kumar | [hemangsk](https://github.com/hemangsk) | <a href="mailto:hemangsk@gmail.com">hemangsk@gmail.com</a> |

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

#### Android
[Android](https://developers.google.com/maps/documentation/android-sdk/get-api-key) in AndroidManifest.xml:
```
<application>
...

<meta-data
        android:name="com.google.android.geo.API_KEY"
        android:value="YOUR_ANDROID_MAPS_API_KEY"/>
...
</application>
```
As of [Capacitor 3](https://capacitorjs.com/docs/updating/3-0), the plugin needs to be registered in MainActivity.java:
```java
import com.hemangkumar.capacitorgooglemaps.CapacitorGoogleMaps;
//...

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // ...
    registerPlugin(CapacitorGoogleMaps.class);
  }
}

```
#### iOS
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

```html
<div id="map" #map></div>
```

`component.css`

```css
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

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://maximus.agency/"><img src="https://avatars.githubusercontent.com/u/14840021?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Grant Brits</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=gbrits" title="Code">💻</a> <a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Agbrits" title="Bug reports">🐛</a> <a href="#ideas-gbrits" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="https://github.com/tafelnl"><img src="https://avatars.githubusercontent.com/u/35837839?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Tafel</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=tafelnl" title="Code">💻</a> <a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Atafelnl" title="Bug reports">🐛</a> <a href="#ideas-tafelnl" title="Ideas, Planning, & Feedback">🤔</a></td>
    <td align="center"><a href="https://github.com/abcoskn"><img src="https://avatars.githubusercontent.com/u/6419471?v=4?s=100" width="100px;" alt=""/><br /><sub><b>abcoskn</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=abcoskn" title="Code">💻</a> <a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Aabcoskn" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/hyun-yang"><img src="https://avatars.githubusercontent.com/u/2142419?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Hyun Yang</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Ahyun-yang" title="Bug reports">🐛</a> <a href="#example-hyun-yang" title="Examples">💡</a></td>
    <td align="center"><a href="https://github.com/MelanieMarval"><img src="https://avatars.githubusercontent.com/u/43726363?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Melanie Marval</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3AMelanieMarval" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/tototares"><img src="https://avatars.githubusercontent.com/u/1064024?v=4?s=100" width="100px;" alt=""/><br /><sub><b>l4ke</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Atototares" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/quaz579"><img src="https://avatars.githubusercontent.com/u/13681950?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Ben Grossman</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Aquaz579" title="Bug reports">🐛</a></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/gerciljunio"><img src="https://avatars.githubusercontent.com/u/4561073?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Gercil Junio</b></sub></a><br /><a href="#userTesting-gerciljunio" title="User Testing">📓</a></td>
    <td align="center"><a href="https://github.com/aacassandra"><img src="https://avatars.githubusercontent.com/u/29236058?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Alauddin Afif Cassandra</b></sub></a><br /><a href="#userTesting-aacassandra" title="User Testing">📓</a></td>
    <td align="center"><a href="https://github.com/togro"><img src="https://avatars.githubusercontent.com/u/7252575?v=4?s=100" width="100px;" alt=""/><br /><sub><b>togro</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Atogro" title="Bug reports">🐛</a> <a href="#userTesting-togro" title="User Testing">📓</a></td>
    <td align="center"><a href="https://www.selectedpixel.com/"><img src="https://avatars.githubusercontent.com/u/28204537?v=4?s=100" width="100px;" alt=""/><br /><sub><b>selected-pixel-jameson</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3Aselected-pixel-jameson" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://github.com/ChiKaLiO"><img src="https://avatars.githubusercontent.com/u/12167528?v=4?s=100" width="100px;" alt=""/><br /><sub><b>chikalio</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/issues?q=author%3AChiKaLiO" title="Bug reports">🐛</a></td>
    <td align="center"><a href="https://www.tickeri.com/"><img src="https://avatars.githubusercontent.com/u/1047598?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Javier Gonzalez</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=J-Gonzalez" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/parawanderer"><img src="https://avatars.githubusercontent.com/u/37834723?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Shane B.</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=parawanderer" title="Documentation">📖</a></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/dragermrb"><img src="https://avatars.githubusercontent.com/u/11479696?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Manuel Rodríguez</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=dragermrb" title="Code">💻</a></td>
    <td align="center"><a href="https://jamilusalism.github.io/"><img src="https://avatars.githubusercontent.com/u/20730765?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Jamilu Salisu</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=jamilusalism" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/rastafan"><img src="https://avatars.githubusercontent.com/u/7632849?v=4?s=100" width="100px;" alt=""/><br /><sub><b>rastafan</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=rastafan" title="Code">💻</a></td>
    <td align="center"><a href="https://github.com/va2ron1"><img src="https://avatars.githubusercontent.com/u/303179?v=4?s=100" width="100px;" alt=""/><br /><sub><b>va2ron1</b></sub></a><br /><a href="https://github.com/capacitor-community/capacitor-googlemaps-native/commits?author=va2ron1" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
