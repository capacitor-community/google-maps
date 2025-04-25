import SDWebImage

final class SDWebImageCache: ImageURLLoadable {
    static let shared: ImageURLLoadable = SDWebImageCache()

    private let cache = SDImageCache.shared
    private let downloadManager = SDWebImageManager.shared

    private init() {
        cache.config.maxDiskAge = 7 * 24 * 60 * 60
    }

    func image(at urlString: String, resizeWidth: Int, resizeHeight: Int, completion: @escaping VoidReturnClosure<UIImage?>) {
        guard let url = URL(string: urlString) else {
            completion(nil)
            return
        }
        
        // Generate custom key based on the size,
        // so we can cache the resized variant of the image as well.
        let key = "\(urlString)\(resizeWidth)\(resizeHeight)"

        SDImageCache.shared.queryCacheOperation(forKey: key, done: { (image, data, type) in
            if let image = image {
                // If the resized image is found in the cache,
                // return it.
                completion(image)
            } else {
                // Otherwise, we should download the original image,
                self.downloadManager.loadImage(with: url, options: [], context: nil, progress: nil) { image, _, _, _, _, _ in
                    // then resize it to the preferred size,
                    guard let resizedImage = image?.resize(targetSize: CGSize(width: resizeWidth, height: resizeHeight)) else {
                        completion(nil)
                        return
                    }
                    // save it in the cache,
                    SDImageCache.shared.store(resizedImage, forKey: key, completion: {
                        // and return it.
                        completion(resizedImage)
                    })
                }
            }
        })
    }

    func clear(completion: @escaping NoArgsClosure) {
        cache.clearDisk(onCompletion: completion)
    }
}
