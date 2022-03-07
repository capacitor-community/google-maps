# Setting up the WebView

The Map instances are rendered behind the WebView. Because of this, it should be made sure the native WebView and the concerned HTMLElements inside it, are transparent.

> If you want to learn more about this concept you can refer to the [advanced concepts section](advanced-concepts/transparent-webview.md)

This plugin takes care of making the native WebView and the `<html>` transparent\*. You will have to take care of the rest (the `div`'s and other HTMLElements overlaying the Map). This is a deliberate choice, because this plugin could impossibly take care of all the different project setups and an infinite number of different CSS possibilities.

!> \* The `<html>` element is made transparent by adding `background: 'transparent';` to the `style=""` attribute. So in theory it is possible that this is overwritten by some CSS property in your setup.

## Debugging

So how to go about debugging which HTMLElements are not transparent and thus blocking the Map from being viewable?

What you could do to discover what divs are responsible for it, is the following:

1. Inspect your webapp in your browser.
1. Add some very noticeable and distinguishable background color to `<html>`.
1. Then delete the most upper root `div` and see if you can see the background color of `<html>`.
1. You do? Nice. Undo the deletion and remove the child of that `div` and see if you can see the background color of `<html>`.
1. Repeat the previous step until you figured out what `div`'s are responsible for overlaying the Map.
