//
//  MapClusterIconGenerator.swift
//  CapacitorCommunityCapacitorGooglemapsNative
//
//  Created by Admin on 24.01.2022.
//

import GoogleMapsUtils
import UIKit

class CustomClusterIconGenerator: GMUDefaultClusterIconGenerator {
    
    // clusterSizes count must be equal buckets count
    let clusterSizes: [Int] = [64, 68, 72, 76, 80, 84, 88, 92];
    let buckets: [NSNumber] = [5, 10, 15, 20, 25, 30, 35, 40];
    
    var backgroundImages : [UIImage];
    
    override init() {
        
        backgroundImages = [UIImage](repeating: UIImage(), count: clusterSizes.count);
        var index = 0;
        for size in clusterSizes {
            backgroundImages[index] = (UIImage(named: "cluster")?
                                        .resized(to: CGSize(width: size, height: size)))!;
            index += 1;
        }
        super.init();
    }
    
    override func icon(forSize size: UInt) -> UIImage {
        let bucketIndex : Int = bucketIndex(forSize: Int(size));
        var text : String = String(size);
        
        if(backgroundImages != nil) {
            let image : UIImage = backgroundImages[bucketIndex]
            return textToImage(drawText: text as NSString, inImage: image, font: UIFont.systemFont(ofSize: 14))
        } else {
            let image = UIImage(named: "cluster");
            return textToImage(drawText: text as NSString, inImage: image!, font: UIFont.systemFont(ofSize: 14))
        }
    }
    
    private func bucketIndex(forSize size: Int) -> Int {
        var index = 0
        while index + 1 < buckets.count && Int(buckets[index + 1].uintValue) <= size {
            index += 1
        }
        return index
    }

    private func textToImage(drawText text: NSString, inImage image: UIImage, font: UIFont) -> UIImage {
        
        UIGraphicsBeginImageContext(image.size)
        image.draw(in: CGRect(x: 0, y: 0, width: image.size.width, height: image.size.height))
        
        let textStyle = NSMutableParagraphStyle()
        textStyle.alignment = NSTextAlignment.center
        let textColor = UIColor.white
        let attributes = [
            NSAttributedString.Key.font: font,
            NSAttributedString.Key.paragraphStyle: textStyle,
            NSAttributedString.Key.foregroundColor: textColor]
        
        // vertically center (depending on font)
        let textH = font.lineHeight
        let textY = (image.size.height-textH)/2
        let textRect = CGRect(x: 0, y: textY, width: image.size.width, height: textH)
        text.draw(in: textRect.integral, withAttributes: attributes)
        let result = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return result!
    }
}
