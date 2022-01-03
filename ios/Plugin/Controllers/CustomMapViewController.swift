//
//  CustomMapViewController.swift
//  Capacitor
//
//  Created by Admin on 27.12.2021.
//

import Capacitor
import GoogleMaps
import GoogleMapsUtils

class CustomMapViewController: UIViewController, GMSMapViewDelegate {
    
    var customMapViewEvents: CustomMapViewEvents!;
    
    var id: String!;
    
    var GMapView: GMSMapView!
    
    
    
    var boundingRect = BoundingRect();
    var mapCameraPosition = MapCameraPosition();
    var mapPreferences = MapPreferences();
    
    
    
    var savedCallbackIdForCreate: String!;
    
    var savedCallbackIdForDidTapInfoWindow: String!;

    var savedCallbackIdForDidCloseInfoWindow: String!;

    var savedCallbackIdForDidTapMap: String!;

    var savedCallbackIdForDidLongPressMap: String!;
    
    var savedCallbackIdForDidTapMarker: String!;
    var preventDefaultForDidTapMarker: Bool = false;

    var savedCallbackIdForDidTapMyLocationButton: String!;
    var preventDefaultForDidTapMyLocationButton: Bool = false;

    var savedCallbackIdForDidTapMyLocationDot: String!;

    // This allows you to initialise your custom UIViewController without a nib or bundle.
    convenience init(customMapViewEvents: CustomMapViewEvents) {
        self.init(nibName:nil, bundle:nil)
        self.customMapViewEvents = customMapViewEvents
        self.id = NSUUID().uuidString.lowercased()
    }

    
    // This extends the superclass.
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    
    override func viewDidLoad() {
        super.viewDidLoad();
        
        let frame = CGRect(x: self.boundingRect.x, y: self.boundingRect.y, width: self.boundingRect.width, height: self.boundingRect.height);
        
        let camera = GMSCameraPosition.camera(withLatitude: self.mapCameraPosition.latitude, longitude: self.mapCameraPosition.longitude, zoom: self.mapCameraPosition.zoom, bearing: self.mapCameraPosition.bearing, viewingAngle: self.mapCameraPosition.tilt);
        
        self.GMapView = GMSMapView.map(withFrame: frame, camera: camera);
        
        self.view = GMapView;

        self.invalidateMap();

        self.customMapViewEvents.lastResultForCallbackId(callbackId: savedCallbackIdForCreate, result: [
            "googleMap": [
                "mapId": self.id
            ]
        ]);
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        print("CustomMapViewController getting touches and event")
//        var uir : UIResponder = UIResponder()
//        uir.touchesBegan(touches, with: event)
        
        self.GMapView.point(inside: (touches.first?.location(in: self.view))!, with: event)
//        self.GMapView.touchesBegan(touches, with: event)
//        super.touchesBegan(touches, with: event)
    }
    
    
    func invalidateMap() {
        if (self.GMapView == nil) {
            return;
        }

        // set gestures
        self.GMapView.settings.rotateGestures = self.mapPreferences.gestures.isRotateAllowed;
        self.GMapView.settings.scrollGestures = self.mapPreferences.gestures.isScrollAllowed;
        self.GMapView.settings.allowScrollGesturesDuringRotateOrZoom = self.mapPreferences.gestures.isScrollAllowedDuringRotateOrZoom;
        self.GMapView.settings.tiltGestures = self.mapPreferences.gestures.isTiltAllowed;
        self.GMapView.settings.zoomGestures = self.mapPreferences.gestures.isZoomAllowed;

        // set controls
        self.GMapView.settings.compassButton = self.mapPreferences.controls.isCompassButtonEnabled;
        self.GMapView.settings.indoorPicker = self.mapPreferences.controls.isIndoorLevelPickerEnabled;
        self.GMapView.settings.myLocationButton = self.mapPreferences.controls.isMyLocationButtonEnabled;

        // set appearance
        self.GMapView.mapType = self.mapPreferences.appearance.type;
        self.GMapView.mapStyle = self.mapPreferences.appearance.style;
        self.GMapView.isBuildingsEnabled = self.mapPreferences.appearance.isBuildingsShown;
        self.GMapView.isIndoorEnabled = self.mapPreferences.appearance.isIndoorShown;
        self.GMapView.isMyLocationEnabled = self.mapPreferences.appearance.isMyLocationDotShown;
        self.GMapView.isTrafficEnabled = self.mapPreferences.appearance.isTrafficShown;
    }
    
    
    public func setCallbackIdForEvent(callbackId: String!, eventName: String!,
                                      preventDefault: Bool!) {
        if (callbackId != nil && eventName != nil) {
            switch eventName {
            case Events.EVENT_DID_TAP_INFO_WINDOW:
                savedCallbackIdForDidTapInfoWindow = callbackId;
            case Events.EVENT_DID_CLOSE_INFO_WINDOW:
                savedCallbackIdForDidCloseInfoWindow = callbackId;
            case Events.EVENT_DID_TAP_MAP:
                savedCallbackIdForDidTapMap = callbackId;
            case Events.EVENT_DID_LONG_PRESS_MAP:
                savedCallbackIdForDidLongPressMap = callbackId;
            case Events.EVENT_DID_TAP_MARKER:
                savedCallbackIdForDidTapMarker = callbackId
                preventDefaultForDidTapMarker = preventDefault ?? false;
            case Events.EVENT_DID_TAP_MY_LOCATION_BUTTON:
                savedCallbackIdForDidTapMyLocationButton = callbackId;
                preventDefaultForDidTapMyLocationButton = preventDefault ?? false;
            case Events.EVENT_DID_TAP_MY_LOCATION_DOT:
                savedCallbackIdForDidTapMyLocationDot = callbackId
            default:
                return
            }
        
        }
        
    }
    
    
    // MARK: GMSMapViewDelegate
    
