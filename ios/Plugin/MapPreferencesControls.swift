import Capacitor
import GoogleMaps

class MapPreferencesControls {
    public static let COMPASS_BUTTON_KEY: String! = "isCompassButtonEnabled"
    public static let INDOOR_LEVEL_PICKER_KEY: String! = "isIndoorLevelPickerEnabled"
    public static let MAP_TOOLBAR_KEY: String! = "isMapToolbarEnabled" // (android only)
    public static let MY_LOCATION_BUTTON_KEY: String! = "isMyLocationButtonEnabled"
    public static let ZOOM_BUTTONS_KEY: String! = "isZoomButtonsEnabled" // (android only)

    var _isCompassButtonEnabled: Bool = true
    var isCompassButtonEnabled: Bool! {
        get {
            return _isCompassButtonEnabled
        }
        set (newVal) {
            _isCompassButtonEnabled = newVal ?? true
        }
    }

    var _isIndoorLevelPickerEnabled: Bool = false
    var isIndoorLevelPickerEnabled: Bool! {
        get {
            return _isIndoorLevelPickerEnabled
        }
        set (newVal) {
            _isIndoorLevelPickerEnabled = newVal ?? false
        }
    }

    var _isMyLocationButtonEnabled: Bool = true
    var isMyLocationButtonEnabled: Bool! {
        get {
            return _isMyLocationButtonEnabled
        }
        set (newVal) {
            _isMyLocationButtonEnabled = newVal ?? true
        }
    }

    func updateFromJSObject(object: JSObject) {
        self.isCompassButtonEnabled = object[MapPreferencesControls.COMPASS_BUTTON_KEY] as? Bool;
        self.isIndoorLevelPickerEnabled = object[MapPreferencesControls.INDOOR_LEVEL_PICKER_KEY] as? Bool;
        self.isMyLocationButtonEnabled = object[MapPreferencesControls.MY_LOCATION_BUTTON_KEY] as? Bool;
    }
    
    func getJSObject(_ mapView: GMSMapView) -> JSObject {
        return [
            MapPreferencesControls.COMPASS_BUTTON_KEY: mapView.settings.compassButton,
            MapPreferencesControls.INDOOR_LEVEL_PICKER_KEY: mapView.settings.indoorPicker,
            MapPreferencesControls.MAP_TOOLBAR_KEY: false, // (android only)
            MapPreferencesControls.MY_LOCATION_BUTTON_KEY: mapView.settings.myLocationButton,
            MapPreferencesControls.ZOOM_BUTTONS_KEY: false, // (android only)
        ]
    }
}
