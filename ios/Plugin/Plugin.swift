import Foundation
import Capacitor
import GoogleMaps
import SDWebImage

@objc(CapacitorGoogleMaps)
public class CapacitorGoogleMaps: CustomMapViewEvents {

    var GOOGLE_MAPS_KEY: String = "";

    var customMapViews = [String : CustomMapView]();

    var customMarkers = [String : CustomMarker]();

    @objc func initialize(_ call: CAPPluginCall) {

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

        DispatchQueue.main.async {
            let customMapView : CustomMapView = CustomMapView(customMapViewEvents: self)

            self.bridge?.saveCall(call)
            customMapView.savedCallbackIdForCreate = call.callbackId

            let boundingRect = call.getObject("boundingRect", JSObject())
            customMapView.boundingRect.updateFromJSObject(boundingRect)

            let mapCameraPosition = call.getObject("cameraPosition", JSObject())
            customMapView.mapCameraPosition.updateFromJSObject(mapCameraPosition)

            let preferences = call.getObject("preferences", JSObject())
            customMapView.mapPreferences.updateFromJSObject(preferences)

            self.bridge?.viewController?.view.addSubview(customMapView.view)
            self.bridge?.viewController?.view.sendSubviewToBack(customMapView.view)
            self.setupWebView()

            DispatchQueue.main.asyncAfter(deadline: .now() + 0.05) {
                self.setupWebView()
            }

            customMapView.GMapView.delegate = customMapView;

            self.customMapViews[customMapView.id] = customMapView
        }
    }

    @objc func updateMap(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId")!;

        DispatchQueue.main.async {
            let customMapView = self.customMapViews[mapId];

            if (customMapView != nil) {
                let preferences = call.getObject("preferences", JSObject());
                customMapView?.mapPreferences.updateFromJSObject(preferences);

                customMapView?.invalidateMap();
            } else {
                call.reject("map not found");
            }
        }

    }

    @objc func addMarker(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "")

        DispatchQueue.main.async {
            guard let customMapView = self.customMapViews[mapId] else {
                call.reject("map not found")
                return
            }
            let preferences = call.getObject("preferences", JSObject())
            let marker = CustomMarker()
            marker.updateFromJSObject(preferences: preferences)
            marker.map = customMapView.GMapView
            self.customMarkers[marker.id] = marker
            if let url = call.getObject("icon")?["url"] as? String {
                self.imageCache.image(at: url) { image in
                    marker.icon = image
                }
            }
            
            call.resolve(CustomMarker.getResultForMarker(marker))
        }
    }
    
    @objc func addMarkers(_ call: CAPPluginCall) {
        let mapId: String = call.getString("mapId", "")
        
        DispatchQueue.main.async {
            guard let customMapView = self.customMapViews[mapId] else {
                call.reject("map not found")
                return
            }
            
            let markers = List<JSValue>(elements: call.getArray("markers", []))
            self.addMarker(node: markers.first, mapView: customMapView)
            
            call.resolve()
        }
    }

    @objc func removeMarker(_ call: CAPPluginCall) {
        let markerId: String = call.getString("markerId", "");

        DispatchQueue.main.async {
            let customMarker = self.customMarkers[markerId];

            if (customMarker != nil) {
                customMarker?.map = nil;
                self.customMarkers[markerId] = nil;
                call.resolve();
            } else {
                call.reject("marker not found");
            }
        }
    }

    @objc func didTapInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_INFO_WINDOW);
    }

    @objc func didCloseInfoWindow(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_CLOSE_INFO_WINDOW);
    }

    @objc func didTapMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MAP);
    }

    @objc func didLongPressMap(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_LONG_PRESS_MAP);
    }

    @objc func didTapMarker(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MARKER);
    }

    @objc func didTapMyLocationButton(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MY_LOCATION_BUTTON);
    }

    @objc func didTapMyLocationDot(_ call: CAPPluginCall) {
        setCallbackIdForEvent(call: call, eventName: CustomMapView.EVENT_DID_TAP_MY_LOCATION_DOT);
    }

    func setCallbackIdForEvent(call: CAPPluginCall, eventName: String) {
        call.keepAlive = true;
        let callbackId = call.callbackId;
        guard let mapId = call.getString("mapId") else { return };

        let customMapView: CustomMapView = customMapViews[mapId]!;

        let preventDefault: Bool = call.getBool("preventDefault", false);
        customMapView.setCallbackIdForEvent(callbackId: callbackId, eventName: eventName, preventDefault: preventDefault);
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

}

private extension CapacitorGoogleMaps {
    func addMarker(node: Node<JSValue>?,
                   mapView: CustomMapView) {
        guard let node = node else { return }
        let markerObject = node.value as? JSObject ?? JSObject();
        let preferences = markerObject["preferences"] as? JSObject ?? JSObject();
        
        let marker = CustomMarker()
        marker.updateFromJSObject(preferences: preferences)
        marker.map = mapView.GMapView
        self.customMarkers[marker.id] = marker
        if let url = (markerObject["icon"] as? JSObject)?["url"] as? String {
            imageCache.image(at: url) { [weak self] image in
                marker.icon = image
                self?.addMarker(node: node.next, mapView: mapView)
            }
        }
    }
    
    func setupWebView() {
        DispatchQueue.main.async {
            self.webView?.isOpaque = false
            self.webView?.backgroundColor = .clear
            self.webView?.scrollView.backgroundColor = .clear
            self.webView?.scrollView.isOpaque = false

            let javascript = "document.documentElement.style.backgroundColor = 'transparent'"
            self.webView!.evaluateJavaScript(javascript)
        }
    }
}

extension WKWebView {
    open override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        for view in subviews {
            let convertedPoint = self.convert(point, to: view)
            guard view is GMSMapView,
                  let mapView = view.hitTest(convertedPoint, with: event),
                  scrollView.layer.pixelColorAtPoint(point: point).cgColor.alpha == 0.0
                    // Alternative condition - in case of issues with map touch, disable previous and enable next line
                    //layer.pixelColorAtPoint(point: point) == mapView.layer.pixelColorAtPoint(point: convertedPoint)
            else { continue }
            return mapView
        }
        return super.hitTest(point, with: event)
    }
}

extension CapacitorGoogleMaps: ImageCachable {
    var imageCache: ImageURLLoadable {
        SDWebImageCache.shared
    }
}
