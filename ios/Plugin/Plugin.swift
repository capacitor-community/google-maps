import Foundation
import Capacitor
import GoogleMaps

@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CAPPlugin {

    var GOOGLE_MAPS_KEY: String = "";

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

}
