# Installation

## Install package from npm

```bash
npm i --save @capacitor-community/google-maps
npx cap sync
```

## Setup

### Obtain API Keys

You must add an API key for the Maps SDK to any app that uses the SDK.

Before you start using the Maps SDK, you need a project with a billing account and the Maps SDK (both for Android and iOS) enabled. Google requires you to have a valid billing account setup, although likely you will not be charged anything. See the [Should I use this plugin?](should-you-use-this-plugin.md) section for more information on pricing.

Extensive and detailed steps can be found here:

- [Android](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
- [iOS](https://developers.google.com/maps/documentation/ios-sdk/get-api-key)

You should have two API keys by the end of this step. Let's proceed.

### Adding API keys to your App

#### Android

Please follow the [official Google guide](https://developers.google.com/maps/documentation/android-sdk/config#step_2_add_your_api_key_to_the_project). See "Step 2: Add your API key to the project". You can skip Step 1, since this plugin already takes care of that.

Alternatively, you can (**but really should not**) use the following quick and dirty way:

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

Again: please **do not** use this alternative method for anything other than testing purposes, since you **will** leak your API Key.

#### iOS

On iOS, the API key needs to be set programmatically. This is done using the [`initialize`](./api.md#initialize) method.
