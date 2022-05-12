import SDWebImage

final class SDWebImageCache: ImageURLLoadable {
    static let shared: ImageURLLoadable = SDWebImageCache()

    private let cache = SDImageCache.shared
    private let downloadManager = SDWebImageManager.shared

    private init() {
        cache.config.maxDiskAge = 7 * 24 * 60 * 60
    }

    func image(at urlString: String, resizeWidth: Int, resizeHeight: Int, completion: @escaping VoidReturnClosure<UIImage?>) {
        let key = "\(urlString)\(resizeWidth)\(resizeHeight)"

        SDImageCache.shared.queryCacheOperation(forKey: key, done: { (image, data, type) in
            if let image = image {
                completion(image)
            } else {
                self.downloadManager.loadImage(with: URL(string: urlString), options: [], context: nil, progress: nil) { image,_,_,_,_,_ in
                    let resizedImage = image?.resize(targetSize: CGSize(width: resizeWidth, height: resizeHeight))
                    SDImageCache.shared.store(resizedImage, forKey: key, completion: {
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
