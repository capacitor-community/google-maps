import Foundation
import Capacitor
import GoogleMaps
import UIKit


@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {
    
    // --constants--
    let TAG_NUMBER_FOR_MAP_UIVIEW : Int = 10;
    let TAG_NUMBER_FOR_TOP_OVERLAY_UIVIEW : Int = 20;
    let TAG_NUMBER_FOR_DEFAULT_WEBVIEW_SUBVIEW_WITH_HTML_ELEMENTS : Int = 1;
    let MARKER_CATEGORY_DIRECTORY : String = "marker-categories.bundle";
    let SIZE_OF_MARKERS = CGSize(width: 64.0, height: 64.0)
    
    
    var GOOGLE_MAPS_KEY: String = "";
    
    var customMapViewControllers = [String : CustomMapViewController]();
    
    var hasTopView : Bool = false;
    
    var arrayOfHTMLElements = [BoundingRect]();
    
    
    // class for view that will receive touches and transmit them
    class OverlayView: UIView {
        
        public var mainClass: CapacitorGoogleMaps?;
        
        override func point(inside point: CGPoint, with event: UIEvent?) -> Bool {

            // make html view default off
            mainClass?.bridge?.webView?.viewWithTag(mainClass!.TAG_NUMBER_FOR_DEFAULT_WEBVIEW_SUBVIEW_WITH_HTML_ELEMENTS)?.isUserInteractionEnabled = false;

            // checking the hit in the elements
            for boundingRect in mainClass?.arrayOfHTMLElements ?? [] {
                // checking the hit in the element
                if(Bool(point.x > boundingRect.x) &&
                   Bool(point.x < (boundingRect.x + boundingRect.width)) &&
                   Bool(point.y > boundingRect.y) &&
                   Bool(point.y < (boundingRect.y + boundingRect.height))) {
                    // touch point inside of html element
                    // then we make this subview is toucheable
                    mainClass?.bridge?.webView?.viewWithTag(mainClass!.TAG_NUMBER_FOR_DEFAULT_WEBVIEW_SUBVIEW_WITH_HTML_ELEMENTS)?.isUserInteractionEnabled = true
                    return false
                }
            }

            // if we here, than html view off
            let arrayOfMaps = [CustomMapViewController]((mainClass?.customMapViewControllers.values)!)

            for mapview in arrayOfMaps {
                // checking if map exists in this point
                if(point.x > mapview.boundingRect.x &&
                   point.x < (mapview.boundingRect.x + mapview.boundingRect.width) &&
                   point.y > mapview.boundingRect.y &&
                   point.y < (mapview.boundingRect.y + mapview.boundingRect.height)) {
                    // if mapview exist in this point than doing nothing
                    // just go further
                    return false
                } else {
                    // if there is no mapView than we on html view
                }

            }
            mainClass?.bridge?.webView?.viewWithTag(mainClass!.TAG_NUMBER_FOR_DEFAULT_WEBVIEW_SUBVIEW_WITH_HTML_ELEMENTS)?.isUserInteractionEnabled = true
            return false
        }
    }
    
    
    @objc func initialize(_ call: CAPPluginCall) {
        
        initMarkerCategories();
        
        self.GOOGLE_MAPS_KEY = call.getString("key", "")
        
        if self.GOOGLE_MAPS_KEY.isEmpty {
            call.reject("GOOGLE MAPS API key missing!")
            return
        }
        
        GMSServices.provideAPIKey(self.GOOGLE_MAPS_KEY)
        
        call.resolve([
            "initialized": true
        ])
    }
    
    
    
    @objc func createMap(_ call: CAPPluginCall) {
        
        DispatchQueue.main.sync{
            let customMapViewController : CustomMapViewController = CustomMapViewController(customMapViewEvents: self);
            
            self.bridge?.saveCall(call)
            customMapViewController.savedCallbackIdForCreate = call.callbackId;
            
            let boundingRect = call.getObject("boundingRect", JSObject());
            customMapViewController.boundingRect.updateFromJSObject(boundingRect);
            
            let mapCameraPosition = call.getObject("cameraPosition", JSObject());
            customMapViewController.mapCameraPosition.updateFromJSObject(mapCameraPosition);
            
            let preferences = call.getObject("preferences", JSObject());
            customMapViewController.mapPreferences.updateFromJSObject(preferences);
            
            
            self.bridge?.webView?.subviews[0].tag = TAG_NUMBER_FOR_DEFAULT_WEBVIEW_SUBVIEW_WITH_HTML_ELEMENTS;
            customMapViewController.view.tag = self.TAG_NUMBER_FOR_MAP_UIVIEW;
            
            // elements on the top of the mapView
            // getting JSOobject
            let elementsOnTop = call.getObject("elementsOnTop", JSObject());
            // getting two dimensional array of child elements
            let twoDArray =  elementsOnTop["innerEles"] as? [[Int]];
            for i in 0..<twoDArray!.count {
                var newBoundingRect = BoundingRect();
                //     width: Math.round(boundingRectangleOfChild.width),
                //     height: Math.round(boundingRectangleOfChild.height),
                //     x: Math.round(boundingRectangleOfChild.x),
                //     y: Math.round(boundingRectangleOfChild.y),
                newBoundingRect.width = Double(twoDArray![i][0]);
                newBoundingRect.height = Double(twoDArray![i][1]);
                newBoundingRect.x = Double(twoDArray![i][2]);
                newBoundingRect.y = Double(twoDArray![i][3]);
                self.arrayOfHTMLElements.append(newBoundingRect);
            }
            
            
            self.bridge?.webView?.addSubview(customMapViewController.view)
            
            customMapViewController.mapView.delegate = customMapViewController;
            
            self.customMapViewControllers[customMapViewController.id] = customMapViewController;
            
            
            // Bring the WebView in front of the MapView
            // This allows us to overlay the MapView in HTML/CSS
            // subview[0] - is map
            self.bridge?.webView?.sendSubviewToBack(customMapViewController.view)
       
            
            
            if(self.hasTopView == false) {
                let overlayView: OverlayView = OverlayView()
                overlayView.mainClass = self
                overlayView.isOpaque = true;
                overlayView.backgroundColor = UIColor.clear
                overlayView.tag = self.TAG_NUMBER_FOR_TOP_OVERLAY_UIVIEW;
                self.bridge?.webView?.addSubview(overlayView)
                
                self.hasTopView = true
            }
            
        }
        
        DispatchQueue.main.sync {
        // Hide the background
        self.webView?.isOpaque = false;
        self.webView?.backgroundColor = .clear;
        self.webView?.scrollView.backgroundColor = .clear;
        self.webView?.scrollView.isOpaque = false;
        }
        
    }
    
    @objc func updateMap(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId")!;
        
        DispatchQueue.main.async {
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                let preferences = call.getObject("preferences", JSObject());
                customMapViewController?.mapPreferences.updateFromJSObject(preferences);
                
                customMapViewController?.invalidateMap();
            } else {
                call.reject("map not found");
            }
        }
        
    }
    
    @objc func clearMap(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId")!;
        
        DispatchQueue.main.async {
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                customMapViewController?.clear();
            } else {
                call.reject("map not found");
            }
        }
        
    }
    
    @objc func addMarker(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            // find what mapview marker belongs
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                // get preferences of new marker
                let preferences = call.getObject("preferences", JSObject());
                
                let marker = CustomMarker();
                marker.updateFromJSObject(preferences: preferences);
                
                // add new marker to the map
                customMapViewController?.addMarker(marker);
                
                call.resolve(CustomMarker.getResultForMarker(marker, mapId));
            } else {
                call.reject("map not found");
            }
        }
    }
    
    @objc func addMarkers(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            // find what mapview marker belongs
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                let markersObj : JSObject? = call.getObject("markers");
                
                let markers = markersObj?["arrayOfMarkers"] as? [Any] ?? [];
                
                var markerArray = [CustomMarker]();
                for item in markers {
                    let markerObject = item as? JSObject ?? JSObject();
                    
                    let preferences = markerObject["preferences"] as? JSObject ?? JSObject();
                    
                    let marker = CustomMarker();
                    marker.updateFromJSObject(preferences: preferences);
                    markerArray.append(marker);

                }
                customMapViewController?.addMarkers(markerArray);
                
                call.resolve();
            } else {
                call.reject("map not found");
            }
        }
    }
    
    @objc func updateMarker(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            // find what mapview marker belongs
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                let markerId: String = call.getString("markerId", "");
                
                // check if marker exists
                
                // get new preferences of existing marker
                let preferences = call.getObject("preferences", JSObject());
                
                
                // update marker on the map
                let isUpdated =  customMapViewController?.updateMarker(markerId, preferences) ?? false;
                if(!isUpdated) {
                    call.reject("marker not updated");
                } else {
                    call.resolve();
                }
            } else {
                call.reject("map not found");
            }
        }
    }
    
    @objc func removeMarker(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            // find what mapview marker belongs
            let customMapViewController = self.customMapViewControllers[mapId];
            
            if (customMapViewController != nil) {
                let markerId: String = call.getString("markerId", "");
                
                // check if marker exists
                customMapViewController?.removeMarker(markerId);
            } else {
                call.reject("map not found");
            }
        }
    }
    
    @objc func didTapInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_INFO_WINDOW);
    }
    
    @objc func didCloseInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_CLOSE_INFO_WINDOW);
    }
    
    @objc func didTapMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MAP);
    }
    
    @objc func didLongPressMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_LONG_PRESS_MAP);
    }
    
    @objc func didTapMarker(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MARKER);
    }
    
    @objc func didTapCluster(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_CLUSTER);
    }
    
    @objc func didTapMyLocationButton(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MY_LOCATION_BUTTON);
    }
    
    @objc func didTapMyLocationDot(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: Events.EVENT_DID_TAP_MY_LOCATION_DOT);
    }
    
    func setCallbackIdForEvent(call: CAPPluginCall, eventName: String) {
        call.keepAlive = true;
        let callbackId = call.callbackId;
        guard let mapId = call.getString("mapId") else { return };
        
        let customMapViewController: CustomMapViewController = customMapViewControllers[mapId]!;
        
        let preventDefault: Bool = call.getBool("preventDefault", false);
        customMapViewController.setCallbackIdForEvent(callbackId: callbackId, eventName: eventName, preventDefault: preventDefault);
    }
    
    override func lastResultForCallbackId(callbackId: String, result: PluginCallResultData) {
        let call = bridge?.savedCall(withID: callbackId);
        call?.resolve(result);
        bridge?.releaseCall(call!);
    }
    
    override func resultForCallbackId(callbackId: String, result: PluginCallResultData?) {
        let call = bridge?.savedCall(withID: callbackId);
        if (result != nil) {
            call?.resolve(result!);
        } else {
            call?.resolve();
        }
    }
    
    
    
    @objc func blockMapViews(_ call: CAPPluginCall) {
        
        DispatchQueue.main.async {
            
            // turn off every map
            for mapview in self.customMapViewControllers {
                mapview.value.view.isUserInteractionEnabled = false;
            }
            
            // and turn on html elements view
            // if (count-1) is the top view, then hmtl layout is second and here must be (count-2)
            let numberOfHTMLElementsOfView: Int = (self.bridge?.webView?.subviews.count)! - 2;
            self.bridge?.webView?.subviews[numberOfHTMLElementsOfView].isUserInteractionEnabled = true
            
            // and if we don't need maps then we dont need and topOverlayView
            // for transmiting touches
            if(self.hasTopView == true) {
                // first view is the top view
                let numberOfTopOverlayView: Int = (self.bridge?.webView?.subviews.count)! - 2;
                self.bridge?.webView?.subviews[numberOfTopOverlayView].isUserInteractionEnabled = false
            }
            
            call.resolve([
                "mapsBlocked": true
            ])
        }
    }
    
    @objc func unblockMapViews(_ call: CAPPluginCall) {
        
        DispatchQueue.main.async {
            
            // turn on every map
            for mapview in self.customMapViewControllers {
                mapview.value.view.isUserInteractionEnabled = true;
            }
            
            
            // and if we need maps then we need topOverlayView
            // for transmiting touches
            if(self.hasTopView == true) {
                // first view is the top view
                let numberOfTopOverlayView: Int = (self.bridge?.webView?.subviews.count)! - 2;
                self.bridge?.webView?.subviews[numberOfTopOverlayView].isUserInteractionEnabled = false
            }
            
            call.resolve([
                "mapsBlocked": false
            ])
        }
    }
    
    @objc func getArrayOfHTMLElements(_ call: CAPPluginCall) {
        
        var result = JSObject();
        var arrayOfJSObjects = [JSObject]();
        
        for rect in self.arrayOfHTMLElements {
            arrayOfJSObjects.append(rect.getJSObject())
        }
        
        
        // sending full array of BoundingRect
        result.updateValue(arrayOfJSObjects, forKey: "arrayOfHTMLElements")
        
        call.resolve(result)
    }
    
    
    
    @objc func setArrayOfHTMLElements(_ call: CAPPluginCall) {
        let response = call.getObject("response", JSObject());

        // optimization thing
        // check if size of array is changed
        var isKeepCapacityOfArray = false;
        isKeepCapacityOfArray = response["isKeepCapacityOfArray"] as? Bool ?? false;
        
        // clear previous html elements in array
        self.arrayOfHTMLElements.removeAll(keepingCapacity: isKeepCapacityOfArray);
        
        // get JS array from call as array of JSObjects
        let myArr = response["arrayOfHTMLElements"] as? [JSObject] ?? [];
        
        
        let br : BoundingRect = BoundingRect();
        
        
        for boundingRectJSObject in myArr{
            br.updateFromJSObject(boundingRectJSObject);
            self.arrayOfHTMLElements.append(br);
            print("Here will be smt when this is work")
            print(String(br.x))
        }
        
        
        call.resolve();
    }
    
    
    @objc func zoomInButtonClick(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]!;
            
            if(customMapViewController != nil) {
                customMapViewController.zoomIn();
                call.resolve();
            } else {
                call.reject("map not found");
            }
            
            
        }
    }
    
    @objc func zoomOutButtonClick(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]!;
            
            if(customMapViewController != nil) {
                customMapViewController.zoomOut();
                call.resolve();
            } else {
                call.reject("map not found");
            }
            
            
        }
    }
    
    @objc func myLocationButtonClick(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            guard let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]
                else {
                    call.reject("map not found");
                    return
                }
            self.locationRequest();
            customMapViewController.myLocationButtonClick();
            call.resolve();
            
        }
    }
    
    private func locationRequest() {
        // this open app specific settings
        func openSettings(alert: UIAlertAction!) {
            if let url = URL.init(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
            }
        }
        

        
        let status = CLLocationManager.authorizationStatus()
            if status == .notDetermined || status == .denied{
                
                // present an alert indicating location authorization required
                // and offer to take the user to Settings for the app via
                // UIApplication -openUrl: and UIApplicationOpenSettingsURLString
                DispatchQueue.main.async {
                    let title :  String?;
                    let message : String?;
                    let titleOfSecondAction : String;
                    if(CLLocationManager.locationServicesEnabled() != true) {
                        title = "Location Services Off"
                        message = "Turn on Location Services in Settings > Privacy to allow \"" + Bundle.appName() + "\" to determine your current location";
                        titleOfSecondAction = "OK";
                    } else {
                        title = "GPS access is restricted. In order to Allow \"" + Bundle.appName() + "\" to Determine Your Location, please give GPS permissions to application";
                        message = nil
                        titleOfSecondAction = "Cancel";
                    }
                    
                    
                    let alert = UIAlertController(title: title,
                                                  message: message,
                                                  preferredStyle: .alert)
                    
                    alert.addAction(UIAlertAction(title: "Settings",
                                                  style: UIAlertAction.Style.default,
                                                  handler: openSettings))
                                    
                    alert.addAction(UIAlertAction(title: titleOfSecondAction, style: UIAlertAction.Style.default, handler: nil))
                    self.bridge?.webView?.window?.rootViewController?.present(alert, animated: true, completion: nil)
                }
            }
    }
    
    @objc func addMarkerCategory(_ call: CAPPluginCall) {
        let id : Int = call.getInt("id", -1);
        let title : String = call.getString("title", "");
        let encodedImage : String = call.getString("base64Data", "");
        
        if (id == -1 ) {
            call.reject("dont have id for category");
            return;
        }
        
        var image : UIImage = UIImage();
        if let decodedData = Data(base64Encoded: encodedImage, options: .ignoreUnknownCharacters) {
            image = UIImage(data: decodedData)!
            image = image.resized(to: SIZE_OF_MARKERS);
        }
        
        MarkerCategory(id, title, image);
        
    }

    
    private func initMarkerCategories() {
        // default marker icon for zero category
        MarkerCategory(0, "default", nil);
        
        // getting map of names of categories and icons of this
        var markerCategoriesNamesAndIcons = fetchMarkersCategoriesFilesFromAssets();
        // sorting keys in alphabetical order for adding catetories in the same order
        var arrayOfKeys = Array(markerCategoriesNamesAndIcons.keys.map{ $0 })
        arrayOfKeys = arrayOfKeys.sorted(by: <)
        
        var i : Int = 1;
        for nameOfCategory in arrayOfKeys {
            MarkerCategory(i, nameOfCategory, markerCategoriesNamesAndIcons[nameOfCategory] as? UIImage ?? nil)
            i += 1;
        }
        
    }
    
    private func fetchMarkersCategoriesFilesFromAssets() -> [String : UIImage?] {
        var returnArray = [String : UIImage?]();

        let fm = FileManager.default
         let listImageName = fm.getListFileNameInBundle(bundlePath: MARKER_CATEGORY_DIRECTORY)
         for imgName in listImageName {
             
             let pattern = #"(.+?)(\.[^.]*$|$)"#
             let regex = try! NSRegularExpression(pattern: pattern)
             var result: [String] = [String]();
             let stringRange = NSRange(location: 0, length: imgName.utf16.count)
             if let firstMatch = regex.firstMatch(in: imgName, range: stringRange) {
                 result = (1 ..< firstMatch.numberOfRanges).map { (imgName as NSString).substring(with: firstMatch.range(at: $0)) }
             } else {
                 result[0] = imgName;
             }
             
             
             
             var image = fm.getImageInBundle(bundlePath: MARKER_CATEGORY_DIRECTORY + "/\(result[0])")
             image = image?.resized(to: SIZE_OF_MARKERS)
             returnArray[imgName] = image;
         }
        return returnArray;
    }
    
    
    @objc func hide(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            guard let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]
                else {
                    call.reject("map not found");
                    return
                }
            customMapViewController.mapView.isHidden = true;
            call.resolve();
        }
    }
    
    @objc func show(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "");
        
        DispatchQueue.main.async {
            guard let customMapViewController: CustomMapViewController = self.customMapViewControllers[mapId]
                else {
                    call.reject("map not found");
                    return
                }
            customMapViewController.mapView.isHidden = false;
            call.resolve();
        }
    }
    
}

