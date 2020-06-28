
  Pod::Spec.new do |s|
    s.name = 'CapacitorGoogleMaps'
    s.version = '0.0.1'
    s.summary = 'Plugin using native Maps API for Android and iOS.'
    s.license = 'MIT'
    s.homepage = 'https://github.com/hemangsk/capacitor-google-maps'
    s.author = 'Hemang Kumar'
    s.source = { :git => 'https://github.com/hemangsk/capacitor-google-maps', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
    s.dependency 'GoogleMaps'
    s.static_framework = true
  end