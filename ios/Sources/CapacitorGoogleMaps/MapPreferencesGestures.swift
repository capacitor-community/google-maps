import Capacitor
import GoogleMaps

class MapPreferencesGestures {
    public static let ROTATE_ALLOWED_KEY: String! = "isRotateAllowed"
    public static let SCROLL_ALLOWED_KEY: String! = "isScrollAllowed"
    public static let SCROLL_ALLOWED_DURING_ROTATE_OR_ZOOM_KEY: String! = "isScrollAllowedDuringRotateOrZoom"
    public static let TILT_ALLOWED_KEY: String! = "isTiltAllowed"
    public static let ZOOM_ALLOWED_KEY: String! = "isZoomAllowed"
    
    var _isRotateAllowed: Bool = true
    var isRotateAllowed: Bool! {
        get {
            return _isRotateAllowed
        }
        set (newVal) {
            _isRotateAllowed = newVal ?? true
        }
    }
    
    var _isScrollAllowed: Bool = true
    var isScrollAllowed: Bool! {
        get {
            return _isScrollAllowed
        }
        set (newVal) {
            _isScrollAllowed = newVal ?? true
        }
    }
    
    var _isScrollAllowedDuringRotateOrZoom: Bool = true
    var isScrollAllowedDuringRotateOrZoom: Bool! {
        get {
            return _isScrollAllowedDuringRotateOrZoom
        }
        set (newVal) {
            _isScrollAllowedDuringRotateOrZoom = newVal ?? true
        }
    }
    
    var _isTiltAllowed: Bool = true
    var isTiltAllowed: Bool! {
        get {
            return _isTiltAllowed
        }
        set (newVal) {
            _isTiltAllowed = newVal ?? true
        }
    }
    
    var _isZoomAllowed: Bool = true
    var isZoomAllowed: Bool! {
        get {
            return _isZoomAllowed
        }
        set (newVal) {
            _isZoomAllowed = newVal ?? true
        }
    }
    
    func updateFromJSObject(object: JSObject) {
        self.isRotateAllowed = object[MapPreferencesGestures.ROTATE_ALLOWED_KEY] as? Bool;
        self.isScrollAllowed = object[MapPreferencesGestures.SCROLL_ALLOWED_KEY] as? Bool;
        self.isScrollAllowedDuringRotateOrZoom = object[MapPreferencesGestures.SCROLL_ALLOWED_DURING_ROTATE_OR_ZOOM_KEY] as? Bool;
        self.isScrollAllowed = object[MapPreferencesGestures.TILT_ALLOWED_KEY] as? Bool;
        self.isScrollAllowed = object[MapPreferencesGestures.ZOOM_ALLOWED_KEY] as? Bool;
    }
    
    func getJSObject(_ mapView: GMSMapView) -> JSObject {
        return [
            MapPreferencesGestures.ROTATE_ALLOWED_KEY: mapView.settings.rotateGestures,
            MapPreferencesGestures.SCROLL_ALLOWED_KEY: mapView.settings.scrollGestures,
            MapPreferencesGestures.SCROLL_ALLOWED_DURING_ROTATE_OR_ZOOM_KEY:                     mapView.settings.allowScrollGesturesDuringRotateOrZoom,
            MapPreferencesGestures.TILT_ALLOWED_KEY: mapView.settings.tiltGestures,
            MapPreferencesGestures.ZOOM_ALLOWED_KEY: mapView.settings.zoomGestures
        ]
    }
}
