import Foundation
import Capacitor
import GoogleMaps

@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {

    var GOOGLE_MAPS_KEY: String = "";
    
    var customMapViews = [String : CustomMapView]();
    
    var customMarkers = [String : CustomMarker]();

    @objc func initialize(_ call: CAPPluginCall) {

        self.GOOGLE_MAPS_KEY = call.getString("key", "")

        if self.GOOGLE_MAPS_KEY.isEmpty {
            call.reject("GOOGLE MAPS API key missing!")
            return
        }

        GMSServices.provideAPIKey(self.GOOGLE_MAPS_KEY)
        call.resolve([
            "initialized": true
        ])
    }

    @objc func createMap(_ call: CAPPluginCall) {

        DispatchQueue.main.async {
            let customMapView : CustomMapView = CustomMapView(customMapViewEvents: self);
            
            self.bridge?.saveCall(call)
            customMapView.savedCallbackIdForCreate = call.callbackId;
            
            customMapView.mapViewBounds = [
                "width": call.getDouble("width") ?? 500,
                "height": call.getDouble("height") ?? 500,
                "x": call.getDouble("x") ?? 0,
                "y": call.getDouble("y") ?? 0,
            ]
            customMapView.cameraPosition = [
                "latitude": call.getDouble("latitude") ?? 0,
                "longitude": call.getDouble("longitude") ?? 0,
                "zoom": call.getDouble("zoom") ?? (12.0)
            ]
            
            let preferences = call.getObject("preferences");
            customMapView.mapPreferences.updateFromJSObject(preferences);
            
            self.bridge?.viewController?.view.addSubview(customMapView.view);

            customMapView.GMapView.delegate = customMapView;
            
            self.customMapViews[customMapView.id] = customMapView;
        }
    }
    
    @objc func updateMap(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId")!;

        DispatchQueue.main.async {
            let customMapView = self.customMapViews[mapId];
            
            if (customMapView != nil) {
                let preferences = call.getObject("preferences");
                customMapView?.mapPreferences.updateFromJSObject(preferences);
                
                customMapView?.invalidateMap();
            } else {
                call.reject("map not found");
            }
        }
        
    }
    
    @objc func addMarker(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");

        DispatchQueue.main.async {
            let customMapView = self.customMapViews[mapId];
            
            if (customMapView != nil) {
                let preferences = call.getObject("preferences", JSObject());
                
                let marker = CustomMarker();
                marker.updateFromJSObject(preferences: preferences);
                
                marker.map = customMapView?.GMapView;
                
                self.customMarkers[marker.id] = marker;
                
                call.resolve(CustomMarker.getResultForMarker(marker));
            } else {
                call.reject("map not found");
            }
        }
    }
    
    @objc func removeMarker(_ call: CAPPluginCall) {
        let markerId: String = call.getString("markerId", "");
        
        DispatchQueue.main.async {
            let customMarker = self.customMarkers[markerId];
            
            if (customMarker != nil) {
                customMarker?.map = nil;
                self.customMarkers[markerId] = nil;
                call.resolve();
            } else {
                call.reject("marker not found");
            }
        }
    }
    
    @objc func didTapMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MAP)
    }
    
    @objc func didTapMarker(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MARKER)
    }
    
    func setCallbackIdForEvent(call: CAPPluginCall, eventName: String) {
        call.keepAlive = true;
        let callbackId = call.callbackId;
        guard let mapId = call.getString("mapId") else { return };
        
        let customMapView: CustomMapView = customMapViews[mapId]!;
        
        let preventDefault: Bool = call.getBool("preventDefault", false);
        customMapView.setCallbackIdForEvent(callbackId: callbackId, eventName: eventName, preventDefault: preventDefault);
    }
    
    override func lastResultForCallbackId(callbackId: String, result: PluginCallResultData) {
        let call = bridge?.savedCall(withID: callbackId);
        call?.resolve(result);
        bridge?.releaseCall(call!);
    }
    
    override func resultForCallbackId(callbackId: String, result: PluginCallResultData) {
        let call = bridge?.savedCall(withID: callbackId);
        call?.resolve(result);
    }

}