extension Bundle {
    static func appName() -> String {
        guard let dictionary = Bundle.main.infoDictionary else {return ""}
        if let appName : String = dictionary["CFBundleName"] as? String {
            return appName
        } else {
            return ""
        }
    }
}


extension FileManager {
    func getListFileNameInBundle(bundlePath: String) -> [String] {

        let fileManager = FileManager.default
        let bundleURL = Bundle.main.bundleURL
        let assetURL = bundleURL.appendingPathComponent(bundlePath)
        do {
            let contents = try fileManager.contentsOfDirectory(at: assetURL, includingPropertiesForKeys: [URLResourceKey.nameKey, URLResourceKey.isDirectoryKey], options: .skipsHiddenFiles)
            return contents.map{$0.lastPathComponent}
        }
        catch {
            return []
        }
    }

    func getImageInBundle(bundlePath: String) -> UIImage? {
        let bundleURL = Bundle.main.bundleURL
        let assetURL = bundleURL.appendingPathComponent(bundlePath)
        return UIImage.init(contentsOfFile: assetURL.relativePath)
    }
}

extension UIImage {
    public func resized(to target: CGSize) -> UIImage {
        let ratio = min(
            target.height / size.height, target.width / size.width
        )
        let new = CGSize(
            width: size.width * ratio, height: size.height * ratio
        )
        let renderer = UIGraphicsImageRenderer(size: new)
        return renderer.image { _ in
            self.draw(in: CGRect(origin: .zero, size: new))
        }
    }
}
