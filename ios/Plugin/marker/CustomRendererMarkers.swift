//
//  CustomRendererMarkers.swift
//  CapacitorCommunityCapacitorGooglemapsNative
//
//  Created by Admin on 24.01.2022.
//

import Foundation
import GoogleMapsUtils

class CustomRendererMarkers: GMUDefaultClusterRenderer {
    
    // -- constants --
    public static let MIN_COUNT_ELEMENTS_IN_CLUSTER : Int = 2;
    
var mapView:GMSMapView?
let kGMUAnimationDuration: Double = 0.5

override init(mapView: GMSMapView, clusterIconGenerator iconGenerator: GMUClusterIconGenerator) {
  
  super.init(mapView: mapView, clusterIconGenerator: iconGenerator)
}
    
    override func shouldRender(as cluster: GMUCluster, atZoom zoom: Float) -> Bool {
        return cluster.count >= CustomRendererMarkers.MIN_COUNT_ELEMENTS_IN_CLUSTER;
    }
    

// method dont working and dont override anything
// TODO: rewrite super class for changing animation
func markerWithPosition(position: CLLocationCoordinate2D, from: CLLocationCoordinate2D, userData: AnyObject, clusterIcon: UIImage, animated: Bool) -> GMSMarker {
  let initialPosition = animated ? from : position
  let marker = GMSMarker(position: initialPosition)
  marker.userData! = userData
  if clusterIcon.cgImage != nil {
      marker.icon = clusterIcon
  }
  else {
      marker.icon = self.getCustomTitleItem(userData: userData)
      
  }
  marker.map = mapView
  if animated
  {
      CATransaction.begin()
      CAAnimation.init().duration = kGMUAnimationDuration
      marker.layer.latitude = position.latitude
      marker.layer.longitude = position.longitude
      CATransaction.commit()
  }
  return marker
}

func getCustomTitleItem(userData: AnyObject) -> UIImage {
  let item = userData as! CustomMarker
    return MarkerCategory.markerCategories[item.markerCategoryId]?.getIcon ?? item.icon!;
//  return item.icon!
}
}
