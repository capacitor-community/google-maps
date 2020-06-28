<h1 align="center">Capacitor Google Maps Plugin</h1>
<p align="center">Plugin using native Maps API for Android and iOS.</p>


## Why ?

Maps SDK for Android & iOS bring better performance and offline caching compared to JS SDK and they're free to use.

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
| Location  | <h3 align="center">WIP</h3>  | <h3 align="center">WIP</h3>  | <li>android: ``enableCurrentLocation()`` ``onMyLocationButtonClick``, ``onMyLocationClick`` <li>iOS: ``enableCurrentLocation()``, ``myLocation()``| API wrapping needs improvement so that it becomes consistent for both platforms |
| Drawing on Map  | <h5 align="center">WIP</h5>  | <h5 align="center">WIP</h5>  | | Shapes, Ground Overlays, Tile Overlays
| Utility Library  | <h3 align="center">&#10005;</h3>  | <h3 align="center">&#10005;</h3>  | |
