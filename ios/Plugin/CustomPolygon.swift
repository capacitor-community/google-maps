//
//  CustomPolygon.swift
//  Plugin
//
//  Created by Michiel Boertjes on 05/01/2023.
//  Copyright © 2023 Max Lynch. All rights reserved.
//

import Foundation
import GoogleMaps
import Capacitor

class CustomPolygon : GMSPolygon {
    var id: String! = NSUUID().uuidString.lowercased();

    
    init(points: [JSObject]) {
        let path = CustomPolygon.pathFromJson(points: points)
        super.init()
        self.path = path
    }
    
    
    
    public func updateFromJSObject(polygonData: JSObject) {
        let holesArray = polygonData["holes"] as? [[JSObject]] ?? [[JSObject]]()
        let holesUpdate = holesArray.map { holePoints in
            return CustomPolygon.pathFromJson(points: holePoints)
        }
        
        self.holes = holesUpdate
        
        self.strokeWidth = polygonData["strokeWidth"] as? Double ?? 1.0
        if let strokeColor = polygonData["strokeColor"] as? String {
            self.strokeColor = UIColor.capacitor.color(fromHex: strokeColor) ?? nil
        }
        
        if let fillColor = polygonData["fillColor"] as? String {
            self.fillColor = UIColor.capacitor.color(fromHex: fillColor) ?? nil
        }
        
        self.title = polygonData["title"] as? String ?? nil
        self.zIndex = Int32.init(polygonData["zIndex"] as? Int ?? 0);
        self.geodesic = polygonData["isGeodesic"] as? Bool ?? false
        self.isTappable = polygonData["isClickable"] as? Bool ?? false
        let metadata: JSObject = polygonData["metadata"] as? JSObject ?? JSObject();
        self.userData = [
            "id": self.id!,
            "metadata": metadata
        ] as? JSObject ?? JSObject();
    }
    
    public static func getResultForPolygon(_ polygon: GMSPolygon, mapId: String) -> PluginCallResultData {
        let tag: JSObject = polygon.userData as! JSObject;
        let holes: [GMSPath] = polygon.holes ?? [GMSPath]()
        return [
            "polygon": [
                "mapId": mapId,
                "id": tag["id"] ?? nil,
                "points": CustomPolygon.jsonFromPath(path: polygon.path ?? nil),
                "preferences": [
                    "title": polygon.title,
                    "holes": holes.map { path in
                        return CustomPolygon.jsonFromPath(path: path)
                    },
                    "metadata": tag["metadata"] ?? JSObject()
                ]
            ]
        ];
    }
    

    private static func jsonFromPath(path: GMSPath?) -> [JSObject] {
        guard let path else {
            return [JSObject]()
        }
        let size = path.count()
        var result: [JSObject] = []
        for i in stride(from: 0, to: size, by: 1) {
            let coord = path.coordinate(at: i)
            result.append(CustomPolygon.jsonFromCoord(coord: coord))
        }
        return result
    }
    
    private static func jsonFromCoord(coord: CLLocationCoordinate2D) -> JSObject {
        return ["latitude" : coord.latitude, "longitude": coord.longitude]
    }
    
    private static func pathFromJson(points: [JSObject]) -> GMSPath {
        let path = GMSMutablePath()
        points.forEach { point in
            if let lat = point["latitude"] as? Double, let long = point["longitude"] as? Double {
                let coord = CLLocationCoordinate2D(latitude: lat, longitude: long)
                path.add( coord)
            }
        }
        
        return path as GMSPath
    }
    
    
    
}
