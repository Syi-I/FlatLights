package com.uberhelixx.flatlights.damagesource;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

import javax.annotation.Nullable;

public class ModDamageTypes {

    public static final DamageSource QUANTUM = new DamageSource(FlatLights.MOD_ID + "_quantum").setDamageBypassesArmor();
    public static final DamageSource ENTANGLED = new DamageSource(FlatLights.MOD_ID + "_entangled").setDamageBypassesArmor();
    public static final DamageSource PHYSICAL = new DamageSource(FlatLights.MOD_ID + "_physical");

    public static DamageSource causeIndirectQuantum(Entity source, @Nullable Entity trueSource) {
        return (new IndirectEntityDamageSource(FlatLights.MOD_ID + "_indirectQuantum", source, trueSource)).setDamageBypassesArmor();
    }

    public static DamageSource causeIndirectEntangled(Entity source, @Nullable Entity trueSource) {
        return (new IndirectEntityDamageSource(FlatLights.MOD_ID + "_indirectEntangled", source, trueSource)).setDamageIsAbsolute().setDamageBypassesArmor();
    }

    public static DamageSource causeIndirectPhys(Entity source, @Nullable Entity trueSource) {
        return (new IndirectEntityDamageSource(FlatLights.MOD_ID + "_indirectPhysical", source, trueSource));
    }

    public static DamageSource causeIndirectBleed(Entity source, @Nullable Entity trueSource) {
        return (new IndirectEntityDamageSource(FlatLights.MOD_ID + "_indirectBleed", source, trueSource)).setDamageIsAbsolute().setDamageBypassesArmor();
    }
}
