
  Pod::Spec.new do |s|
    s.name = 'CapacitorCommunityCapacitorGooglemapsNative'
    s.version = '1.0.1'
    s.summary = 'Plugin using native Maps API for Android and iOS.'
    s.license = 'MIT'
    s.homepage = 'https://github.com/capacitor-community/capacitor-googlemaps-native'
    s.author = 'Hemang Kumar'
    s.source = { :git => 'https://github.com/capacitor-community/capacitor-googlemaps-native', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '12.0'
    s.dependency 'Capacitor'
    s.dependency 'GoogleMaps'
    s.static_framework = true
  end