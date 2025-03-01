package com.uberhelixx.flatlights.effect;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffects {
    public static final DeferredRegister<Effect> POTIONS
            = DeferredRegister.create(ForgeRegistries.POTIONS, FlatLights.MOD_ID);

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

    //potion liquidColorIn is formatted in decimal format
    public static final RegistryObject<Effect> ENTANGLED = POTIONS.register("entangled", () -> new EntangledEffect(EffectType.HARMFUL, 4984012));
    public static final RegistryObject<Effect> ARMOR_SHRED = POTIONS.register("armor_shred", () -> new ArmorShredEffect(EffectType.HARMFUL, 4740710).addAttributesModifier(Attributes.ARMOR, "ce4baba4-105e-11ee-be56-0242ac120002", -0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Effect> BLEED = POTIONS.register("bleed", () -> new BleedEffect(EffectType.HARMFUL, 9386280));
    public static final RegistryObject<Effect> HEALTH_REDUCTION = POTIONS.register("health_reduction", () -> new HealthReductionEffect(EffectType.HARMFUL, 6957095).addAttributesModifier(Attributes.MAX_HEALTH, "01e1c9f8-90d7-48c6-b8bd-fb3c3964d9fd", -0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL));

}
