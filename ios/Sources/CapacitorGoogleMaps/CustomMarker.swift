import Capacitor
import GoogleMaps

class CustomMarker: GMSMarker {
    var id: String! = NSUUID().uuidString.lowercased();
    
    public func updateFromJSObject(_ markerData: JSObject) {
        let position = markerData["position"] as? JSObject ?? JSObject();
        
        let latitude = position["latitude"] as? Double ?? 0.0;
        let longitude = position["longitude"] as? Double ?? 0.0;
        
        let preferences = markerData["preferences"] as? JSObject ?? JSObject();
        
        self.position = CLLocationCoordinate2D(latitude: latitude, longitude: longitude);
        
        self.title = preferences["title"] as? String ?? nil;
        
        self.snippet = preferences["snippet"] as? String ?? nil;
        
        self.opacity = preferences["opacity"] as? Float ?? 1.0;
        
        self.isFlat = preferences["isFlat"] as? Bool ?? false;
        
        self.isDraggable = preferences["isDraggable"] as? Bool ?? false;
        
        self.zIndex = Int32.init(preferences["zIndex"] as? Int ?? 0);

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
    
    public static func getResultForMarker(_ marker: GMSMarker, mapId: String) -> PluginCallResultData {
        let tag: JSObject = marker.userData as! JSObject;
        
        return [
            "marker": [
                "mapId": mapId,
                "markerId": tag["markerId"] ?? nil,
                "position": [
                    "latitude": marker.position.latitude,
                    "longitude": marker.position.longitude
                ],
                "preferences": [
                    "title": marker.title,
                    "snippet": marker.snippet,
                    "opacity": marker.opacity,
                    "isFlat": marker.isFlat,
                    "isDraggable": marker.isDraggable,
                    "zIndex": marker.zIndex,
                    "anchor": [
                        "x": marker.groundAnchor.x,
                        "y": marker.groundAnchor.y
                    ],
                    "metadata": tag["metadata"] ?? JSObject()
                ]
            ]
        ];
    }
}
