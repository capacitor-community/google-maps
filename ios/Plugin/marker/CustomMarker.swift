import Capacitor
import GoogleMaps

class CustomMarker: GMSMarker {
    private var id: String! = NSUUID().uuidString.lowercased()
    public var markerId: String {
        get {
            return self.id;
        }
    }
    
    var markerCategoryId : Int = 0;
    
    public func updateFromJSObject(preferences: JSObject) {
        let position = preferences["position"] as? JSObject ?? JSObject();
        let latitude = position["latitude"] as? Double ?? 0.0;
        let longitude = position["longitude"] as? Double ?? 0.0;
        self.position = CLLocationCoordinate2D(latitude: latitude, longitude: longitude);
        
        self.title = preferences["title"] as? String ?? nil;
        self.snippet = preferences["snippet"] as? String ?? nil;
        self.opacity = preferences["opacity"] as? Float ?? 1.0;
        self.isFlat = preferences["isFlat"] as? Bool ?? false;
        self.isDraggable = preferences["isDraggable"] as? Bool ?? false;
        self.markerCategoryId = preferences["category"] as? Int ?? 0;
        
        
        let metadata: JSObject = preferences["metadata"] as? JSObject ?? JSObject();
        
        self.userData = [
            "markerId": self.markerId,
            "metadata": metadata
        ] as? JSObject ?? JSObject();
    }
    
    public static func getResultForMarker(_ marker: GMSMarker) -> PluginCallResultData {
        let markerObj = marker.userData as! GMSMarker;
        let tag: JSObject = markerObj.userData  as! JSObject;
        
        return [
            "marker": [
                "markerId": tag["markerId"] ?? nil,
                "title": marker.title,
                "snippet": marker.snippet,
                "opacity": marker.opacity,
                "isFlat": marker.isFlat,
                "isDraggable": marker.isDraggable,
                "position": [
                    "latitude": marker.position.latitude,
                    "longitude": marker.position.longitude
                ],
                "metadata": tag["metadata"] ?? JSObject()
            ]
        ];
    }
    
    
    public static func getJSONForClusterItem(_ item: GMSMarker) -> JSObject {
        let markerObj = item as! GMSMarker;
        let tag: JSObject = markerObj.userData  as! JSObject;
        
        var result = JSObject();

        result = [
            "markerId" : tag["markerId"] ?? "",
            "title": item.title ?? "",
            "snippet": item.snippet ?? "",
            "opacity": item.opacity,
            "isFlat": item.isFlat,
            "isDraggable": item.isDraggable,
            "position": [
                "latitude": item.position.latitude,
                "longitude": item.position.longitude
            ],
            "metadata": tag["metadata"] ?? JSObject()
        ]

        return result;
    }
}
