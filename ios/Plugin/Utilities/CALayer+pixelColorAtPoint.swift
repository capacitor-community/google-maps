extension CALayer {
    func pixelColorAtPoint(point:CGPoint) -> UIColor {
       let pixel = UnsafeMutablePointer<CUnsignedChar>.allocate(capacity: 4)
       let colorSpace = CGColorSpaceCreateDeviceRGB()
       let bitmapInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedLast.rawValue)
       let context = CGContext(data: pixel, width: 1, height: 1, bitsPerComponent: 8, bytesPerRow: 4, space: colorSpace, bitmapInfo: bitmapInfo.rawValue)
       var color: UIColor? = nil

       if let context = context {
           context.translateBy(x: -point.x, y: -point.y)
           render(in: context)

           color = UIColor(red: CGFloat(pixel[0])/255.0,
                           green: CGFloat(pixel[1])/255.0,
                           blue: CGFloat(pixel[2])/255.0,
                           alpha: CGFloat(pixel[3])/255.0)

           pixel.deallocate()
       }
        return color ?? .clear
   }
}
