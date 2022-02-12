import UIKit
import Capacitor
import GoogleMaps

class CustomWKWebView: WKWebView {
    var customMapViews = [String : CustomMapView]();

    open override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        let values = self.customMapViews.map({ $0.value })
        for customMapView in values {
            let convertedPoint = self.convert(point, to: customMapView.GMapView)
            let mapView = customMapView.GMapView.hitTest(convertedPoint, with: event)
            
            if (mapView != nil), scrollView.layer.pixelColorAtPoint(point: convertedPoint) == true{
                return mapView
            }
        }
        return super.hitTest(point, with: event)
    }
}

class CustomMapViewController: CAPBridgeViewController {
    open override func webView(with frame: CGRect, configuration: WKWebViewConfiguration) -> WKWebView {
        let webView = CustomWKWebView(frame: frame, configuration: configuration)
        return webView
    }
}
