import Foundation
import GoogleMaps
import Capacitor

class CustomPolyline : GMSPolyline {
    var id: String! = NSUUID().uuidString.lowercased();    
    
    public func updateFromJSObject(_ polylineData: JSObject) {
        let pathArray = polylineData["path"] as? [JSObject] ?? [JSObject]()
        let path = CustomPolyline.pathFromJson(pathArray)
        self.path = path

        let preferences = polylineData["preferences"] as? JSObject ?? JSObject()
        
        self.width = preferences["width"] as? Double ?? 10.0
        
        if let color = preferences["color"] as? String {
            self.color = UIColor.capacitor.color(fromHex: color) ?? nil
        }
        
        if let fillColor = preferences["fillColor"] as? String {
            self.fillColor = UIColor.capacitor.color(fromHex: fillColor) ?? nil
        }
        
        self.title = preferences["title"] as? String ?? ""
        self.zIndex = Int32.init(preferences["zIndex"] as? Int ?? 1)
        self.geodesic = preferences["isGeodesic"] as? Bool ?? false
        self.isTappable = preferences["isClickable"] as? Bool ?? false
        
        let metadata: JSObject = preferences["metadata"] as? JSObject ?? JSObject()
        self.userData = [
            "polylineId": self.id!,
            "metadata": metadata
        ] as? JSObject ?? JSObject()
    }
    
    public static func getResultForPolyline(_ polyline: GMSPolyline, mapId: String) -> PluginCallResultData {
        let tag: JSObject = polyline.userData as! JSObject

        return [
            "polyline": [
                "mapId": mapId,
                "polylineId": tag["polylineId"] ?? "",
                "path": CustomPolyline.jsonFromPath(polyline.path),
                "preferences": [
                    "title": polyline.title ?? "",
                    "width": polyline.width,
                    "color": polyline.color ?? "",
                    "zIndex": polyline.zIndex,
                    "isGeodesic": polyline.geodesic,
                    "isClickable": polyline.isTappable,
                    "metadata": tag["metadata"] ?? JSObject()
                ]
            ]
        ];
    }
    

    private static func jsonFromPath(_ path: GMSPath?) -> [JSObject] {
        guard let path = path else {
            return [JSObject]()
        }
        let size = path.count()
        var result: [JSObject] = []
        for i in stride(from: 0, to: size, by: 1) {
            let coord = path.coordinate(at: i)
            result.append(CustomPolyline.jsonFromCoord(coord))
        }
        return result
    }
    
    private static func jsonFromCoord(_ coord: CLLocationCoordinate2D) -> JSObject {
        return ["latitude" : coord.latitude, "longitude": coord.longitude]
    }
    
    private static func pathFromJson(_ latLngArray: [JSObject]) -> GMSPath {
        let path = GMSMutablePath()
        latLngArray.forEach { point in
            if let lat = point["latitude"] as? Double, let long = point["longitude"] as? Double {
                let coord = CLLocationCoordinate2D(latitude: lat, longitude: long)
                path.add(coord)
            }
        }
        
        return path as GMSPath
    }
}
