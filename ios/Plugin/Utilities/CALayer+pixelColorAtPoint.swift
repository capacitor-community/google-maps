extension CALayer {
    func pixelColorAtPoint(point:CGPoint) -> Bool {
        var pixel: [UInt8] = [0, 0, 0, 0]
        let colourSpace = CGColorSpaceCreateDeviceRGB()
        let alphaInfo = CGBitmapInfo(rawValue: CGImageAlphaInfo.premultipliedLast.rawValue)
        let context = CGContext(data: &pixel, width: 1, height: 1, bitsPerComponent: 8, bytesPerRow: 4, space: colourSpace, bitmapInfo: alphaInfo.rawValue)

        context?.translateBy(x: -point.x, y: -point.y)

        self.render(in: context!)

        return CGFloat(pixel[0]) == 0
            && CGFloat(pixel[1]) == 0
            && CGFloat(pixel[2]) == 0
            && CGFloat(pixel[3]) == 0
   }
}
