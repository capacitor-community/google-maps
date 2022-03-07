# Transparent WebView

Whenever [`createMap`](api.md#createmap) is being called, a native Google Maps instance ([Android](https://developers.google.com/android/reference/com/google/android/gms/maps/MapView), [iOS](https://developers.google.com/maps/documentation/ios-sdk/reference/interface_g_m_s_map_view)) is added to the app. Such an instance is a native view which thus lives in the native side of the code base (Java and Swift). This means that they are **not** HTMLElements, **not** a `<div>` or anything HTML related.

These view instances are being rendered **behind** the `WebView` ([Android](https://developer.android.com/reference/android/webkit/WebView), [iOS](https://developer.apple.com/documentation/webkit/wkwebview)). The `WebView` itself is made transparent. This has the major benefit that you can render any kind of HTMLElement on top of the native Map.

> Read more about how we managed to make both the Map instances and `WebView` interactable in [this section](advanced-concepts/touch-delegation.md).

<h2>Android</h2>

On Android this plugin makes sure that the `WebView` overlays the Map by this piece of code:

```Java
bridge.getWebView().bringToFront();
```

Simple yet effective.

Since the `WebView` has a background color by default, the plugin also makes sure the background is transparent by the following code snippet:

```Java
bridge.getWebView().setBackgroundColor(Color.TRANSPARENT);
```

Of course the webapp itself also may have elements that are not transparent by default. An example of this is the `<html>` element. The plugin automatically tries to make the `<html>` element transparent by adding `background: 'transparent';` to the `style=""` attribute\*.

This is done by this piece of code:

```Java
bridge.getWebView().loadUrl("javascript:document.documentElement.style.backgroundColor = 'transparent';void(0);");
```

!> \* This means in theory it is possible that this is overwritten by some CSS property in your setup. Learn how to make sure the Map is viewable in [this guide](guide/setup-webview.md).

<h2>iOS</h2>

On iOS this plugin makes sure that the `WebView` overlays the Map by "sending" the Map view to the back:

```swift
self.bridge?.viewController?.view.sendSubviewToBack(customMapView.view)
```

Simple yet effective.

Since the `WebView` has a background color by default, the plugin also makes sure the background is transparent by the following code snippet:

```Swift
self.customWebView?.isOpaque = false
self.customWebView?.backgroundColor = .clear
```

Of course the webapp itself also may have elements that are not transparent by default. An example of this is the `<html>` element. The plugin automatically tries to make the `<html>` element transparent by adding `background: 'transparent';` to the `style=""` attribute\*.

This is done by this piece of code:

```Swift
let javascript = "document.documentElement.style.backgroundColor = 'transparent'"
self.customWebView?.evaluateJavaScript(javascript)
```

!> \* This means in theory it is possible that this is overwritten by some CSS property in your setup. Learn how to make sure the Map is viewable in [this guide](guide/setup-webview.md).
