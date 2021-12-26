import Foundation
import Capacitor
import GoogleMaps
import UIKit



@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {
    

    var GOOGLE_MAPS_KEY: String = "";

    var customMapViews = [String : CustomMapView]();

    var customMarkers = [String : CustomMarker]();
    
    var devicePixelRatio : Float = 0;
    
    public var lastEventChainId : String = "";
    public var previousEvents: [UIEvent] = []
    public var delegateTouchEventsToMapId : String?;
    
    var overlayView : CustomTopView? = nil;
    
    
    public func notifyListenerFromPlugin(_ eventName: String, _ data: JSObject) {
       notifyListeners(eventName, data: data)
    }
    
    /**
     This method should be called after we requested the WebView through notifyListeners("didRequestElementFromPoint").
     It should tell us if the exact point that was being touched, is from an element in which a MapView exists.
     Otherwise it is a 'normal' HTML element, and we should thus not delegate touch events.
     */
    @objc func elementFromPointResult(_ call: CAPPluginCall) {
        var eventChainId : String? = call.getString("eventChainId");
        if(eventChainId != nil && eventChainId == self.lastEventChainId) {
            var isSameNode : Bool? = call.getBool("isSameNode", false);
            if (isSameNode != nil && isSameNode == true) {
                // The WebView apparently has decide the touched point belongs to a certains MapView
                // Now we should find out which one exactly.
                var mapId : String? = call.getString("mapId");
                if (mapId != nil) {
                    delegateTouchEventsToMapId = mapId!;
                }
            }
        }
        call.resolve();
    }


    @objc func initialize(_ call: CAPPluginCall) {

        self.GOOGLE_MAPS_KEY = call.getString("key", "")

        if self.GOOGLE_MAPS_KEY.isEmpty {
            call.reject("GOOGLE MAPS API key missing!")
            return
        }

        GMSServices.provideAPIKey(self.GOOGLE_MAPS_KEY)
        self.devicePixelRatio = call.getFloat("devicePixelRatio", 0)
        self.overlayView = CustomTopView(self, self.webView!.frame)
        self.overlayView?.devicePixelRatio = self.devicePixelRatio
        call.resolve([
            "initialized": true
        ])
    }
    


    @objc func createMap(_ call: CAPPluginCall) {

        DispatchQueue.main.async {
            let customMapView : CustomMapView = CustomMapView(customMapViewEvents: self);

            self.bridge?.saveCall(call)
            customMapView.savedCallbackIdForCreate = call.callbackId;
            
            let boundingRect = call.getObject("boundingRect", JSObject());
            customMapView.boundingRect.updateFromJSObject(boundingRect);
            
            let mapCameraPosition = call.getObject("cameraPosition", JSObject());
            customMapView.mapCameraPosition.updateFromJSObject(mapCameraPosition);

            let preferences = call.getObject("preferences", JSObject());
            customMapView.mapPreferences.updateFromJSObject(preferences);
    
            
            self.bridge?.webView?.addSubview(customMapView.view)

            customMapView.GMapView.delegate = customMapView;
            self.customMapViews[customMapView.id] = customMapView;
            

            // Bring the WebView in front of the MapView
            // This allows us to overlay the MapView in HTML/CSS
            self.bridge?.webView?.sendSubviewToBack(customMapView.view)
        
            
            // Hide the background
            self.bridge?.webView?.isOpaque = false;
            self.bridge?.webView?.backgroundColor = UIColor.clear
            
            // Adding third UIView on top of the WebView for getting touches
            // and translate them to MapView
            self.overlayView?.isUserInteractionEnabled = true;
            self.overlayView?.isOpaque = false
            self.overlayView?.backgroundColor = UIColor.clear
            self.overlayView?.valOfMapView = customMapView
            // here will be adding new MapViewId to overlayView
            // TODO

            self.bridge?.webView?.addSubview(self.overlayView!)
            self.bridge?.webView?.bringSubviewToFront(self.overlayView!)
        }
    }

    @objc func updateMap(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId")!;

        DispatchQueue.main.async {
            let customMapView = self.customMapViews[mapId];

            if (customMapView != nil) {
                let preferences = call.getObject("preferences", JSObject());
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
    
    @objc func addMarkers(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");

        DispatchQueue.main.async {
            let customMapView = self.customMapViews[mapId];

            if (customMapView != nil) {
                let markers = call.getArray("markers", []);
                
                for item in markers {
                    let markerObject = item as? JSObject ?? JSObject();

                    let preferences = markerObject["preferences"] as? JSObject ?? JSObject();

                    let marker = CustomMarker();
                    marker.updateFromJSObject(preferences: preferences);

                    marker.map = customMapView?.GMapView;

                    self.customMarkers[marker.id] = marker;
                }

                call.resolve();
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

    @objc func didTapInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_INFO_WINDOW);
    }

    @objc func didCloseInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_CLOSE_INFO_WINDOW);
    }

    @objc func didTapMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MAP);
    }

    @objc func didLongPressMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_LONG_PRESS_MAP);
    }

    @objc func didTapMarker(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MARKER);
    }

    @objc func didTapMyLocationButton(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MY_LOCATION_BUTTON);
    }

    @objc func didTapMyLocationDot(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MY_LOCATION_DOT);
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

    override func resultForCallbackId(callbackId: String, result: PluginCallResultData?) {
        let call = bridge?.savedCall(withID: callbackId);
        if (result != nil) {
            call?.resolve(result!);
        } else {
            call?.resolve();
        }
    }

}
