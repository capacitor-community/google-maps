#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(CapacitorGoogleMaps, "CapacitorGoogleMaps",
           CAP_PLUGIN_METHOD(initialize, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(create, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addMarker, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setMapType, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setMapStyle, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(setCamera, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(isIndoorEnabled, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(accessibilityElementsHidden, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(isMyLocationEnabled, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(padding, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(clear, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(isTrafficEnabled, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(close, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(hide, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(show, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(reverseGeocodeCoordinate, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(settings, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addPolyline, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addPolygon, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addCircle, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(enableCurrentLocation, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(myLocation, CAPPluginReturnPromise);
)
