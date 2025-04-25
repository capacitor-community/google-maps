import SDWebImage

final class NativeImageCache: ImageURLLoadable {
    static let shared: ImageURLLoadable = NativeImageCache()

    private lazy var cache: NSCache<AnyObject, AnyObject> = {
        NSCache<AnyObject, AnyObject>()
    }()

    private init(){}

    func image(at urlString: String, resizeWidth: Int, resizeHeight: Int, completion: @escaping VoidReturnClosure<UIImage?>) {
        guard let url = URL(string: urlString) else {
            completion(nil)
            return
        }
        
        // Generate custom key based on the size,
        // so we can cache the resized variant of the image as well.
        let key = "\(urlString)\(resizeWidth)\(resizeHeight)"
        
        if let image = cache.object(forKey: key as AnyObject) as? UIImage {
            // If the resized image is found in the cache,
            // return it.
            completion(image)
        } else {
            // Otherwise, we should download the original image,
            SDWebImageDownloader.shared.downloadImage(with: url, options: [], context: nil, progress: nil) { image, _, _, _ in
                // then resize it to the preferred size,
                guard let resizedImage = image?.resize(targetSize: CGSize(width: resizeWidth, height: resizeHeight)) else {
                    completion(nil)
                    return
                }
                // save it in the cache,
                self.cache.setObject(resizedImage, forKey: key as AnyObject)
                // and return it.
                completion(resizedImage)
            }
        }
    }

    func clear(completion: @escaping NoArgsClosure) {
        cache.removeAllObjects()
        completion()
    }
}
