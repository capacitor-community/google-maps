
  Pod::Spec.new do |s|
    s.name = 'CapacitorGooglemapsNative'
    s.version = '0.0.5'
    s.summary = 'Plugin using native Maps API for Android and iOS.'
    s.license = 'MIT'
    s.homepage = 'https://github.com/hemangsk/capacitor-googlemaps-native'
    s.author = 'Hemang Kumar'
    s.source = { :git => 'https://github.com/hemangsk/capacitor-googlemaps-native', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
    s.dependency 'GoogleMaps'
    s.static_framework = true
  end