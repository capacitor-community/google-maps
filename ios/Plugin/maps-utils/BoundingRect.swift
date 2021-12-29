import Capacitor

class BoundingRect {
    public static let WIDTH_KEY: String! = "width";
    public static let HEIGHT_KEY: String! = "height";
    public static let X_KEY: String! = "x";
    public static let Y_KEY: String! = "y";
    
    private static let WIDTH_DEFAULT: Double! = 500.0;
    private static let HEIGHT_DEFAULT: Double! = 500.0;
    private static let X_DEFAULT: Double! = 0.0;
    private static let Y_DEFAULT: Double! = 0.0;
    
    public var width: Double!;
    public var height: Double!;
    public var x: Double!;
    public var y: Double!;
    
    public init () {
        self.width = BoundingRect.WIDTH_DEFAULT;
        self.height = BoundingRect.HEIGHT_DEFAULT;
        self.x = BoundingRect.X_DEFAULT;
        self.y = BoundingRect.Y_DEFAULT;
    }

    func updateFromJSObject(_ object: JSObject) {
        self.width = object[BoundingRect.WIDTH_KEY] as? Double ?? BoundingRect.WIDTH_DEFAULT;
        self.height = object[BoundingRect.HEIGHT_KEY] as? Double ?? BoundingRect.HEIGHT_DEFAULT;
        self.x = object[BoundingRect.X_KEY] as? Double ?? BoundingRect.X_DEFAULT;
        self.y = object[BoundingRect.Y_KEY] as? Double ?? BoundingRect.Y_DEFAULT;
    }
}
