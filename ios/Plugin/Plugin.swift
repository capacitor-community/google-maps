import Foundation
import Capacitor
import GoogleMaps
import UIKit




@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {
    

    var GOOGLE_MAPS_KEY: String = "";

    var customMapViewControllers = [String : CustomMapViewController]();

    var customMarkers = [String : CustomMarker]();
    
    public var devicePixelRatio : Float = 0;
    
    public var lastEventChainId : String = "";

    public var previousEvents: [UIEvent] = []
    public var delegateTouchEventsToMapId : String?;
        
    /**
     This method should be called after we requested the WebView through notifyListeners("didRequestElementFromPoint").
     It should tell us if the exact point that was being touched, is from an element in which a MapView exists.
     Otherwise it is a 'normal' HTML element, and we should thus not delegate touch events.
     */
    @objc func elementFromPointResult(_ call: CAPPluginCall) {
            var isSameNode : Bool? = call.getBool("isSameNode", false);
            if (isSameNode != nil && isSameNode == true) {
                // The WebView apparently has decide the touched point belongs to a certains MapView
                // Now we should find out which one exactly.
                var mapId : String? = call.getString("mapId");
                if (mapId != nil) {
                    self.delegateTouchEventsToMapId = mapId!;
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
                newBoundingRect.width = Double(twoDArray![i][0]);
                newBoundingRect.height = Double(twoDArray![i][1]);
                newBoundingRect.x = Double(twoDArray![i][2]);
                newBoundingRect.y = Double(twoDArray![i][3]);
            }
            
            //     width: Math.round(boundingRectangleOfChild.width),
            //     height: Math.round(boundingRectangleOfChild.height),
            //     x: Math.round(boundingRectangleOfChild.x),
            //     y: Math.round(boundingRectangleOfChild.y),
            
            
            
            
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

            // subview[1] - is html elements: buttons, textfields, etc
            // it will always last element of webview.subviews, because we always
            // send MapView to the back
            var numberOfHTMLElementsOfView: Int = (self.bridge?.webView?.subviews.count)! - 1;
            self.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = false
            
            // class for view that will receive touches and transmit them
            class OverlayView: UIView {
                
                public var mainClass: CapacitorGoogleMaps?;
                
                override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
                        mainClass?.delegateTouchEventsToMapId = nil

                        // Initialize JSObjects for resolve().
                        var result : JSObject = JSObject()
                        var coordinatesOfPoint : JSObject = JSObject()

                        // Generate a UUID, and assign it to lastId.
                        // This way we can make sure we are always referencing the last chain of events.
                        mainClass!.lastEventChainId = NSUUID().uuidString.lowercased()

                        // Then add it to result object, so the Webview can reference the correct events when needed.
                        result.updateValue(mainClass!.lastEventChainId, forKey: "eventChainId")

                        // Get the touched position.
                        var x : Double = point.x
                        var y : Double = point.y

                        // Since pixels on a webpage are calculated differently, should convert them first.
                        // Convert it to 'real' pixels in WebView by using devicePixelRatio
//                        if (mainClass!.devicePixelRatio != nil && mainClass!.devicePixelRatio > 0) {
//                            coordinatesOfPoint.updateValue(Float(x) / mainClass!.devicePixelRatio, forKey: "x")
//                            coordinatesOfPoint.updateValue(Float(y) / mainClass!.devicePixelRatio, forKey: "y")
//                        }
                    
                    coordinatesOfPoint.updateValue(x, forKey: "x")
                    coordinatesOfPoint.updateValue(y, forKey: "y")

                        // Then add it to result object.
                        result.updateValue(coordinatesOfPoint, forKey: "point");
                        
                        // Then notify the listener that we request to let the WebView determine
                        // if the element touched is the same node as where some MapView is attached to.
                    
                    mainClass?.notifyListeners("didRequestElementFromPoint", data: result)
                    
                    
                    // if number -1 is the top view, then hmtl layaout is second and here must be -2
                    var numberOfHTMLElementsOfView: Int = (mainClass?.bridge?.webView?.subviews.count)! - 2;

                    mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = false
                    
                    if(mainClass?.delegateTouchEventsToMapId == nil)  {
                        mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
                    } else {
                        mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = false
                        mainClass?.customMapViewControllers[String(mainClass!.delegateTouchEventsToMapId!)]!.view.isUserInteractionEnabled = true;
                    }
                    
                    
                    return false
                    
                    
//                    for var HTMLElement in mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].subviews ?? [] {
//
//
//                    }
                        
                        
                        
//                    var numOfTouchedElem: Int = 0;
//
//                    mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].subviews
////                        .dropFirst()
//                        .forEach({ HTMLElement in
//                            numOfTouchedElem+=1;
//                        //skiping first element that take size of full webview
//
//                        // checking the hit in the element
//                        if(point.x > HTMLElement.coordinateSpace.bounds.minX &&
//                           point.x < HTMLElement.coordinateSpace.bounds.maxX &&
//                           point.y > HTMLElement.coordinateSpace.bounds.minY &&
//                           point.y < HTMLElement.coordinateSpace.bounds.maxY) {
//                            // touch point inside of html element
//                            // then we make this subview is toucheable
//                            mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
//                            print()
//                            print("Number of touched View: " + String(numOfTouchedElem))
//                            print()
//                        } else {
//                            // do nothing
//                        }
//                    })
                    
//                    for var HTMLElement: UIView in mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].subviews {
//
//
//                        // checking the hit in the element
//                        if(point.x > HTMLElement.coordinateSpace.bounds.minX &&
//                           point.x < HTMLElement.coordinateSpace.bounds.maxX &&
//                           point.y > HTMLElement.coordinateSpace.bounds.minY &&
//                           point.y < HTMLElement.coordinateSpace.bounds.maxY) {
//                            // touch point inside of html element
//                            // then we make this subview is toucheable
//                            mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
//                        } else {
//                            mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = false
//                        }
//                    }
                    
                    
                    
                }
            }
            
            var overlayView: OverlayView = OverlayView()
            overlayView.mainClass = self
            overlayView.isOpaque = true;
            overlayView.backgroundColor = UIColor.clear
            self.bridge?.webView?.addSubview(overlayView)
            
            
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
