////
////  OwnUITapGestureRecognizer.swift
////  CapacitorCommunityCapacitorGooglemapsNative
////
////  Created by Admin on 22.12.2021.
////
//
//import Capacitor
//import UIKit
//
//public class CustomTopView : UIView {
//
//    var instanceOfGoogleMapsPlugin : CapacitorGoogleMaps;
//
//    var mapView : CustomMapView?    // Stored property
//
//    var valOfMapView : CustomMapView? { // Computed property
//        set {
//            self.mapView = newValue
//        }
//        get {
//            return self.mapView
//        }
//    }
//
//    public var devicePixelRatio : Float = 0;
//
//
//    init(_ instanceOfGoogleMapsPlugin : CapacitorGoogleMaps, _ frame : CGRect) {
//        self.instanceOfGoogleMapsPlugin = instanceOfGoogleMapsPlugin;
//        super.init(frame: frame);
//
//    }
//
//    required init?(coder: NSCoder) {
//        fatalError("init(coder:) has not been implemented")
//    }
//
//
//
////    public override func hitTest(_ point: C	GPoint, with event: UIEvent?) -> UIView? {
//////        mapView?.view.point(inside: point, with: event)
////
////
////        return mapView!.view
////    }
//
//    public override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {
//        print("Passing all touches to the next view (if any), in the view stack.")
//
//        if(event?.type == UIEvent.EventType.touches) {
//            // Throw away all previous state when starting a new touch gesture.
//            // The framework may have dropped the up or cancel event for the previous gesture
//            // due  to an app switch, ANR, or some other state change.
//            //////////////////////////////////////////////////////////
////            instanceOfGoogleMapsPlugin.delegateTouchEventsToMapId = nil
//            instanceOfGoogleMapsPlugin.previousEvents.removeAll();
//
//            // Initialize JSObjects for resolve().
//            var result : JSObject = JSObject()
//            var coordinatesOfPoint : JSObject = JSObject()
//
//            // Generate a UUID, and assign it to lastId.
//            // This way we can make sure we are always referencing the last chain of events.
//            instanceOfGoogleMapsPlugin.lastEventChainId = NSUUID().uuidString.lowercased()
//            // Then add it to result object, so the Webview can reference the correct events when needed.
//            result.updateValue(instanceOfGoogleMapsPlugin.lastEventChainId, forKey: "eventChainId")
//
//            // Get the touched position.
//            var x : Int = Int(point.x);
//            var y : Int = Int(point.y);
//
//            // Since pixels on a webpage are calculated differently, should convert them first.
//            // Convert it to 'real' pixels in WebView by using devicePixelRatio
//            if (devicePixelRatio != nil && devicePixelRatio > 0) {
//                coordinatesOfPoint.updateValue(Float(x) / devicePixelRatio, forKey: "x")
//                coordinatesOfPoint.updateValue(Float(y) / devicePixelRatio, forKey: "y")
//
//                // Then add it to result object.
//                result.updateValue(coordinatesOfPoint, forKey: "point");
//            }
//
//            // Then notify the listener that we request to let the WebView dermine
//            // if the element touched is the same node as where some MapView is attached to.
//            instanceOfGoogleMapsPlugin.notifyListenerFromPlugin("didRequestElementFromPoint", result);
//
//        }
//
//        if(instanceOfGoogleMapsPlugin.delegateTouchEventsToMapId != nil) {
//            var customMapView : CustomMapView? = instanceOfGoogleMapsPlugin.customMapViews[instanceOfGoogleMapsPlugin.delegateTouchEventsToMapId!];
//            if(customMapView != nil) {
//                // Apparently, all touch events should be delegated to a specific MapView.
//
//                // If previous events exist, we should execute those first
//                if (!instanceOfGoogleMapsPlugin.previousEvents.isEmpty) {
//                    for e in instanceOfGoogleMapsPlugin.previousEvents {
//                        if (e != nil) {
//                            // Delegate this previous event to the MapView.
//                            dispatchTouchEvent(e, customMapView!, touchPoint: point);
//                        }
//                    }
//                    // Since we delegated all previous events, we can now forget about them.
//                    instanceOfGoogleMapsPlugin.previousEvents.removeAll();
//                }
//
//                // Finally delegate the current event to the MapView
////                var clonedEvent : UIEvent = event?.copy() as! UIEvent
////                dispatchTouchEvent(clonedEvent, customMapView!, touchPoint: point);
//                dispatchTouchEvent(event!, customMapView!, touchPoint: point);
//            }
//        } else {
//                // If delegateTouchEventsToMapId is not set, but it could still be set later!
//                // So we should save all past events.
//                // That way we can still execute them later on.
//                instanceOfGoogleMapsPlugin.previousEvents.append(event!)
//            }
//
//        return false;
//    }
//
//    /**
//        dispatching event on selected MapView
//     */
//    private func dispatchTouchEvent(_ event: UIEvent, _ customMapView: CustomMapView, touchPoint : CGPoint) {
////        offsetViewBounds : CGRect = CGRect();
////        // returns the visible bounds
////        customMapView.mapView.Drawing
//
//
//        var relativeTop : Int = Int(customMapView.boundingRect.y)
//        var relativeLeft : Int = Int(customMapView.boundingRect.x)
//
//        var newCGPoint : CGPoint = CGPoint()
//        newCGPoint.x = touchPoint.x - CGFloat(relativeLeft)
//        newCGPoint.y = touchPoint.y - CGFloat(relativeTop)
//
//        customMapView.view.window?.sendEvent(event)
////        customMapView.view.point(inside: newCGPoint, with: event)
//    }
//
////    public override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
////        self.startPoi
////
////
////        mapView?.GMapView.touchesBegan(touches, with: event)
////    }
////
////    public override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
////        mapView?.GMapView.touchesEnded(touches, with: event)
////    }
//
//
//}
