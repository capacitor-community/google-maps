import Foundation
import GoogleMaps
import Capacitor

class CustomPolygon : GMSPolygon {
    var id: String! = NSUUID().uuidString.lowercased();    
    
    public func updateFromJSObject(_ polygonData: JSObject) {
        let pathArray = polygonData["path"] as? [JSObject] ?? [JSObject]()
        let path = CustomPolygon.pathFromJson(pathArray)
        self.path = path

        let preferences = polygonData["preferences"] as? JSObject ?? JSObject()

        let holesArray = preferences["holes"] as? [[JSObject]] ?? [[JSObject]]()
        let holes = holesArray.map { holePathArray in
            return CustomPolygon.pathFromJson(holePathArray)
        }
        self.holes = holes
        
        self.strokeWidth = preferences["strokeWidth"] as? Double ?? 10.0
        
        if let strokeColor = preferences["strokeColor"] as? String {
            self.strokeColor = UIColor.capacitor.color(fromHex: strokeColor) ?? nil
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
            "polygonId": self.id!,
            "metadata": metadata
        ] as? JSObject ?? JSObject()
    }
    
    public static func getResultForPolygon(_ polygon: GMSPolygon, mapId: String) -> PluginCallResultData {
        let tag: JSObject = polygon.userData as! JSObject
        let holes: [GMSPath] = polygon.holes ?? [GMSPath]()

        return [
            "polygon": [
                "mapId": mapId,
                "polygonId": tag["polygonId"] ?? "",
                "path": CustomPolygon.jsonFromPath(polygon.path),
                "preferences": [
                    "title": polygon.title ?? "",
                    "holes": holes.map { path in
                        return CustomPolygon.jsonFromPath(path)
                    },
                    "strokeWidth": polygon.strokeWidth,
                    "strokeColor": polygon.strokeColor ?? "",
                    "fillColor": polygon.fillColor ?? "",
                    "zIndex": polygon.zIndex,
                    "isGeodesic": polygon.geodesic,
                    "isClickable": polygon.isTappable,
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
            result.append(CustomPolygon.jsonFromCoord(coord))
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
