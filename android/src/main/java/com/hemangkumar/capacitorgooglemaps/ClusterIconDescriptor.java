package com.hemangkumar.capacitorgooglemaps;

/**
 * Icon to replace a default cluster marker
 */
class ClusterIconDescriptor {
    public final IconDescriptor iconDescriptor;
    public final int minZoom;
    public final int maxZoom;

    /**
     * Constructor
     * @param iconDescriptor Custom icon, may be null to use default Android marker
     * @param minZoom Minimum map scale to use this custom icon, 0 to skip this limitation
     * @param maxZoom Maximum map scale to use this custom icon, 0 to skip this limitation
     */
    public ClusterIconDescriptor(IconDescriptor iconDescriptor, int minZoom, int maxZoom) {
        this.iconDescriptor = iconDescriptor;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }
}
