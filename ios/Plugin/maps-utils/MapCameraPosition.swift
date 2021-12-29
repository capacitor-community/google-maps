import Capacitor

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

    func updateFromJSObject(_ object: JSObject) {
        let target: JSObject = object[MapCameraPosition.TARGET_KEY] as? JSObject ?? MapCameraPosition.TARGET_DEFAULT;
        self.latitude = target[MapCameraPosition.LATITUDE_KEY] as? Double ?? MapCameraPosition.LATITUDE_DEFAULT;
        self.longitude = target[MapCameraPosition.LONGITUDE_KEY] as? Double ?? MapCameraPosition.LONGITUDE_DEFAULT;
        self.bearing = object[MapCameraPosition.BEARING_KEY] as? Double ?? MapCameraPosition.BEARING_DEFAULT;
        self.tilt = object[MapCameraPosition.TILT_KEY] as? Double ?? MapCameraPosition.TILT_DEFAULT;
        self.zoom = object[MapCameraPosition.ZOOM_KEY] as? Float ?? MapCameraPosition.ZOOM_DEFAULT;
    }
}
