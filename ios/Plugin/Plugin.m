#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(CapacitorGoogleMaps, "CapacitorGoogleMaps",
           CAP_PLUGIN_METHOD(initialize, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createMap, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(updateMap, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addMarker, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addMarkers, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(updateMarker, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(removeMarker, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(didTapInfoWindow, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didCloseInfoWindow, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didTapMap, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didLongPressMap, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didTapMarker, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didTapCluster, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didTapMyLocationButton, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didTapMyLocationDot, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(blockMapViews, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(unblockMapViews, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getArrayOfHTMLElements, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setArrayOfHTMLElements, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(zoomInButtonClick, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(zoomOutButtonClick, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(myLocationButtonClick, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addMarkerCategory, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(hide, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(show, CAPPluginReturnPromise);
)
