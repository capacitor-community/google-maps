import Foundation
import Capacitor
import GoogleMaps
import UIKit




@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {
    
    // --constants--
    let TAG_NUMBER_FOR_MAP_UIVIEW : Int = 10;
    let TAG_NUMBER_FOR_TOP_OVERLAY_UIVIEW : Int = 20;
    let TAG_NUMBER_FOR_DEFAULT_WEBVIEW_SUBVIEW : Int = 1;
    
    
    var GOOGLE_MAPS_KEY: String = "";
    
    var customMapViewControllers = [String : CustomMapViewController]();
    
    var customMarkers = [String : CustomMarker]();
    
    var hasTopView : Bool = false;
    
    var arrayOfHTMLElements = [BoundingRect]();
    
    
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
            
            
            customMapViewController.view.tag = self.TAG_NUMBER_FOR_MAP_UIVIEW;
            
            // elements on the top of the mapView
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
                self.arrayOfHTMLElements.append(newBoundingRect);
            }
            
            
            self.bridge?.webView?.addSubview(customMapViewController.view)
//            self.webView?.insertSubview(customMapViewController.view, at: 0)
            
            customMapViewController.mapView.delegate = customMapViewController;
            
            self.customMapViewControllers[customMapViewController.id] = customMapViewController;
            
            
            // Bring the WebView in front of the MapView
            // This allows us to overlay the MapView in HTML/CSS
            // subview[0] - is map
            self.bridge?.webView?.sendSubviewToBack(customMapViewController.view)
            
            
            // Hide the background
            self.webView?.isOpaque = false;
            self.webView?.backgroundColor = .clear;
            self.webView?.scrollView.backgroundColor = .clear;
            self.webView?.scrollView.isOpaque = false;
           
            
 
            
            // class for view that will receive touches and transmit them
            class OverlayView: UIView {
                
                public var mainClass: CapacitorGoogleMaps?;
                
                override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {

                    // if (count-1) is the top view, then hmtl layout is second and here must be (count-2)
                    let numberOfHTMLElementsOfView: Int = (mainClass?.bridge?.webView?.subviews.count)! - 2;

                    // make html view default off
                    mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = false

                    // checking the hit in the elements
                    for boundingRect in mainClass?.arrayOfHTMLElements ?? [] {
                        // checking the hit in the element
                        if(Bool(point.x > boundingRect.x) &&
                           Bool(point.x < (boundingRect.x + boundingRect.width)) &&
                           Bool(point.y > boundingRect.y) &&
                           Bool(point.y < (boundingRect.y + boundingRect.height))) {
                            // touch point inside of html element
                            // then we make this subview is toucheable
                            mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
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
                            // if there is no mapView than we on html view
                        }

                    }
                    mainClass?.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
                    return false
                }
            }
            
            if(self.hasTopView == false) {
                let overlayView: OverlayView = OverlayView()
                overlayView.mainClass = self
                overlayView.isOpaque = true;
                overlayView.backgroundColor = UIColor.clear
                overlayView.tag = self.TAG_NUMBER_FOR_TOP_OVERLAY_UIVIEW;
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
            // find what mapview marker belongs
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                // get preferences of new marker
                let preferences = call.getObject("preferences", JSObject());
                
                let marker = CustomMarker();
                marker.updateFromJSObject(preferences: preferences);
                
                // add new marker to the map
                customMapViewController?.addMarker(marker);
                
                call.resolve(CustomMarker.getResultForMarker(marker));
            } else {
                call.reject("map not found");
            }
        }
    }
    
    @objc func addMarkers(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            // find what mapview marker belongs
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                let markers = call.getArray("markers", []);
                
                var markerArray = [CustomMarker]();
                for item in markers {
                    let markerObject = item as? JSObject ?? JSObject();
                    
                    let preferences = markerObject["preferences"] as? JSObject ?? JSObject();
                    
                    let marker = CustomMarker();
                    marker.updateFromJSObject(preferences: preferences);
                    markerArray.append(marker);

                }
                customMapViewController?.addMarkers(markerArray);
                
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
                
//                customMarker?.map = nil;
//                self.customMarkers[markerId] = nil;
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
    
    
    
    @objc func blockMapViews(_ call: CAPPluginCall) {
        
        DispatchQueue.main.async {
            
            // turn off every map
            for mapview in self.customMapViewControllers {
                mapview.value.view.isUserInteractionEnabled = false;
            }
            
            // and turn on html elements view
            // if (count-1) is the top view, then hmtl layout is second and here must be (count-2)
            let numberOfHTMLElementsOfView: Int = (self.bridge?.webView?.subviews.count)! - 2;
            self.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
            
            // and if we don't need maps then we dont need and topOverlayView
            // for transmiting touches
            if(self.hasTopView == true) {
                // first view is the top view
                let numberOfTopOverlayView: Int = (self.bridge?.webView?.subviews.count)! - 2;
                self.bridge?.webView?.subviews[numberOfTopOverlayView].isUserInteractionEnabled = false
            }
            
            call.resolve([
                "mapsBlocked": true
            ])
        }
    }
    
    @objc func unblockMapViews(_ call: CAPPluginCall) {
        
        DispatchQueue.main.async {
            
            // turn on every map
            for mapview in self.customMapViewControllers {
                mapview.value.view.isUserInteractionEnabled = true;
            }
            
            
            // and if we need maps then we need topOverlayView
            // for transmiting touches
            if(self.hasTopView == true) {
                // first view is the top view
                let numberOfTopOverlayView: Int = (self.bridge?.webView?.subviews.count)! - 2;
                self.bridge?.webView?.subviews[numberOfTopOverlayView].isUserInteractionEnabled = false
            }
            
            call.resolve([
                "mapsBlocked": false
            ])
        }
    }
    
    @objc func getArrayOfHTMLElements(_ call: CAPPluginCall) {
        
        var result = JSObject();
        var arrayOfJSObjects = [JSObject]();
        
        for rect in self.arrayOfHTMLElements {
            arrayOfJSObjects.append(rect.getJSObject())
        }
        
        
        // sending full array of BoundingRect
        result.updateValue(arrayOfJSObjects, forKey: "arrayOfHTMLElements")
        
        call.resolve(result)
    }
    
    
    
    @objc func setArrayOfHTMLElements(_ call: CAPPluginCall) {
        let response = call.getObject("response", JSObject());

        // optimization thing
        // check if size of array is changed
        var isKeepCapacityOfArray = false;
        isKeepCapacityOfArray = response["isKeepCapacityOfArray"] as? Bool ?? false;
        
        // clear previous html elements in array
        self.arrayOfHTMLElements.removeAll(keepingCapacity: isKeepCapacityOfArray);
        
        // get JS array from call as array of JSObjects
        let myArr = response["arrayOfHTMLElements"] as? [JSObject] ?? [];
        
        
        let br : BoundingRect = BoundingRect();
        
        
        for boundingRectJSObject in myArr{
            br.updateFromJSObject(boundingRectJSObject);
            self.arrayOfHTMLElements.append(br);
            print("Here will be smt when this is work")
            print(String(br.x))
        }
        
        
        call.resolve();
    }
    
    
    @objc func zoomInButtonClick(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]!;
            
            if(customMapViewController != nil) {
                customMapViewController.zoomIn();
                call.resolve();
            } else {
                call.reject("map not found");
            }
            
            
        }
    }
    
    @objc func zoomOutButtonClick(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]!;
            
            if(customMapViewController != nil) {
                customMapViewController.zoomOut();
                call.resolve();
            } else {
                call.reject("map not found");
            }
            
            
        }
    }
    
}
