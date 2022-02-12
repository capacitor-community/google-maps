import UIKit

private enum Constants {
    static let resizeWidth = 30
    static let resizeHeight = 30
}

final class NativeImageCache: ImageURLLoadable {
    static let shared: ImageURLLoadable = NativeImageCache()

    private lazy var cache: NSCache<AnyObject, AnyObject> = {
        NSCache<AnyObject, AnyObject>()
    }()

    private init(){}

    func image(at urlString: String, completion: @escaping VoidReturnClosure<UIImage?>) {
        guard let url = URL(string: urlString) else {
            completion(nil)
            return
        }
        guard let image = cache.object(forKey: url as AnyObject) as? UIImage else {
            DispatchQueue.global(qos: .userInitiated).async {
                do {
                    let imageData = try Data(contentsOf: url)
                    guard let newImage = UIImage(data: imageData)?.resize(targetSize: CGSize(width: Constants.resizeWidth,
                                                                                          height: Constants.resizeHeight)) else {
                        completion(nil)
                        return
                    }
                    self.cache.setObject(newImage, forKey: url as AnyObject)
                    DispatchQueue.main.async {
                        completion(newImage)
                    }
                } catch {
                    DispatchQueue.main.async {
                        completion(nil)
                    }
                }
            }
            return
        }
        completion(image)
    }

    func clear(completion: @escaping NoArgsClosure) {
        cache.removeAllObjects()
        completion()
    }
}
