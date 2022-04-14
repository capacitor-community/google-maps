import { MarkerIconSize } from "./../../../../definitions";

/**
 * A data class representing icon/marker.
 */
export interface MarkerIcon {
  /**
   * URL path to icon
   *
   * @since 2.0.0
   */
  url: string;
  /**
   * Target icon size in pixels. Defaults to 30x30 if not specified.
   *
   * @since 2.0.0
   */
  size?: MarkerIconSize;
}
