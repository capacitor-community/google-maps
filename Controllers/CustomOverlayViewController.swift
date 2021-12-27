//
//  CustomOverlayViewController.swift
//  CapacitorCommunityCapacitorGooglemapsNative
//
//  Created by Admin on 27.12.2021.
//

import Foundation
import UIKit
import GoogleMaps
import Capacitor

class CustomOverlayViewController: UIViewController, GMSMapViewDelegate {
    
    open var instanceOfGoogleMapsPlugin : CapacitorGoogleMaps?
//        get {
//             return instanceOfGoogleMapsPlugin
//        }
//        set {
//            self.instanceOfGoogleMapsPlugin = newValue
//        }
    
    
    open var delegateTouchEventsToMapId : String?
//        get {
//            return delegateTouchEventsToMapId
//        }
//        set {
//            self.delegateTouchEventsToMapId = newValue
//        }
    
    public var lastEventChainId : String = "";
    
    open var devicePixelRatio : Float?
//        get {
//            return devicePixelRatio
//        }
//        set {
//            self.devicePixelRatio = newValue
//        }
    

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        print("touches began")
        
//        delegateTouchEventsToMapId = nil;
        
        // Initialize JSObjects for resolve().
        var result : JSObject = JSObject()
        var coordinatesOfPoint : JSObject = JSObject()
        
        // Generate a UUID, and assign it to lastId.
        // This way we can make sure we are always referencing the last chain of events.
        self.lastEventChainId = NSUUID().uuidString.lowercased()
        instanceOfGoogleMapsPlugin?.lastEventChainId = self.lastEventChainId
        // Then add it to result object, so the Webview can reference the correct events when needed.
        result.updateValue(self.lastEventChainId, forKey: "eventChainId")
        
        // Get the touched position.
        var x : Double = touches.first!.location(in: self.view).x
        var y : Double = touches.first!.location(in: self.view).y
        
        
        
        // Since pixels on a webpage are calculated differently, should convert them first.
        // Convert it to 'real' pixels in WebView by using devicePixelRatio
        if (devicePixelRatio != nil && devicePixelRatio! > 0) {
            coordinatesOfPoint.updateValue(Float(x) / devicePixelRatio!, forKey: "x")
            coordinatesOfPoint.updateValue(Float(y) / devicePixelRatio!, forKey: "y")
        }
        
        // Then add it to result object.
        result.updateValue(coordinatesOfPoint, forKey: "point");
        
        // Then notify the listener that we request to let the WebView determine
        // if the element touched is the same node as where some MapView is attached to.
        instanceOfGoogleMapsPlugin!.notifyListenerFromPlugin("didRequestElementFromPoint", result);
        
        self.delegateTouchEventsToMapId = instanceOfGoogleMapsPlugin?.delegateTouchEventsToMapId
        if (delegateTouchEventsToMapId != nil) {
            var customMapViewController : CustomMapViewController = (instanceOfGoogleMapsPlugin?.customMapViewControllers[delegateTouchEventsToMapId!])!;
            if(customMapViewController != nil) {
                // Apparently, all touch events should be delegated to a specific MapView
//                dispatchTouchEvent(event!, customMapViewController)
                customMapViewController.touchesBegan(touches, with: event)
            }
        }
    
    }
    
}
