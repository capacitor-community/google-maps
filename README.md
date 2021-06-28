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
<a href="#contributors"><img src="https://img.shields.io/badge/all%20contributors-13-orange?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

## Purpose

Under the hood this package makes use of the native Maps SDK for Android and iOS. The native Maps SDK has much better performance than the JS equivalent. It also adds support for offline caching. On top of that it is completely free to use ([native pricing](https://developers.google.com/maps/billing/gmp-billing#maps-product)), in contrary to the JS SDK ([JS pricing](https://developers.google.com/maps/documentation/javascript/usage-and-billing#new-payg)).

## Maintainers

| Maintainer   | GitHub                                  | Mail                                                       |
| ------------ | --------------------------------------- | ---------------------------------------------------------- |
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

## Getting Started

### Installation

#### Install package from npm

```
npm i --save @capacitor-community/capacitor-googlemaps-native
npx cap sync
```

### Setup

#### Obtain API Keys

You must add an API key for the Maps SDK to any app that uses the SDK.

Before you start using the Maps SDK, you need a project with a billing account and the Maps SDK (both for Android and iOS) enabled.

Extensive and detailed steps can be found here:

- [Android](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
- [iOS](https://developers.google.com/maps/documentation/ios-sdk/get-api-key)

You should have two API keys by the end of this step. Lets proceed:

#### Adding API keys to your App

##### Android

Please follow the guide here: https://developers.google.com/maps/documentation/android-sdk/get-api-key#add_key

Alternatively, you can (but really should not) use the following quick and dirty way:

In your `AndroidManifest.xml` add the following lines:

```diff
<application>
...
+  <meta-data
+    android:name="com.google.android.geo.API_KEY"
+    android:value="YOUR_ANDROID_MAPS_API_KEY"/>
...
</application>
```

where `YOUR_ANDROID_MAPS_API_KEY` is the API key you aqcuired in the previous step.

##### iOS

On iOS, the API key needs to be set programmatically. This is done using the [`initialize`](./API_REFERENCE.md#initialize) method.

### Initializing a Map instance

After following all installation steps, you can follow this small guide to quickly setup a simple Map instance.

#### Setting up your HTML

The Maps SDK renders a native element (MapView) behind your webapp (WebView). You need to specify the boundaries (which is explained later) that is rendered in. So it is NOT an HTMLElement, but rather a native element. It can therefore not be styled, mutated or listened to like you would with a 'normal' HTMLElement.

At the moment,the only drawback of this is, that the map instance does not size and move along with the div that it is attached to. This is a known issue and it will be solved in the future as there are some known solutions as well. However, most use cases would use a Map instance that stays at a fixed position anyway.

Therefore the only requirement right now is to make sure the 'foster element' (the element which you are going to attach the Map instance to) remains at the same position. This can be achieved by preventing it to be able to scroll and to make the current view portrait (or landscape) only.

So let's get to it. Let's assume you have the following layout:

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0" />
    <title>Maps SDK for Capacitor - Basic Example</title>
    <style>
      body {
        margin: 0;
      }
      #container {
        width: 100vw;
        height: 100vh;
        background: #000;
      }
    </style>
  </head>
  <body>
    <div id="container"></div>
  </body>
</html>
```

Note that it can be anything really. Like with Capacitor itself, it does not matter what framework you are using. Angular, Vue, React, Svelte... they are all supported.

#### Setting up your JavaScript

The Plugin can be imported as follows:

```javascript
import { CapacitorGoogleMaps } from "@capacitor-community/capacitor-googlemaps-native";
```

Let's assume you have imported the Plugin correctly. A simple Maps instance can then be initialized as follows:

```javascript
const initializeMap = async () => {
  // first of all, you should initialize the Maps SDK:
  await CapacitorGoogleMaps.initialize({
    key: "YOUR_IOS_MAPS_API_KEY",
    devicePixelRatio: window.devicePixelRatio, // this line is very important
  });

  // then get the element you want to attach the Maps instance to:
  const element = document.getElementById("container");

  // afterwards get its boundaries like so:
  const boundingRect = element.getBoundingClientRect();

  // we can now create the map using the boundaries of #container
  try {
    const result = await CapacitorGoogleMaps.createMap({
      boundingRect: {
        width: Math.round(boundingRect.width),
        height: Math.round(boundingRect.height),
        x: Math.round(boundingRect.x),
        y: Math.round(boundingRect.y),
      },
    });

    // remove background, so map can be seen
    element.style.background = "";

    // finally set `data-maps-id` attribute for delegating touch events
    element.setAttribute("data-maps-id", result.googleMap.mapId);

    alert("Map loaded successfully");
  } catch (e) {
    alert("Map failed to load");
  }
};

(function () {
  // on page load, execute the above method
  initializeMap();

  // Some frameworks and a recommended lifecycle hook you could use to initialize the Map:
  // Ionic: `ionViewDidEnter`
  // Angular: `mounted`
  // Vue: `mounted`
  // React: `componentDidMount`

  // Of course you can also initialize the Map on different events, like clicking on a button.
  // Just make sure you do not unnecessarily initialize it multiple times.
})();
```

### What's next?

As the previous example shows, it is really easy to integrate the Maps SDK into your app. But, of course, there are many more possibilities.

- Refer to [API Reference](./API_REFERENCE.md) to learn more everything about the Maps API.
- Take a look at some examples [here](https://github.com/DutchConcepts/capacitor-google-maps-examples).

## Known Issues

- Right now, its not possible to allow Map view in the template to scroll along with the Page, it remains at its fixed position.

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
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
