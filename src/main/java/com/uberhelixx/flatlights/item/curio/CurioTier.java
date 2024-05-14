package com.uberhelixx.flatlights.item.curio;

/**
 * Curio Tier values with associated item model texture override float values and curio buff tier multipliers
 */
public enum CurioTier {
    COMMON (0.1f, 1.0f),
    RARE (0.2f, 1.5f),
    EPIC (0.3f, 2.0f),
    LEGENDARY (0.4f, 2.5f),
    GROWTH (0.5f, 2.5f),
    ERROR(0.6f, 0.0f);

    public final float MODEL_VALUE;
    public final float TIER_MULTIPLIER;
    CurioTier(float modelOverride, float tierMultiplier) {
        this.MODEL_VALUE = modelOverride;
        this.TIER_MULTIPLIER = tierMultiplier;
    }
}
