// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorCommunityGoogleMaps",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorCommunityGoogleMaps",
            targets: ["CapacitorCommunityGoogleMaps"]
        )
    ],
    dependencies: [
        .package(
            url: "https://github.com/ionic-team/capacitor-swift-pm.git",
            from: "7.0.0"
        ),
        .package(
            url: "https://github.com/googlemaps/ios-maps-sdk.git",
            from: "9.0.0"
        ),
        .package(
            url: "https://github.com/SDWebImage/SDWebImage.git",
            from: "5.0.0"
        )
    ],
    targets: [
        .target(
            name: "CapacitorCommunityGoogleMaps",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm"),
                .product(name: "GoogleMaps", package: "ios-maps-sdk"),
                .product(name: "SDWebImage", package: "SDWebImage")
            ],
            path: "ios/Sources/CapacitorGoogleMaps"
        )
    ]
)
