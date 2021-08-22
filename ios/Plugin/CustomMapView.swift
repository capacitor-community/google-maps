import UIKit
import Capacitor
import GoogleMaps

class CustomMapView: UIViewController, GMSMapViewDelegate {
    var customMapViewEvents: CustomMapViewEvents!;
    
    var id: String!;
    
    var GMapView: GMSMapView!
    
    var savedCallbackIdForCreate: String!;
    
    var savedCallbackIdForDidTapMap: String!;
    
    static var EVENT_DID_TAP_MAP: String = "didTapMap";
    
    var mapViewBounds: [String : Double]!
    var cameraPosition: [String: Double]!
    
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
        
        self.customMapViewEvents.lastResultForCallbackId(callbackId: savedCallbackIdForCreate, result: [
            "googleMap": [
                "mapId": self.id
            ]
        ])
    }
    
    public func setCallbackIdForEvent(callbackId: String!, eventName: String!, preventDefault: Bool!) {
        if (callbackId != nil && eventName != nil) {
            if (eventName == CustomMapView.EVENT_DID_TAP_MAP) {
                savedCallbackIdForDidTapMap = callbackId;
            }
        }
    }
    
    internal func mapView(_ mapView: GMSMapView, didTapAt coordinate: CLLocationCoordinate2D) {
        if (customMapViewEvents != nil && savedCallbackIdForDidTapMap != nil) {
            let result: JSObject = self.getResultForPosition(coordinate: coordinate);
            customMapViewEvents.resultForCallbackId(callbackId: savedCallbackIdForDidTapMap, result: result);
        }
    }
    
    private func getResultForPosition(coordinate: CLLocationCoordinate2D) -> JSObject {
        return [
            "position": [
                "latitude": coordinate.latitude,
                "longitude": coordinate.longitude
            ]
        ]
    }

}
