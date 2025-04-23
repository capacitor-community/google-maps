// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorCommunityGoogleMaps",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapacitorCommunityGoogleMaps",
            targets: ["CapacitorGoogleMaps"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "6.0.0"),
        .package(url: "https://github.com/googlemaps/ios-maps-sdk.git", exact: "8.3.1"),
        .package(url: "https://github.com/SDWebImage/SDWebImage.git", exact: "5.14.3")
    ],
    targets: [
        .target(
            name: "CapacitorGoogleMaps",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm"),
                .product(name: "GoogleMaps", package: "ios-maps-sdk"),
                .product(name: "SDWebImage", package: "SDWebImage")
            ],
            path: "ios/Plugin")
    ]
)
