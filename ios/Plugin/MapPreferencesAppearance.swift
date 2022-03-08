import Capacitor
import GoogleMaps

class MapPreferencesAppearance {
    public static let TYPE_KEY: String! = "type"
    public static let STYLE_KEY: String! = "style"
    public static let BUILDINGS_SHOWN_KEY: String! = "isBuildingsShown"
    public static let INDOOR_SHOWN_KEY: String! = "isIndoorShown"
    public static let MY_LOCATION_DOT_SHOWN_KEY: String! = "isMyLocationDotShown"
    public static let TRAFFIC_SHOWN_KEY: String! = "isTrafficShown"

    var type: GMSMapViewType = GMSMapViewType.normal;

    var style: GMSMapStyle? = nil;

    var _isBuildingsShown: Bool = true
    var isBuildingsShown: Bool! {
        get {
            return _isBuildingsShown
        }
        set (newVal) {
            _isBuildingsShown = newVal ?? true
        }
    }

    var _isIndoorShown: Bool = true
    var isIndoorShown: Bool! {
        get {
            return _isIndoorShown
        }
        set (newVal) {
            _isIndoorShown = newVal ?? true
        }
    }

    var _isMyLocationDotShown: Bool = false
    var isMyLocationDotShown: Bool! {
        get {
            return _isMyLocationDotShown
        }
        set (newVal) {
            _isMyLocationDotShown = newVal ?? false
        }
    }

    var _isTrafficShown: Bool = false
    var isTrafficShown: Bool! {
        get {
            return _isTrafficShown
        }
        set (newVal) {
            _isTrafficShown = newVal ?? false
        }
    }

    func updateFromJSObject(object: JSObject) {
        if (object[MapPreferencesAppearance.TYPE_KEY] != nil) {
            let type = object[MapPreferencesAppearance.TYPE_KEY] as? Int;
            if (type != nil) {
                if (type == 0) {
                    self.type = GMSMapViewType.none;
                } else if (type == 1) {
                    self.type = GMSMapViewType.normal;
                } else if (type == 2) {
                    self.type = GMSMapViewType.satellite;
                } else if (type == 3) {
                    self.type = GMSMapViewType.terrain;
                } else if (type == 4) {
                    self.type = GMSMapViewType.hybrid;
                } else {
                    self.type = GMSMapViewType.normal;
                }
            }
        }
        
        if (object[MapPreferencesAppearance.STYLE_KEY] != nil) {
            let style = object[MapPreferencesAppearance.STYLE_KEY] as? String;
            if (style == nil) {
                self.style = nil;
            } else {
                do {
                    try self.style = GMSMapStyle(jsonString: style ?? "{}");
                } catch {
                    print("error parsing style json string");
                }
            }
        }
        
        self.isBuildingsShown = object[MapPreferencesAppearance.BUILDINGS_SHOWN_KEY] as? Bool;
        self.isIndoorShown = object[MapPreferencesAppearance.INDOOR_SHOWN_KEY] as? Bool;
        self.isMyLocationDotShown = object[MapPreferencesAppearance.MY_LOCATION_DOT_SHOWN_KEY] as? Bool;
        self.isTrafficShown = object[MapPreferencesAppearance.TRAFFIC_SHOWN_KEY] as? Bool;
    }
    
    func getJSObject(_ mapView: GMSMapView) -> JSObject {
        var type = 1;
        if (mapView.mapType == GMSMapViewType.none) {
            type = 0;
        } else if (mapView.mapType == GMSMapViewType.normal) {
            type = 1;
        } else if (mapView.mapType == GMSMapViewType.satellite) {
            type = 2;
        } else if (mapView.mapType == GMSMapViewType.terrain) {
            type = 3;
        } else if (mapView.mapType == GMSMapViewType.hybrid) {
            type = 4;
        }
        
        return [
            MapPreferencesAppearance.TYPE_KEY: type,
            MapPreferencesAppearance.BUILDINGS_SHOWN_KEY: mapView.isBuildingsEnabled,
            MapPreferencesAppearance.INDOOR_SHOWN_KEY: mapView.isIndoorEnabled,
            MapPreferencesAppearance.MY_LOCATION_DOT_SHOWN_KEY: mapView.isMyLocationEnabled,
            MapPreferencesAppearance.TRAFFIC_SHOWN_KEY: mapView.isTrafficEnabled
        ]
    }
}
