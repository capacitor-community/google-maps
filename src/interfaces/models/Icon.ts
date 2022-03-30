import { Size } from "./Size";

/**
 * A data class representing icon/marker.
 */
export interface Icon {
  /**
   * URL path to icon
   *
   * @since 3.0.0
   */
  url: string;
  
  /**
   * Target icon size in millimeters
   *
   * @since 3.0.0
   */
  target_size_mm?: Size;
  
  /**
   * Target icon size in pixels
   *
   * @since 3.0.0
   */
  target_size_px?: Size;
}
