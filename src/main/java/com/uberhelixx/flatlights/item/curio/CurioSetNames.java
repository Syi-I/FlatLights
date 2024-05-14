package com.uberhelixx.flatlights.item.curio;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;

public class CurioSetNames {
    //used for storing translation key names of curio sets to be used everywhere else
    public static final String DRAGONSFINAL = "flatlights.curioset.dragonsfinal";
    public static final String DRAGONSFINAL_EFFECT = "flatlights.curioeffect.dragonsfinal";
    public static final String SHORE = "flatlights.curioset.shore";
    public static final String SHORE_EFFECT = "flatlights.curioeffect.shore";

    public static Map<String, String> setPairs = new HashMap<>();

    /**
     * Pair up the corresponding {@code Set Name} and {@code Set Effect} translation keys
     */
    public static void pairUp() {
        setPairs.put(DRAGONSFINAL, DRAGONSFINAL_EFFECT);
        setPairs.put(SHORE, SHORE_EFFECT);
    }

    /**
     * Gets the {@code Set Name} of the input curio
     * @param curio The curio that we are egtting the set name of
     * @return The set name translation key of the curio as a {@link String}, or {@code null} if none
     */
    public static String getName(ItemStack curio) {
        CompoundNBT tag = curio.getTag();
        if(!tag.isEmpty()) {
            if(tag.contains(CurioUtils.SET)) {
                return tag.getString(CurioUtils.SET);
            }
        }
        return null;
    }

    /**
     * Gets the {@code Set Effect} which matches the input {@code Set Name} of the curio
     * @param setIn The name of the curio set that we are getting the set effect for
     * @return The translation key of the corresponding set effect
     */
    public static String getEffect(String setIn) {
        return setPairs.get(setIn);
    }
}
