import Capacitor
import GoogleMaps

class CustomMarker: GMSMarker {
    private var id: String! = NSUUID().uuidString.lowercased()
    var markerId: String {
        get {
            return self.id;
        }
    }
    
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
}
