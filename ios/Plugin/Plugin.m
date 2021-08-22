#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(CapacitorGoogleMaps, "CapacitorGoogleMaps",
           CAP_PLUGIN_METHOD(initialize, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createMap, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(updateMap, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addMarker, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(removeMarker, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(didTapMap, CAPPluginReturnCallback);
           CAP_PLUGIN_METHOD(didTapMarker, CAPPluginReturnCallback);
)
