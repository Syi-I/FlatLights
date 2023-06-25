package com.uberhelixx.flatlights.damagesource;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.util.DamageSource;

public class ModDamageTypes {

    public static final DamageSource QUANTUM = new DamageSource(FlatLights.MOD_ID + "_quantum").setDamageBypassesArmor();
    public static final DamageSource ENTANGLED = new DamageSource(FlatLights.MOD_ID + "_entangled").setDamageBypassesArmor();
    public static final DamageSource PHYSICAL = new DamageSource(FlatLights.MOD_ID + "_physical");

}