    internal func mapView(_ mapView: GMSMapView, didTapInfoWindowOf marker: GMSMarker) {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapInfoWindow != nil) {
            let result: PluginCallResultData = CustomMarker.getResultForMarker(marker);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapInfoWindow, result: result);
        }
    }

    internal func mapView(_ mapView: GMSMapView, didCloseInfoWindowOf marker: GMSMarker) {
        if (customMapViewEvents != nil && savedCallbackIdForDidCloseInfoWindow != nil) {
            let result: PluginCallResultData = CustomMarker.getResultForMarker(marker);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidCloseInfoWindow, result: result);
        }
    }
    
    internal func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMap != nil) {
            let result: PluginCallResultData = self.getResultForPosition(coordinate: coordinate);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMap, result: result);
        }
    }
    
    internal func mapView(_ mapView: GMSMapView, didLongPressAt coordinate: CLLocationCoordinate2D) {
        if (customMapViewEvents != nil && savedCallbackIdForDidLongPressMap != nil) {
            let result: PluginCallResultData = self.getResultForPosition(coordinate: coordinate);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidLongPressMap, result: result);
        }
    }

    internal func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMarker != nil) {
            let result: PluginCallResultData = CustomMarker.getResultForMarker(marker);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMarker, result: result);
        }
        return preventDefaultForDidTapMarker;
    }

    internal func mapView(_ mapView: GMSMapView, didTapMyLocation coordinate: CLLocationCoordinate2D) {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMyLocationDot != nil) {
            let result: PluginCallResultData = self.getResultForPosition(coordinate: coordinate);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMyLocationDot, result: result);
        }
    }

    internal func didTapMyLocationButton(for mapView: GMSMapView) -> Bool {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMyLocationButton != nil) {
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMyLocationButton, result: nil);
        }
        return preventDefaultForDidTapMyLocationButton;
    }

    private func getResultForPosition(coordinate: CLLocationCoordinate2D) -> PluginCallResultData {
        return [
            "position": [
                "latitude": coordinate.latitude,
                "longitude": coordinate.longitude
            ]
        ]
    }
    
    

}


