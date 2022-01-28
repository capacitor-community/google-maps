//
//  MarkerCategory.swift
//  CapacitorCommunityCapacitorGooglemapsNative
//
//  Created by Admin on 27.01.2022.
//

import Foundation

class MarkerCategory {
    
    private var id : Int?;
    private var title : String?;
    private var icon : UIImage?;
    
    static var markerCategories = [Int : MarkerCategory]();
    
    init(_ id: Int, _ title: String, _ icon: UIImage?) {
        self.id = id;
        self.title = title;
        self.icon = icon;
        
        MarkerCategory.markerCategories[self.id ?? 0] = self
    }
    
    var getIcon : UIImage? {
        get {
            return icon;
        }
    }
}
