//
//  UIView+isPixelTransparent+Extension
//  Plugin
//
//  Created by Jarret on 12/08/2025.
//  Copyright © 2025 Max Lynch. All rights reserved.
//

import UIKit

extension UIView {

    func isPixelTransparent(at point: CGPoint) -> Bool {
        let scale = window?.screen.scale ?? contentScaleFactor
        let rect = CGRect(x: floor(point.x), y: floor(point.y), width: 1/scale, height: 1/scale)

        let format = UIGraphicsImageRendererFormat.default()
        format.scale = scale
        format.opaque = false

        let img = UIGraphicsImageRenderer(bounds: rect, format: format).image { _ in
            drawHierarchy(in: bounds, afterScreenUpdates: false)
        }
        guard let cg = img.cgImage,
              let data = cg.dataProvider?.data,
              let bytes = CFDataGetBytePtr(data) else { return false }

        return bytes[0] == 0 && bytes[1] == 0 && bytes[2] == 0 && bytes[3] == 0
    }
}
