import Capacitor
import GoogleMaps

class CustomMarker: GMSMarker {
    private var markerId: String! = NSUUID().uuidString.lowercased()
    public var id: String {
        get {
            return self.markerId;
        }
    }
    
    var markerCategoryId : Int = 0;
    
    public func updateFromJSObject(preferences: JSObject) {
        let markerId = preferences["id"] as? String ?? nil;
        if(markerId != nil) {
            self.markerId = markerId as! String;
        }
        
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
        
        var userData = JSObject()
        userData["id"] = self.id
        userData["iconId"] = self.markerCategoryId;
        userData["metadata"] = metadata
        
        self.userData = userData
    }
    
    public static func getResultForMarker(_ marker: GMSMarker, _ mapId: String) -> PluginCallResultData {
        // check marker is marker from map or from clusterManager
        let markerObj : GMSMarker;
        if marker.userData is GMSMarker {
            markerObj = marker.userData as! GMSMarker;
        } else {
            markerObj = marker as! GMSMarker
        }
        
        let tag: JSObject = markerObj.userData  as! JSObject;
        
        return [
            "position": [
                "mapId": mapId,
                "latitude": marker.position.latitude,
                "longitude": marker.position.longitude,
            ] as! JSObject,
            "marker": [
                "metadata": tag["metadata"] ?? JSObject(),
                "id": tag["id"] ?? "",
                "iconId": tag["iconId"] ?? -1,
            ]
        ];
        
//        return [
//            "marker": [
//                "id": tag["id"] ?? "",
//                "title": marker.title ?? "",
//                "snippet": marker.snippet ?? "",
//                "opacity": marker.opacity,
//                "isFlat": marker.isFlat,
//                "isDraggable": marker.isDraggable,
//                "position": [
//                    "latitude": marker.position.latitude,
//                    "longitude": marker.position.longitude
//                ],
//                "metadata": tag["metadata"] ?? JSObject()
//            ]
//        ];
    }
}
