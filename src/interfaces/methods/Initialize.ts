export interface InitializeOptions {
  /**
   * Defines the pixel ratio of the current device in the current state.
   * Recommended to be set by using `window.devicePixelRatio`.
   * This is needed because real pixels on a device do not necessarily correspond with how pixels are calculated in a WebView.
   *
   * @since 2.0.0
   */
  devicePixelRatio: number;
  /**
   * (iOS only)
   * API Key for Google Maps SDK for iOS.
   * Hence it is only required on iOS.
   *
   * @since 1.0.0
   */
  key?: string;
}
