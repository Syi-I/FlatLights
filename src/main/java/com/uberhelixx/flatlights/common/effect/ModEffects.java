package com.uberhelixx.flatlights.common.effect;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> POTIONS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, FlatLights.MODID);
    
    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
    
    //potion liquidColorIn is formatted in decimal format
    public static final RegistryObject<MobEffect> ENTANGLED = POTIONS.register("entangled", () -> new EntangledEffect(MobEffectCategory.HARMFUL, 4984012));
    public static final RegistryObject<MobEffect> ARMOR_SHRED = POTIONS.register("armor_shred", () -> new ArmorShredEffect(MobEffectCategory.HARMFUL, 4740710).addAttributeModifier(Attributes.ARMOR, "ce4baba4-105e-11ee-be56-0242ac120002", -0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<MobEffect> BLEED = POTIONS.register("bleed", () -> new BleedEffect(MobEffectCategory.HARMFUL, 9386280));
    public static final RegistryObject<MobEffect> HEALTH_REDUCTION = POTIONS.register("health_reduction", () -> new HealthReductionEffect(MobEffectCategory.HARMFUL, 6957095).addAttributeModifier(Attributes.MAX_HEALTH, "01e1c9f8-90d7-48c6-b8bd-fb3c3964d9fd", -0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    
}
