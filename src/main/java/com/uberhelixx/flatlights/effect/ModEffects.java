package com.uberhelixx.flatlights.effect;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
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

    public static final RegistryObject<Effect> ENTANGLED = POTIONS.register("entangled", () -> new EntangledEffect(EffectType.HARMFUL, 4984012));
    public static final RegistryObject<Effect> ARMOR_SHRED = POTIONS.register("armor_shred", () -> new ArmorShredEffect(EffectType.HARMFUL, 4740710).addAttributesModifier(Attributes.ARMOR, "ce4baba4-105e-11ee-be56-0242ac120002", (double)-0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL));
    public static final RegistryObject<Effect> BLEED = POTIONS.register("bleed", () -> new EntangledEffect(EffectType.HARMFUL, 9386280));

}
