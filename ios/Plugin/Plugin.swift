import Foundation
import Capacitor
import GoogleMaps
import UIKit




@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {
    

    var GOOGLE_MAPS_KEY: String = "";

    var customMapViewControllers = [String : CustomMapViewController]();

    var customMarkers = [String : CustomMarker]();
    
    var hasTopView : Bool = false;
    
    var arrayHTMLElements = [BoundingRect]();


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
            let customMapViewController : CustomMapViewController = CustomMapViewController(customMapViewEvents: self);

            self.bridge?.saveCall(call)
            customMapViewController.savedCallbackIdForCreate = call.callbackId;
            
            let boundingRect = call.getObject("boundingRect", JSObject());
            customMapViewController.boundingRect.updateFromJSObject(boundingRect);
            
            let mapCameraPosition = call.getObject("cameraPosition", JSObject());
            customMapViewController.mapCameraPosition.updateFromJSObject(mapCameraPosition);

            let preferences = call.getObject("preferences", JSObject());
            customMapViewController.mapPreferences.updateFromJSObject(preferences);
            
            
            // getting JSOobject
            let elementsOnTop = call.getObject("elementsOnTop", JSObject());
            // getting two dimensional array of child elements
            let twoDArray =  elementsOnTop["innerEles"] as? [[Int]];
            for i in 0..<twoDArray!.count {
                var newBoundingRect = BoundingRect();
                //     width: Math.round(boundingRectangleOfChild.width),
                //     height: Math.round(boundingRectangleOfChild.height),
                //     x: Math.round(boundingRectangleOfChild.x),
                //     y: Math.round(boundingRectangleOfChild.y),
                newBoundingRect.width = Double(twoDArray![i][0]);
                newBoundingRect.height = Double(twoDArray![i][1]);
                newBoundingRect.x = Double(twoDArray![i][2]);
                newBoundingRect.y = Double(twoDArray![i][3]);
                self.arrayHTMLElements.append(newBoundingRect);
            }
            

            self.bridge?.webView?.addSubview(customMapViewController.view)

            customMapViewController.GMapView.delegate = customMapViewController;
            
            self.customMapViewControllers[customMapViewController.id] = customMapViewController;
            

            // Bring the WebView in front of the MapView
            // This allows us to overlay the MapView in HTML/CSS
            // subview[0] - is map
            self.bridge?.webView?.sendSubviewToBack(customMapViewController.view)
        
            
            // Hide the background
            self.bridge?.webView?.isOpaque = false;
            self.bridge?.webView?.backgroundColor = UIColor.clear

            
            // class for view that will receive touches and transmit them
            class OverlayView: UIView {
                
                public var mainClass: CapacitorGoogleMaps?;
                
                override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
                    
                    // if (count-1) is the top view, then hmtl layout is second and here must be (count-2)
                    var countOfHTMLElementsOfView: Int = (mainClass?.bridge?.webView?.subviews.count)! - 2;
                    
                    // make html view default off
                    mainClass?.bridge?.webView?.subviews[countOfHTMLElementsOfView].isUserInteractionEnabled = false
                    
                    // checking the hit in the elements
                    for boundingRect in mainClass?.arrayHTMLElements ?? [] {
                        // checking the hit in the element
                        if(point.x > boundingRect.x &&
                           point.x < (boundingRect.x + boundingRect.width) &&
                           point.y > boundingRect.y &&
                           point.y < (boundingRect.y + boundingRect.height)) {
                            // touch point inside of html element
                            // then we make this subview is toucheable
                            mainClass?.bridge?.webView?.subviews[countOfHTMLElementsOfView].isUserInteractionEnabled = true
                            return false
                        }
                    }
                    
                    // if we here, than html view off
                    let arrayOfMaps = [CustomMapViewController]((mainClass?.customMapViewControllers.values)!)
                    
                    for mapview in arrayOfMaps {
                        // checking if map exists in this point
                        if(point.x > mapview.boundingRect.x &&
                           point.x < (mapview.boundingRect.x + mapview.boundingRect.width) &&
                           point.y > mapview.boundingRect.y &&
                           point.y < (mapview.boundingRect.y + mapview.boundingRect.height)) {
                        // if mapview exist in this point than doing nothing
                        // just go further
                            return false
                        } else {
                            // if there is no mapView than we on html
//                            mainClass?.bridge?.webView?.subviews[countOfHTMLElementsOfView].isUserInteractionEnabled = true
//                            return false
                        }
                                                
                    }
                    mainClass?.bridge?.webView?.subviews[countOfHTMLElementsOfView].isUserInteractionEnabled = true
                    return false
                }
            }
            
            if(self.hasTopView == false) {
                let overlayView: OverlayView = OverlayView()
                overlayView.mainClass = self
                overlayView.isOpaque = true;
                overlayView.backgroundColor = UIColor.clear
                self.bridge?.webView?.addSubview(overlayView)
                
                self.hasTopView = true
            }
            
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
