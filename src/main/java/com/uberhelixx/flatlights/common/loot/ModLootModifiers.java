package com.uberhelixx.flatlights.common.loot;

import com.mojang.serialization.Codec;
import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    //custom loot conditions
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, FlatLights.MODID);
    
    public static final RegistryObject<LootItemConditionType> GENERIC_STRUCTURE_CHEST =
            LOOT_CONDITIONS.register("generic_structure_chest", () -> new LootItemConditionType(new ChestCheckCondition.Serializer()));
    
    //custom loot modifiers
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FlatLights.MODID);
    
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> CURIO_STRUCTURE_LOOT =
            LOOT_MODIFIER_SERIALIZERS.register("curio_structure_loot", CurioStructureAdditionModifier.CODEC);
    
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> JOGOAT_FIRE =
            LOOT_MODIFIER_SERIALIZERS.register("jogoat_fire", JogoatAdditionModifier.CODEC);
    
    public static void register(IEventBus eventBus) {
        LOOT_CONDITIONS.register(eventBus);
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
