import Foundation
import Capacitor
import GoogleMaps

@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {

    var GOOGLE_MAPS_KEY: String = "";
    
    var customMapViews = [String : CustomMapView]();

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
            
            self.bridge?.viewController?.view.addSubview(customMapView.view);

            customMapView.GMapView.delegate = customMapView;
            
            self.customMapViews[customMapView.id] = customMapView;
        }
    }
    
    override func lastResultForCallbackId(callbackId: String, result: JSObject) {
        let call = bridge?.savedCall(withID: callbackId);
        call?.resolve(result);
        bridge?.releaseCall(call!);
    }

}
