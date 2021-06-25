# API Reference 🔌

Below is an index of all the methods available.

<docgen-index>

- [`initialize(...)`](#initialize)
- [`createMap(...)`](#createmap)
- [`updateMap(...)`](#updatemap)
- [`moveCamera(...)`](#movecamera)
- [`addMarker(...)`](#addmarker)
- [`removeMarker(...)`](#removemarker)
- [`didCloseInfoWindow(...)`](#didcloseinfowindow)
- [`didTapMap(...)`](#didtapmap)
- [`didTapMarker(...)`](#didtapmarker)
- [`elementFromPointResult(...)`](#elementfrompointresult)
- [`addListener('didRequestElementFromPoint', ...)`](#addlistenerdidrequestelementfrompoint-)
- [Interfaces](#interfaces)
- [Type Aliases](#type-aliases)
- [Enums](#enums)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### initialize(...)

```typescript
initialize(options: InitializeOptions) => Promise<void>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#initializeoptions">InitializeOptions</a></code> |

---

### createMap(...)

```typescript
createMap(options: CreateMapOptions) => Promise<CreateMapResult>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#createmapoptions">CreateMapOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#createmapresult">CreateMapResult</a>&gt;</code>

---

### updateMap(...)

```typescript
updateMap(options: UpdateMapOptions) => Promise<UpdateMapResult>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#updatemapoptions">UpdateMapOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#updatemapresult">UpdateMapResult</a>&gt;</code>

---

### moveCamera(...)

```typescript
moveCamera(options: MoveCameraOptions) => Promise<MoveCameraResult>
```

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`options`** | <code><a href="#movecameraoptions">MoveCameraOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#movecameraresult">MoveCameraResult</a>&gt;</code>

---

### addMarker(...)

```typescript
addMarker(options: AddMarkerOptions) => Promise<AddMarkerResult>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#addmarkeroptions">AddMarkerOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#addmarkerresult">AddMarkerResult</a>&gt;</code>

---

### removeMarker(...)

```typescript
removeMarker(markerId: string) => Promise<void>
```

| Param          | Type                |
| -------------- | ------------------- |
| **`markerId`** | <code>string</code> |

---

### didCloseInfoWindow(...)

```typescript
didCloseInfoWindow(options: DefaultEventOptions, callback: DidCloseInfoWindowCallback) => Promise<CallbackID>
```

| Param          | Type                                                                              |
| -------------- | --------------------------------------------------------------------------------- |
| **`options`**  | <code><a href="#defaulteventoptions">DefaultEventOptions</a></code>               |
| **`callback`** | <code><a href="#didcloseinfowindowcallback">DidCloseInfoWindowCallback</a></code> |

**Returns:** <code>Promise&lt;string&gt;</code>

---

### didTapMap(...)

```typescript
didTapMap(options: DefaultEventOptions, callback: DidTapMapCallback) => Promise<CallbackID>
```

| Param          | Type                                                                |
| -------------- | ------------------------------------------------------------------- |
| **`options`**  | <code><a href="#defaulteventoptions">DefaultEventOptions</a></code> |
| **`callback`** | <code><a href="#didtapmapcallback">DidTapMapCallback</a></code>     |

**Returns:** <code>Promise&lt;string&gt;</code>

---

### didTapMarker(...)

```typescript
didTapMarker(options: DefaultEventWithPreventDefaultOptions, callback: DidTapMarkerCallback) => Promise<CallbackID>
```

| Param          | Type                                                                                                    |
| -------------- | ------------------------------------------------------------------------------------------------------- |
| **`options`**  | <code><a href="#defaulteventwithpreventdefaultoptions">DefaultEventWithPreventDefaultOptions</a></code> |
| **`callback`** | <code><a href="#didtapmarkercallback">DidTapMarkerCallback</a></code>                                   |

**Returns:** <code>Promise&lt;string&gt;</code>

---

### elementFromPointResult(...)

```typescript
elementFromPointResult(options: ElementFromPointResultOptions) => Promise<void>
```

After `didRequestElementFromPoint` fires, this method is used to let the WebView know whether or not to delegate the touch event to a certain MapView.
It is handled automatically and you should probably not use it.

| Param         | Type                                                                                    |
| ------------- | --------------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#elementfrompointresultoptions">ElementFromPointResultOptions</a></code> |

---

### addListener('didRequestElementFromPoint', ...)

```typescript
addListener(eventName: "didRequestElementFromPoint", listenerFunc: (result: DidRequestElementFromPointResult) => void) => PluginListenerHandle
```

This listens for touch events on the WebView.
It is handled automatically and you should probably not use it.

| Param              | Type                                                                                                               |
| ------------------ | ------------------------------------------------------------------------------------------------------------------ |
| **`eventName`**    | <code>'didRequestElementFromPoint'</code>                                                                          |
| **`listenerFunc`** | <code>(result: <a href="#didrequestelementfrompointresult">DidRequestElementFromPointResult</a>) =&gt; void</code> |

**Returns:** <code><a href="#pluginlistenerhandle">PluginListenerHandle</a></code>

---

### Interfaces

#### InitializeOptions

| Prop                   | Type                | Description                                                                                                                                                                                                                                             | Since |
| ---------------------- | ------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| **`devicePixelRatio`** | <code>number</code> | Defines the pixel ratio of the current device in the current state. Recommended to be set by using `window.devicePixelRatio`. This is needed because real pixels on a device do not necessarily correspond with how pixels are calculated in a WebView. | 2.0.0 |
| **`key`**              | <code>string</code> | (iOS only) API Key for Google Maps SDK for iOS. Hence it is only required on iOS.                                                                                                                                                                       | 1.0.0 |

#### CreateMapResult

| Prop            | Type                                            | Since |
| --------------- | ----------------------------------------------- | ----- |
| **`googleMap`** | <code><a href="#googlemap">GoogleMap</a></code> | 2.0.0 |

#### GoogleMap

| Prop                 | Type                                                      | Description                                      | Since |
| -------------------- | --------------------------------------------------------- | ------------------------------------------------ | ----- |
| **`mapId`**          | <code>string</code>                                       | GUID representing the unique id of this map      | 2.0.0 |
| **`cameraPosition`** | <code><a href="#cameraposition">CameraPosition</a></code> | See <a href="#cameraposition">CameraPosition</a> | 2.0.0 |
| **`preferences`**    | <code><a href="#mappreferences">MapPreferences</a></code> | See <a href="#mappreferences">MapPreferences</a> | 2.0.0 |

#### CameraPosition

The map view is modeled as a camera looking down on a flat plane.
The position of the camera (and hence the rendering of the map) is specified by the following properties: target (latitude/longitude location), bearing, tilt, and zoom.
More information can be found here: https://developers.google.com/maps/documentation/android-sdk/views#the_camera_position

| Prop          | Type                                      | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Since |
| ------------- | ----------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| **`target`**  | <code><a href="#latlng">LatLng</a></code> | The camera target is the location of the center of the map, specified as latitude and longitude co-ordinates.                                                                                                                                                                                                                                                                                                                                                                           | 2.0.0 |
| **`bearing`** | <code>number</code>                       | The camera bearing is the direction in which a vertical line on the map points, measured in degrees clockwise from north. Someone driving a car often turns a road map to align it with their direction of travel, while hikers using a map and compass usually orient the map so that a vertical line is pointing north. The Maps API lets you change a map's alignment or bearing. For example, a bearing of 90 degrees results in a map where the upwards direction points due east. | 2.0.0 |
| **`tilt`**    | <code>number</code>                       | The tilt defines the camera's position on an arc between directly over the map's center position and the surface of the Earth, measured in degrees from the nadir (the direction pointing directly below the camera). When you change the viewing angle, the map appears in perspective, with far-away features appearing smaller, and nearby features appearing larger.                                                                                                                | 2.0.0 |
| **`zoom`**    | <code>number</code>                       | The zoom level of the camera determines the scale of the map. At larger zoom levels more detail can be seen on the screen, while at smaller zoom levels more of the world can be seen on the screen.                                                                                                                                                                                                                                                                                    | 2.0.0 |

#### LatLng

A data class representing a pair of latitude and longitude coordinates, stored as degrees.

| Prop            | Type                | Description                                                    | Since |
| --------------- | ------------------- | -------------------------------------------------------------- | ----- |
| **`latitude`**  | <code>number</code> | Latitude, in degrees. This value is in the range [-90, 90].    | 2.0.0 |
| **`longitude`** | <code>number</code> | Longitude, in degrees. This value is in the range [-180, 180]. | 2.0.0 |

#### MapPreferences

| Prop             | Type                                                    | Description                                    | Since |
| ---------------- | ------------------------------------------------------- | ---------------------------------------------- | ----- |
| **`gestures`**   | <code><a href="#mapgestures">MapGestures</a></code>     | See <a href="#mapgestures">MapGestures</a>     | 2.0.0 |
| **`controls`**   | <code><a href="#mapcontrols">MapControls</a></code>     | See <a href="#mapcontrols">MapControls</a>     | 2.0.0 |
| **`appearance`** | <code><a href="#mapappearance">MapAppearance</a></code> | See <a href="#mapappearance">MapAppearance</a> | 2.0.0 |
| **`maxZoom`**    | <code>number</code>                                     |                                                |       |
| **`minZoom`**    | <code>number</code>                                     |                                                |       |
| **`padding`**    | <code>any</code>                                        |                                                |       |
| **`liteMode`**   | <code>boolean</code>                                    |                                                |       |

#### MapGestures

Aggregates all gesture parameters such as allowing for rotating, scrolling, tilting and zooming the map.

| Prop                                    | Type                 | Description                                                                                                                                                                                                                                                                                                                                                                                                                             | Default           | Since |
| --------------------------------------- | -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------- | ----- |
| **`isRotateAllowed`**                   | <code>boolean</code> | If `true`, rotate gestures are allowed. If enabled, users can use a two-finger rotate gesture to rotate the camera. If disabled, users cannot rotate the camera via gestures. This setting doesn't restrict the user from tapping the compass button to reset the camera orientation, nor does it restrict programmatic movements and animation of the camera.                                                                          | <code>true</code> | 2.0.0 |
| **`isScrollAllowed`**                   | <code>boolean</code> | If `true`, scroll gestures are allowed. If enabled, users can swipe to pan the camera. If disabled, swiping has no effect. This setting doesn't restrict programmatic movement and animation of the camera.                                                                                                                                                                                                                             | <code>true</code> | 2.0.0 |
| **`isScrollAllowedDuringRotateOrZoom`** | <code>boolean</code> | If `true`, scroll gestures can take place at the same time as a zoom or rotate gesture. If enabled, users can scroll the map while rotating or zooming the map. If disabled, the map cannot be scrolled while the user rotates or zooms the map using gestures. This setting doesn't disable scroll gestures entirely, only during rotation and zoom gestures, nor does it restrict programmatic movements and animation of the camera. | <code>true</code> | 2.0.0 |
| **`isTiltAllowed`**                     | <code>boolean</code> | If `true`, tilt gestures are allowed. If enabled, users can use a two-finger vertical down swipe to tilt the camera. If disabled, users cannot tilt the camera via gestures. This setting doesn't restrict users from tapping the compass button to reset the camera orientation, nor does it restrict programmatic movement and animation of the camera.                                                                               | <code>true</code> | 2.0.0 |
| **`isZoomAllowed`**                     | <code>boolean</code> | If `true`, zoom gestures are allowed. If enabled, users can either double tap/two-finger tap or pinch to zoom the camera. If disabled, these gestures have no effect. This setting doesn't affect the zoom buttons, nor does it restrict programmatic movement and animation of the camera.                                                                                                                                             | <code>true</code> | 2.0.0 |

#### MapControls

Aggregates all control parameters such as enabling the compass, my-location and zoom buttons as well as the toolbar.

| Prop                            | Type                 | Description                                                                                                                                                                                                                                                                                                                                                                                                | Default            | Since |
| ------------------------------- | -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------ | ----- |
| **`isCompassButtonEnabled`**    | <code>boolean</code> | If `true`, the compass button is enabled. The compass is an icon on the map that indicates the direction of north on the map. If enabled, it is only shown when the camera is rotated away from its default orientation (bearing of 0). When a user taps the compass, the camera orients itself to its default orientation and fades away shortly after. If disabled, the compass will never be displayed. | <code>true</code>  | 2.0.0 |
| **`isMapToolbarEnabled`**       | <code>boolean</code> | (Android only) If `true`, the Map Toolbar is enabled. If enabled, and the Map Toolbar can be shown in the current context, users will see a bar with various context-dependent actions, including 'open this map in the Google Maps app' and 'find directions to the highlighted marker in the Google Maps app'.                                                                                           | <code>false</code> | 2.0.0 |
| **`isMyLocationButtonEnabled`** | <code>boolean</code> | If `true`, the my-location button is enabled. This is a button visible on the map that, when tapped by users, will center the map on the current user location. If the button is enabled, it is only shown when <a href="#mapappearance">`MapAppearance.isMyLocationDotShown</a> === true`.                                                                                                                | <code>true</code>  | 2.0.0 |
| **`isZoomButtonsEnabled`**      | <code>boolean</code> | (Android only) If `true`, the zoom controls are enabled. The zoom controls are a pair of buttons (one for zooming in, one for zooming out) that appear on the screen when enabled. When pressed, they cause the camera to zoom in (or out) by one zoom level. If disabled, the zoom controls are not shown.                                                                                                | <code>false</code> | 2.0.0 |

#### MapAppearance

Aggregates all appearance parameters such as showing 3d building, indoor maps, the my-location (blue) dot and traffic.
Additionally, it also holds parameters such as the type of map tiles and the overall styling of the base map.

| Prop                       | Type                                        | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Default                     | Since |
| -------------------------- | ------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------- | ----- |
| **`type`**                 | <code><a href="#maptype">MapType</a></code> | Controls the type of map tiles that should be displayed.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | <code>MapType.Normal</code> | 2.0.0 |
| **`style`**                | <code><a href="#json">JSON</a></code>       | Holds details about a style which can be applied to a map. When set to `null` the default styling will be used. With style options you can customize the presentation of the standard Google map styles, changing the visual display of features like roads, parks, and other points of interest. As well as changing the style of these features, you can also hide features entirely. This means that you can emphasize particular components of the map or make the map complement the content of your app. For more information check: https://developers.google.com/maps/documentation/ios-sdk/style-reference Or use the wizard for generating <a href="#json">JSON</a>: https://mapstyle.withgoogle.com/ | <code>null</code>           | 2.0.0 |
| **`isBuildingsShown`**     | <code>boolean</code>                        | If `true`, 3D buildings will be shown where available.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | <code>true</code>           | 2.0.0 |
| **`isIndoorShown`**        | <code>boolean</code>                        | If `true`, indoor maps are shown, where available. If this is set to false, caches for indoor data may be purged and any floor currently selected by the end-user may be reset.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | <code>true</code>           | 2.0.0 |
| **`isMyLocationDotShown`** | <code>boolean</code>                        | If `true`, the my-location (blue) dot and accuracy circle are shown.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            | <code>false</code>          | 2.0.0 |
| **`isTrafficShown`**       | <code>boolean</code>                        | If `true`, the map draws traffic data, if available. This is subject to the availability of traffic data.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | <code>false</code>          | 2.0.0 |

#### JSON

An intrinsic object that provides functions to convert JavaScript values to and from the JavaScript Object Notation (<a href="#json">JSON</a>) format.

| Method        | Signature                                                                                                      | Description                                                                                    |
| ------------- | -------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| **parse**     | (text: string, reviver?: (this: any, key: string, value: any) =&gt; any) =&gt; any                             | Converts a JavaScript Object Notation (<a href="#json">JSON</a>) string into an object.        |
| **stringify** | (value: any, replacer?: (this: any, key: string, value: any) =&gt; any, space?: string \| number) =&gt; string | Converts a JavaScript value to a JavaScript Object Notation (<a href="#json">JSON</a>) string. |
| **stringify** | (value: any, replacer?: (number \| string)[] \| null, space?: string \| number) =&gt; string                   | Converts a JavaScript value to a JavaScript Object Notation (<a href="#json">JSON</a>) string. |

#### CreateMapOptions

| Prop                 | Type                                                      | Description                                      | Since |
| -------------------- | --------------------------------------------------------- | ------------------------------------------------ | ----- |
| **`element`**        | <code>HTMLElement</code>                                  |                                                  | 2.0.0 |
| **`boundingRect`**   | <code><a href="#boundingrect">BoundingRect</a></code>     |                                                  | 2.0.0 |
| **`cameraPosition`** | <code><a href="#cameraposition">CameraPosition</a></code> | See <a href="#cameraposition">CameraPosition</a> | 2.0.0 |
| **`preferences`**    | <code><a href="#mappreferences">MapPreferences</a></code> |                                                  | 2.0.0 |

#### BoundingRect

| Prop         | Type                |
| ------------ | ------------------- |
| **`width`**  | <code>number</code> |
| **`height`** | <code>number</code> |
| **`x`**      | <code>number</code> |
| **`y`**      | <code>number</code> |

#### UpdateMapResult

| Prop            | Type                                            | Since |
| --------------- | ----------------------------------------------- | ----- |
| **`googleMap`** | <code><a href="#googlemap">GoogleMap</a></code> | 2.0.0 |

#### UpdateMapOptions

| Prop               | Type                                                      | Since |
| ------------------ | --------------------------------------------------------- | ----- |
| **`mapId`**        | <code>string</code>                                       | 2.0.0 |
| **`element`**      | <code>HTMLElement</code>                                  | 2.0.0 |
| **`boundingRect`** | <code><a href="#boundingrect">BoundingRect</a></code>     | 2.0.0 |
| **`preferences`**  | <code><a href="#mappreferences">MapPreferences</a></code> | 2.0.0 |

#### MoveCameraResult

| Prop            | Type                                            | Since |
| --------------- | ----------------------------------------------- | ----- |
| **`googleMap`** | <code><a href="#googlemap">GoogleMap</a></code> | 2.0.0 |

#### MoveCameraOptions

| Prop                 | Type                                                      | Description                                                                                                                         | Default        | Since |
| -------------------- | --------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------- | -------------- | ----- |
| **`mapId`**          | <code>string</code>                                       |                                                                                                                                     |                | 2.0.0 |
| **`cameraPosition`** | <code><a href="#cameraposition">CameraPosition</a></code> | See <a href="#cameraposition">CameraPosition</a>                                                                                    |                | 2.0.0 |
| **`duration`**       | <code>number</code>                                       | The duration of the animation in milliseconds. If not specified, or equals or smaller than 0, the camera movement will be immediate | <code>0</code> | 2.0.0 |

#### AddMarkerResult

| Prop           | Type                                      | Since |
| -------------- | ----------------------------------------- | ----- |
| **`marker`**   | <code><a href="#marker">Marker</a></code> | 2.0.0 |
| **`position`** | <code><a href="#latlng">LatLng</a></code> | 2.0.0 |

#### Marker

| Prop              | Type                 | Description                                                                                                                                                                                                                                                                                               | Since |
| ----------------- | -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----- |
| **`markerId`**    | <code>string</code>  | GUID representing the unique id of this marker                                                                                                                                                                                                                                                            | 2.0.0 |
| **`title`**       | <code>string</code>  | A text string that's displayed in an info window when the user taps the marker. You can change this value at any time.                                                                                                                                                                                    | 2.0.0 |
| **`snippet`**     | <code>string</code>  | Additional text that's displayed below the title. You can change this value at any time.                                                                                                                                                                                                                  | 2.0.0 |
| **`opacity`**     | <code>number</code>  | This is a value from 0 to 1, where 0 means the marker is completely transparent and 1 means the marker is completely opaque.                                                                                                                                                                              | 2.0.0 |
| **`isFlat`**      | <code>boolean</code> | Controls whether this marker should be flat against the Earth's surface (`true`) or a billboard facing the camera (`false`).                                                                                                                                                                              | 2.0.0 |
| **`isDraggable`** | <code>boolean</code> | Controls whether this marker can be dragged interactively. When a marker is draggable, it can be moved by the user by long pressing on the marker.                                                                                                                                                        | 2.0.0 |
| **`metadata`**    | <code>object</code>  | You can use this property to associate an arbitrary object with this overlay. The Google Maps SDK neither reads nor writes this property. Note that metadata should not hold any strong references to any Maps objects, otherwise a retain cycle may be created (preventing objects from being released). | 2.0.0 |

#### AddMarkerOptions

| Prop              | Type                                                            | Since |
| ----------------- | --------------------------------------------------------------- | ----- |
| **`mapId`**       | <code>string</code>                                             | 2.0.0 |
| **`preferences`** | <code><a href="#markerpreferences">MarkerPreferences</a></code> | 2.0.0 |
| **`position`**    | <code><a href="#latlng">LatLng</a></code>                       | 2.0.0 |

#### MarkerPreferences

| Prop              | Type                 | Description                                                                                                                                                                                                                                                                                               | Default            | Since |
| ----------------- | -------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------ | ----- |
| **`title`**       | <code>string</code>  | A text string that's displayed in an info window when the user taps the marker. You can change this value at any time.                                                                                                                                                                                    | <code>null</code>  | 2.0.0 |
| **`snippet`**     | <code>string</code>  | Additional text that's displayed below the title. You can change this value at any time.                                                                                                                                                                                                                  | <code>null</code>  | 2.0.0 |
| **`opacity`**     | <code>number</code>  | This is a value from 0 to 1, where 0 means the marker is completely transparent and 1 means the marker is completely opaque.                                                                                                                                                                              | <code>1</code>     | 2.0.0 |
| **`isFlat`**      | <code>boolean</code> | Controls whether this marker should be flat against the Earth's surface (`true`) or a billboard facing the camera (`false`).                                                                                                                                                                              | <code>false</code> | 2.0.0 |
| **`isDraggable`** | <code>boolean</code> | Controls whether this marker can be dragged interactively. When a marker is draggable, it can be moved by the user by long pressing on the marker.                                                                                                                                                        | <code>false</code> | 2.0.0 |
| **`metadata`**    | <code>object</code>  | You can use this property to associate an arbitrary object with this overlay. The Google Maps SDK neither reads nor writes this property. Note that metadata should not hold any strong references to any Maps objects, otherwise a retain cycle may be created (preventing objects from being released). | <code>{}</code>    | 2.0.0 |

#### DefaultEventOptions

| Prop        | Type                |
| ----------- | ------------------- |
| **`mapId`** | <code>string</code> |

#### DidCloseInfoWindowResult

| Prop           | Type                                      |
| -------------- | ----------------------------------------- |
| **`position`** | <code><a href="#latlng">LatLng</a></code> |
| **`marker`**   | <code><a href="#marker">Marker</a></code> |

#### DidTapMapResult

| Prop           | Type                                      |
| -------------- | ----------------------------------------- |
| **`position`** | <code><a href="#latlng">LatLng</a></code> |

#### DefaultEventWithPreventDefaultOptions

| Prop                 | Type                 |
| -------------------- | -------------------- |
| **`mapId`**          | <code>string</code>  |
| **`preventDefault`** | <code>boolean</code> |

#### DidTapMarkerResult

| Prop           | Type                                      |
| -------------- | ----------------------------------------- |
| **`position`** | <code><a href="#latlng">LatLng</a></code> |
| **`marker`**   | <code><a href="#marker">Marker</a></code> |

#### ElementFromPointResultOptions

| Prop               | Type                 |
| ------------------ | -------------------- |
| **`eventChainId`** | <code>string</code>  |
| **`mapId`**        | <code>string</code>  |
| **`isSameNode`**   | <code>boolean</code> |

#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |

#### DidRequestElementFromPointResult

| Prop               | Type                                   |
| ------------------ | -------------------------------------- |
| **`eventChainId`** | <code>string</code>                    |
| **`point`**        | <code>{ x: number; y: number; }</code> |

### Type Aliases

#### DidCloseInfoWindowCallback

<code>(result: <a href="#didcloseinfowindowresult">DidCloseInfoWindowResult</a>, err?: any): void</code>

#### CallbackID

<code>string</code>

#### DidTapMapCallback

<code>(result: <a href="#didtapmapresult">DidTapMapResult</a>, err?: any): void</code>

#### DidTapMarkerCallback

<code>(result: <a href="#didtapmarkerresult">DidTapMarkerResult</a>, err?: any): void</code>

### Enums

#### MapType

| Members         | Value          | Description                              | Since |
| --------------- | -------------- | ---------------------------------------- | ----- |
| **`None`**      | <code>0</code> | No base map tiles.                       | 2.0.0 |
| **`Normal`**    | <code>1</code> | Basic map.                               | 2.0.0 |
| **`Satellite`** | <code>2</code> | Satellite imagery with no labels.        | 2.0.0 |
| **`Terrain`**   | <code>3</code> | Topographic data.                        | 2.0.0 |
| **`Hybrid`**    | <code>4</code> | Satellite imagery with roads and labels. | 2.0.0 |

</docgen-api>
