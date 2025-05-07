package com.uberhelixx.flatlights.common.item.curio;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;

/**
 * Curio Tier values with associated item model texture override float values and curio buff tier multipliers
 */
public enum CurioTier {
    COMMON (0.1f, 1.0f, "Common", Style.EMPTY.withColor(ChatFormatting.GRAY)),
    RARE (0.2f, 1.5f, "Rare", Style.EMPTY.withColor(ChatFormatting.BLUE)),
    EPIC (0.3f, 2.0f, "Epic", Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE)),
    LEGENDARY (0.4f, 2.5f, "Legendary", Style.EMPTY.withColor(ChatFormatting.GOLD)),
    GROWTH (0.5f, 2.5f, "Growth", Style.EMPTY.withColor(ChatFormatting.GREEN)),
    ERROR(0.6f, 0.0f, "Unknown", Style.EMPTY.withColor(ChatFormatting.BLACK).withObfuscated(true));
    
    private final float MODEL_VALUE;
    private final float TIER_MULTIPLIER;
    private final String TIER_NAME;
    private final Style TIER_COLOR;
    
    /**
     * Curio Tier enums
     * @param modelOverride Determines the item sprite used ingame, used for client setup and as curio tier ID in NBT
     * @param tierMultiplier Stat multiplier for attributes or functions granted by curios (e.g. attack, armor, hp)
     * @param tierName Name of the tier, used for tooltip hints
     * @param tierColor Color formatting for the tier, used for tooltip hints
     */
    CurioTier(float modelOverride, float tierMultiplier, String tierName, Style tierColor) {
        this.MODEL_VALUE = modelOverride;
        this.TIER_MULTIPLIER = tierMultiplier;
        this.TIER_NAME = tierName;
        this.TIER_COLOR = tierColor;
    }
    
    /**
     * Gets the model float value for the input curio's tier
     * @param tier The curio tier ENUM
     * @return The MODEL_VALUE {@link float} of the corresponding curio tier
     */
    public static float getModel(CurioTier tier) {
        return tier.MODEL_VALUE;
    }
    
    /**
     * Gets the multiplier float value for the input curio's tier
     * @param tier The curio tier ENUM
     * @return The TIER_MULTIPLIER {@link float} of the corresponding curio tier
     */
    public static float getMultiplier(CurioTier tier) {
        return tier.TIER_MULTIPLIER;
    }
    
    /**
     * Gets the tier name String value for the input curio's tier
     * @param tier The curio tier ENUM
     * @return The TIER_NAME {@link String} of the corresponding curio tier
     */
    public static String getName(CurioTier tier) {
        return tier.TIER_NAME;
    }
    
    /**
     * Gets the Component Style for the input curio's tier
     * @param tier The curio tier ENUM
     * @return The TIER_COLOR {@link Style} of the corresponding curio tier
     */
    public static Style getStyle(CurioTier tier) {
        return tier.TIER_COLOR;
    }
}
