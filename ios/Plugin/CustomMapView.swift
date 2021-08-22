import UIKit
import Capacitor
import GoogleMaps

class CustomMapView: UIViewController, GMSMapViewDelegate {
    var customMapViewEvents: CustomMapViewEvents!;
    
    var id: String!;
    
    var GMapView: GMSMapView!
    
    var savedCallbackIdForCreate: String!;
    
    var savedCallbackIdForDidTapMap: String!;
    
    var savedCallbackIdForDidTapMarker: String!;
    var preventDefaultForDidTapMarker: Bool = false;
    
    static var EVENT_DID_TAP_MAP: String = "didTapMap";
    static var EVENT_DID_TAP_MARKER: String = "didTapMarker";
    
    var mapViewBounds: [String : Double]!
    var cameraPosition: [String: Double]!
    var mapPreferences = MapPreferences();
    
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
        super.viewDidLoad()
        let camera = GMSCameraPosition.camera(withLatitude: cameraPosition["latitude"] ?? 0, longitude: cameraPosition["longitude"] ?? 0, zoom: Float(cameraPosition["zoom"] ?? Double(12.0)))
        let frame = CGRect(x: mapViewBounds["x"] ?? 0, y: mapViewBounds["y"]!, width: mapViewBounds["width"] ?? 0, height: mapViewBounds["height"] ?? 0)
        self.GMapView = GMSMapView.map(withFrame: frame, camera: camera)
        self.view = GMapView
        
        self.invalidateMap();
        
        self.customMapViewEvents.lastResultForCallbackId(callbackId: savedCallbackIdForCreate, result: [
            "googleMap": [
                "mapId": self.id
            ]
        ]);
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
    
    public func setCallbackIdForEvent(callbackId: String!, eventName: String!, preventDefault: Bool!) {
        if (callbackId != nil && eventName != nil) {
            if (eventName == CustomMapView.EVENT_DID_TAP_MAP) {
                savedCallbackIdForDidTapMap = callbackId;
            } else if (eventName == CustomMapView.EVENT_DID_TAP_MARKER) {
                savedCallbackIdForDidTapMarker = callbackId;
                preventDefaultForDidTapMarker = preventDefault ?? false;
            }
        }
    }
    
    internal func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMap != nil) {
            let result: PluginCallResultData = self.getResultForPosition(coordinate: coordinate);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMap, result: result);
        }
    }
    
    internal func mapView(_ mapView: GMSMapView, didTap marker: GMSMarker) -> Bool {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMap != nil) {
            let result: PluginCallResultData = CustomMarker.getResultForMarker(marker);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMarker, result: result);
        }
        return preventDefaultForDidTapMarker;
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
