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
        super.init(path: path)
    }
    
    public func updateFromJSObject(polygonData: JSObject) {
        let holesUpdate = polygonData["holes"].map { holePoints in
            return CustomPolygon.pathFromJson(points: holePoints)
        }
        
        self.holes = holesUpdate
        
        self.strokeWidth = polygonData["strokeWidth"] as? Double ?? 1.0
        self.strokeColor = polygonData["strokeColor"] as? String ?? nil
        self.fillColor = polygonData["fillColor"] as? String ?? nil
        self.title = polygonData["title"] as? String ?? nil
        self.zIndex = Int32.init(preferences["zIndex"] as? Int ?? 0);
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
        
        return [
            "polygon": [
                "mapId": mapId,
                "id": tag["id"] ?? nil,
                "points": CustomPolygon.jsonFromPath(path: marker.path),
                "preferences": [
                    "title": marker.title,
                    "holes": marker.holes.map({ path in
                        CustomPolygon.jsonFromPath(path: path)
                    }),
                    "metadata": tag["metadata"] ?? JSObject()
                ]
            ]
        ];
    }
    
    private static func jsonFromPath(path: GMSPath) {
        let size = path.count()
        let result: [JSObject] = []()
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
            let coord = CLLocationCoordinate2D(latitude: point["latitude"], longitude: point["longitude"])
            path.add( coord)
        }
        
        return path as GMSPath
    }
    
}
