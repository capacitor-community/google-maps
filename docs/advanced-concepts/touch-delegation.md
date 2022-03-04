# Touch delegation

> WIP

This plugin generates native map views, and puts them behind the `WebView`. You can read more about that [here](advanced-concepts/transparent-webview.md). Because of that, advanced mechanisms are put into place, to make sure the Map is still interactable. This section explains the (advanced) techniques behind the "touch delegation" that make that possible.

Normally when a view overlaps another view in Java or Swift, the lower view is not touchable. You can compare this to the way `z-index` works in HTML.

This plugin, however, works around that by detecting the point a user touches the screen and consequently detecting whether that touch is meant for either the `WebView` or one of the Map instances.
