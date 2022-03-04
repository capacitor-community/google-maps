# Features that are out of scope for this plugin

There are certain features that are not baked in to this plugin. Examples are Launch Urls and (reverse) Geocoding. Several good reasons exist to not implement this in the plugin. The official Google APIs are already fairly simple and easily accessible from JavaScript. Also it would add overhead supporting this for the maintainers of the plugin. It would also require you, the user of the plugin, to learn non-standard APIs to be able to implement it.

Nonetheless we have written a few quick guides on these features to get you started

## Launch URLs

> Using Maps URLs, you can build a universal, cross-platform URL to launch Google Maps and perform searches, get directions and navigation, and display map views and panoramic images. The URL syntax is the same regardless of the platform in use.
>
> You don't need a Google API key to use Maps URLs.

<strong>Location search</strong>

In a location search, you search for a specific location using a place name, address, or comma-separated latitude/longitude coordinates, and the resulting map displays a pin at that location. These three examples illustrate searches for the same location, CenturyLink Field (a sports stadium in Seattle, WA), using different location values.

Example 1: Searching for the place name "CenturyLink Field" results in the following map:

`https://www.google.com/maps/search/?api=1&query=centurylink+field`

Example 2: Searching for CenturyLink Field using latitude/longitude coordinates as well as the place ID results in the following map:

`https://www.google.com/maps/search/?api=1&query=47.5951518%2C-122.3316393&query_place_id=ChIJKxjxuaNqkFQR3CK6O1HNNqY`

Read more about Launch URLs in the official [Google documentation](https://developers.google.com/maps/documentation/urls/get-started).

## Geocoding

<h3>Geocoding</h3>

> Geocoding is the process of converting addresses (like "1600 Amphitheatre Parkway, Mountain View, CA") into geographic coordinates (like latitude 37.423021 and longitude -122.083739), which you can use to place markers on a map, or position the map

You can start using the Geocoding API by using the API url directly. An example:

`https://maps.googleapis.com/maps/api/geocode/json?address=Washington&key=YOUR_API_KEY`

You could also use the offical JavaScript SDK like so:

```js
geocoder
  .geocode({ address: "Some address" })
  .then((response) => {
    if (response.results[0]) {
      console.log(response.results);
    } else {
      console.log("No results found");
    }
  })
  .catch((e) => console.error(e));
```

<h3>Reverse geocoding</h3>

> Reverse geocoding is the process of converting geographic coordinates into a human-readable address.

You can start using the reverse Geocoding API by using the API url directly. An example:

`https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY`

You could also use the offical JavaScript SDK like so:

```js
geocoder
  .geocode({ location: { lat: 40.731, lng: -73.997 } })
  .then((response) => {
    if (response.results[0]) {
      console.log(response.results);
    } else {
      console.log("No results found");
    }
  })
  .catch((e) => console.error(e));
```

Read more about geocoding in the official [Google documentation](https://developers.google.com/maps/documentation/geocoding/overview).
