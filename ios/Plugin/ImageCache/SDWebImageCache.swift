import SDWebImage

private enum Constants {
    static let resizeWidth = 30
    static let resizeHeight = 30
}

final class SDWebImageCache: ImageURLLoadable {
    static let shared: ImageURLLoadable = SDWebImageCache()

    private let cache = SDImageCache.shared
    private let downloadManager = SDWebImageManager.shared

    private init() {
        cache.config.maxDiskAge = 7 * 24 * 60 * 60
    }

    func image(at urlString: String, completion: @escaping VoidReturnClosure<UIImage?>) {
        downloadManager.loadImage(with: URL(string: urlString), options: [], progress: nil) { image,_,_,_,_,_ in
            completion(image?.resize(targetSize: CGSize(width: Constants.resizeWidth,
                                                        height: Constants.resizeHeight)))
        }
    }

    func clear(completion: @escaping NoArgsClosure) {
        cache.clearDisk(onCompletion: completion)
    }


}
