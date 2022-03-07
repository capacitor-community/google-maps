import Capacitor
import GoogleMaps

class CustomMarker: GMSMarker {
    var id: String! = NSUUID().uuidString.lowercased();
    
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

        let anchor = preferences["anchor"] as? JSObject ?? JSObject();
        let anchorX = anchor["x"] as? Double ?? 0.5;
        let anchorY = anchor["y"] as? Double ?? 1.0;
        self.groundAnchor = CGPoint.init(x: anchorX, y: anchorY);
        
        let metadata: JSObject = preferences["metadata"] as? JSObject ?? JSObject();
        self.userData = [
            "markerId": self.id!,
            "metadata": metadata
        ] as? JSObject ?? JSObject();
    }
    
    public static func getResultForMarker(_ marker: GMSMarker) -> PluginCallResultData {
        let tag: JSObject = marker.userData as! JSObject;
        
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
                "anchor": [
                    "x": marker.groundAnchor.x,
                    "y": marker.groundAnchor.y
                ],
                "metadata": tag["metadata"] ?? JSObject()
            ]
        ];
    }
}
