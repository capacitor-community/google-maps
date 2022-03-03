import UIKit
import Capacitor
import GoogleMaps

class CustomWKWebView: WKWebView {
    var customMapViews = [String : CustomMapView]();

    open override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        let view = super.hitTest(point, with: event)
        let values = self.customMapViews.map({ $0.value })
        for customMapView in values {
            let convertedPoint = self.convert(point, to: customMapView.GMapView)
            let mapView = customMapView.GMapView.hitTest(convertedPoint, with: event)
            let contentView = scrollView.subviews[self.customMapViews.count]
            
            if (mapView != nil), contentView.layer.pixelColorAtPoint(point: point) == true{
                return mapView
            }
        }
        return view
    }
}

class CustomMapViewController: CAPBridgeViewController, UIScrollViewDelegate {
    open override func webView(with frame: CGRect, configuration: WKWebViewConfiguration) -> WKWebView {
        return CustomWKWebView(frame: frame, configuration: configuration)
    }
}
