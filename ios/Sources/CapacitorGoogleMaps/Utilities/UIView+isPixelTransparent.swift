import UIKit

extension UIView {
    func isPixelTransparent(at point: CGPoint) -> Bool {
        var pixel: [UInt8] = [0, 0, 0, 0]
        let colorSpace = CGColorSpaceCreateDeviceRGB()
        let bitmapInfo = CGImageAlphaInfo.premultipliedLast.rawValue

        guard let context = CGContext(
            data: &pixel,
            width: 1,
            height: 1,
            bitsPerComponent: 8,
            bytesPerRow: 4,
            space: colorSpace,
            bitmapInfo: bitmapInfo
        ) else {
            return false
        }
        
        /**
         * We could now do as follows:
         * ```
         * context.translateBy(x: -point.x, y: -point.y)
         * self.layer.render(in: context)
         * ```
         * However, it seems that that approach doesn't work on some Metal-backed devices.
         * So instead, we do it like the code below
         */

        let scale = window?.screen.scale ?? contentScaleFactor
        let rect = CGRect(x: floor(point.x), y: floor(point.y), width: 1/scale, height: 1/scale)

        let format = UIGraphicsImageRendererFormat.default()
        format.scale = scale
        format.opaque = false

        let img = UIGraphicsImageRenderer(bounds: rect, format: format).image { _ in
            drawHierarchy(in: bounds, afterScreenUpdates: false)
        }

        guard let cgImage = img.cgImage else {
            return false
        }

        context.draw(cgImage, in: CGRect(x: 0, y: 0, width: 1, height: 1))

        return pixel[3] == 0
    }
}
