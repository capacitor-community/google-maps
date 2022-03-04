import SDWebImage

final class SDWebImageCache: ImageURLLoadable {
    static let shared: ImageURLLoadable = SDWebImageCache()

    private let cache = SDImageCache.shared
    private let downloadManager = SDWebImageManager.shared

    private init() {
        cache.config.maxDiskAge = 7 * 24 * 60 * 60
    }

    func image(at urlString: String, resizeWidth: Int, resizeHeight: Int, completion: @escaping VoidReturnClosure<UIImage?>) {
        downloadManager.loadImage(with: URL(string: urlString), options: [], progress: nil) { image,_,_,_,_,_ in
            completion(image?.resize(targetSize: CGSize(width: resizeWidth,
                                                        height: resizeHeight)))
        }
    }

    func clear(completion: @escaping NoArgsClosure) {
        cache.clearDisk(onCompletion: completion)
    }
}
