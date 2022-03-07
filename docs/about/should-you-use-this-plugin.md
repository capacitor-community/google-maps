# Should you use this plugin?

<h2>Purpose</h2>

Under the hood this package makes use of the native Maps SDK for Android and iOS. The native Maps SDK has much better performance than the JS equivalent. It also adds support for offline caching. On top of that it ("Dynamic Maps") is completely free to use (as of February 2022) ([native pricing](https://developers.google.com/maps/billing-and-pricing/pricing)), in contrary to the JS SDK ([JS pricing](https://developers.google.com/maps/documentation/javascript/usage-and-billing#new-payg)).

<h2>This plugin likely <i>is</i> a good fit for you if ...</h2>

<h3>... you are building an app which main focus is the Map</h3>

If your app revolves mainly around a Map, like for example Uber or Lime, this plugin is probably a good fit for your concept.

<h3>... you have one or a few Maps in your app that need a performance boost</h3>

If your app utilizes a one or a few Map instances that can remain at its fixed position on the page (see [Limitations](/#limitations)) that need a performance boost (because for example a lot of markers are being rendered) than this plugin is probably a good fit.

<h2>This plugin likely is <i>not</i> a good fit for you if ...</h2>

<h3>... you are only using the Map to show a single static place</h3>

If your app only uses Google Maps to show a single static place (e.g. to show the location of a restaurant), you should probably use an iframe instead (which should be free to use as well). You can do a Google search on "embed Google Maps free". Or take a look a [this site](https://www.embedgooglemap.net/) for example.

<h3>... you are already using the JavaScript SDK with success</h3>

If your app already successfully uses the JavaScript SDK, and does not suffer of performance issues, it might not be worth the extra work/overhead to implement this plugin. Especially when your current/planned usage falls under the free tier the JavaScript SDK offers.
