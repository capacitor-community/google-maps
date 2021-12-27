import Foundation
import Capacitor
import GoogleMaps
import UIKit



@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {
    

    var GOOGLE_MAPS_KEY: String = "";

    var customMapViewControllers = [String : CustomMapViewController]();

    var customMarkers = [String : CustomMarker]();
    
    var devicePixelRatio : Float = 0;
    
    open var lastEventChainId : String = "";
//        get {
//            return lastEventChainId
//        }
//        set {
//            lastEventChainId = newValue
//        }
//    };
    public var previousEvents: [UIEvent] = []
    public var delegateTouchEventsToMapId : String?;
    
    var overlayViewController: CustomOverlayViewController?
    
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
                    self.delegateTouchEventsToMapId = mapId!;
                    self.overlayViewController!.delegateTouchEventsToMapId = self.delegateTouchEventsToMapId
                }
            }
        }
        print("eventChainId: " + eventChainId!)
        print("isSameNode: " + String(call.getBool("isSameNode", false)))
        print("mapId: " + (self.delegateTouchEventsToMapId ?? "nothing"))
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
        call.resolve([
            "initialized": true
        ])
    }
    


    @objc func createMap(_ call: CAPPluginCall) {

        DispatchQueue.main.async {
            let customMapViewController : CustomMapViewController = CustomMapViewController(customMapViewEvents: self);

            self.bridge?.saveCall(call)
            customMapViewController.savedCallbackIdForCreate = call.callbackId;
            
            let boundingRect = call.getObject("boundingRect", JSObject());
            customMapViewController.boundingRect.updateFromJSObject(boundingRect);
            
            let mapCameraPosition = call.getObject("cameraPosition", JSObject());
            customMapViewController.mapCameraPosition.updateFromJSObject(mapCameraPosition);

            let preferences = call.getObject("preferences", JSObject());
            customMapViewController.mapPreferences.updateFromJSObject(preferences);
    
            
            self.bridge?.webView?.addSubview(customMapViewController.view)

            customMapViewController.GMapView.delegate = customMapViewController;
            
            self.customMapViewControllers[customMapViewController.id] = customMapViewController;
            

            // Bring the WebView in front of the MapView
            // This allows us to overlay the MapView in HTML/CSS
            self.bridge?.webView?.sendSubviewToBack(customMapViewController.view)
        
            
            // Hide the background
            self.bridge?.webView?.isOpaque = false;
            self.bridge?.webView?.backgroundColor = UIColor.clear
            
            // Adding third UIView on top of the WebView for getting touches
            // and translate them to MapView
            
            self.overlayViewController = CustomOverlayViewController();
            self.overlayViewController!.instanceOfGoogleMapsPlugin = self
            self.overlayViewController!.devicePixelRatio = self.devicePixelRatio
//
//            self.bridge?.viewController?.addChild(customMapViewController)
//            self.bridge?.viewController?.addChild(self.overlayViewController!)
            self.bridge?.webView?.addSubview(self.overlayViewController!.view)
            self.bridge?.webView?.bringSubviewToFront(self.overlayViewController!.view)
            
            self.overlayViewController!.view.isOpaque = false
            self.overlayViewController!.view.backgroundColor = UIColor.clear
            self.overlayViewController!.view.isUserInteractionEnabled = true
            
//            customMapViewController.GMapView.delegate = self.overlayViewController;
            
//            self.overlayView?.isUserInteractionEnabled = true;
//            self.overlayView?.isOpaque = false
//            self.overlayView?.backgroundColor = UIColor.clear
//            self.overlayView?.valOfMapView = customMapViewController
//            // here will be adding new MapViewId to overlayView
//            // TODO
//
//            self.bridge?.webView?.addSubview(self.overlayView!)
//            self.bridge?.webView?.bringSubviewToFront(self.overlayView!)
        }
    }

    @objc func updateMap(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId")!;

        DispatchQueue.main.async {
            let customMapViewController = self.customMapViewControllers[mapId];

            if (customMapViewController != nil) {
                let preferences = call.getObject("preferences", JSObject());
                customMapViewController?.mapPreferences.updateFromJSObject(preferences);

                customMapViewController?.invalidateMap();
            } else {
                call.reject("map not found");
            }
        }

    }

    @objc func addMarker(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");

        DispatchQueue.main.async {
            let customMapViewController = self.customMapViewControllers[mapId];

            if (customMapViewController != nil) {
                let preferences = call.getObject("preferences", JSObject());

                let marker = CustomMarker();
                marker.updateFromJSObject(preferences: preferences);

                marker.map = customMapViewController?.GMapView;

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
            let customMapViewController = self.customMapViewControllers[mapId];

            if (customMapViewController != nil) {
                let markers = call.getArray("markers", []);
                
                for item in markers {
                    let markerObject = item as? JSObject ?? JSObject();

                    let preferences = markerObject["preferences"] as? JSObject ?? JSObject();

                    let marker = CustomMarker();
                    marker.updateFromJSObject(preferences: preferences);

                    marker.map = customMapViewController?.GMapView;

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
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_INFO_WINDOW);
    }

    @objc func didCloseInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_CLOSE_INFO_WINDOW);
    }

    @objc func didTapMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MAP);
    }

    @objc func didLongPressMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_LONG_PRESS_MAP);
    }

    @objc func didTapMarker(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MARKER);
    }

    @objc func didTapMyLocationButton(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MY_LOCATION_BUTTON);
    }

    @objc func didTapMyLocationDot(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MY_LOCATION_DOT);
    }

    func setCallbackIdForEvent(call: CAPPluginCall, eventName: String) {
        call.keepAlive = true;
        let callbackId = call.callbackId;
        guard let mapId = call.getString("mapId") else { return };

        let customMapViewController: CustomMapViewController = customMapViewControllers[mapId]!;

        let preventDefault: Bool = call.getBool("preventDefault", false);
        customMapViewController.setCallbackIdForEvent(callbackId: callbackId, eventName: eventName, preventDefault: preventDefault);
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
