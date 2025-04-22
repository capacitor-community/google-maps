import Capacitor
import GoogleMaps

class MapCameraPosition {
    public static let TARGET_KEY: String! = "target";
    public static let LATITUDE_KEY: String! = "latitude";
    public static let LONGITUDE_KEY: String! = "longitude";
    public static let BEARING_KEY: String! = "bearing";
    public static let TILT_KEY: String! = "tilt";
    public static let ZOOM_KEY: String! = "zoom";

    private static let TARGET_DEFAULT: JSObject! = JSObject();
    private static let LATITUDE_DEFAULT: Double! = 0.0;
    private static let LONGITUDE_DEFAULT: Double! = 0.0;
    private static let BEARING_DEFAULT: Double! = 0.0;
    private static let TILT_DEFAULT: Double! = 0.0;
    private static let ZOOM_DEFAULT: Float! = 12.0;

    public var latitude: Double!;
    public var longitude: Double!;
    public var bearing: Double!;
    public var tilt: Double!;
    public var zoom: Float!;

    public init () {
        self.latitude = MapCameraPosition.LATITUDE_DEFAULT;
        self.longitude = MapCameraPosition.LONGITUDE_DEFAULT;
        self.bearing = MapCameraPosition.BEARING_DEFAULT;
        self.tilt = MapCameraPosition.TILT_DEFAULT;
        self.zoom = MapCameraPosition.ZOOM_DEFAULT;
    }
    
    func getCameraPosition() -> GMSCameraPosition {
        return GMSCameraPosition.camera(
            withLatitude: self.latitude,
            longitude: self.longitude,
            zoom: self.zoom,
            bearing: self.bearing,
            viewingAngle: self.tilt
        )
    }

    func updateFromJSObject(_ object: JSObject, baseCameraPosition: GMSCameraPosition?) {
        let target: JSObject = object[MapCameraPosition.TARGET_KEY] as? JSObject ?? MapCameraPosition.TARGET_DEFAULT;
        
        var latitude = baseCameraPosition?.target.latitude ?? MapCameraPosition.LATITUDE_DEFAULT
        if (target[MapCameraPosition.LATITUDE_KEY] != nil) {
            // TODO: validate latitude
            latitude = target[MapCameraPosition.LATITUDE_KEY] as? Double ?? latitude
        }
        self.latitude = latitude;
        
        var longitude = baseCameraPosition?.target.longitude ?? MapCameraPosition.LONGITUDE_DEFAULT
        if (target[MapCameraPosition.LONGITUDE_KEY] != nil) {
            // TODO: validate longitude
            longitude = target[MapCameraPosition.LONGITUDE_KEY] as? Double ?? longitude
        }
        self.longitude = longitude;
        
        var bearing = baseCameraPosition?.bearing ?? MapCameraPosition.BEARING_DEFAULT
        if (object[MapCameraPosition.BEARING_KEY] != nil) {
            // TODO: validate bearing
            bearing = object[MapCameraPosition.BEARING_KEY] as? Double ?? bearing
        }
        self.bearing = bearing;
        
        var tilt = baseCameraPosition?.viewingAngle ?? MapCameraPosition.TILT_DEFAULT
        if (object[MapCameraPosition.TILT_KEY] != nil) {
            // TODO: validate tilt
            tilt = object[MapCameraPosition.TILT_KEY] as? Double ?? tilt
        }
        self.tilt = tilt;
        
        var zoom = baseCameraPosition?.zoom ?? MapCameraPosition.ZOOM_DEFAULT
        if (object[MapCameraPosition.ZOOM_KEY] != nil) {
            // TODO: validate zoom
            zoom = object[MapCameraPosition.ZOOM_KEY] as? Float ?? zoom
        }
        self.zoom = zoom;
    }
    
    func getJSObject(_ cameraPosition: GMSCameraPosition) -> JSObject {
        return [
            MapCameraPosition.TARGET_KEY: [
                MapCameraPosition.LATITUDE_KEY: cameraPosition.target.latitude,
                MapCameraPosition.LONGITUDE_KEY: cameraPosition.target.longitude
            ],
            MapCameraPosition.BEARING_KEY: cameraPosition.bearing,
            MapCameraPosition.TILT_KEY: cameraPosition.viewingAngle,
            MapCameraPosition.ZOOM_KEY: cameraPosition.zoom
        ]
    }
}
