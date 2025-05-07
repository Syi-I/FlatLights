package com.uberhelixx.flatlights.startup.registry;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, FlatLights.MODID);
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    
    public static final RegistryObject<Attribute> DODGE_CHANCE = ATTRIBUTES.register("dodge_chance",
            () -> new RangedAttribute(FlatLights.MODID + ".dodge_chance", 0, 0, 100).setSyncable(true));
    
    public static final RegistryObject<Attribute> EMOTIONAL_DMG = ATTRIBUTES.register("emotional_dmg",
            () -> new RangedAttribute(FlatLights.MODID + ".emotional_dmg", 0, 0, Integer.MAX_VALUE).setSyncable(true));
    
    public static final RegistryObject<Attribute> XP_BOOST = ATTRIBUTES.register("xp_boost",
            () -> new RangedAttribute(FlatLights.MODID + ".xp_boost", 0, 0, Integer.MAX_VALUE).setSyncable(true));
    
    public static final RegistryObject<Attribute> HEALING_BOOST = ATTRIBUTES.register("healing_boost",
            () -> new RangedAttribute(FlatLights.MODID + ".healing_boost", 0, 0, Integer.MAX_VALUE).setSyncable(true));
    
    public static final RegistryObject<Attribute> LOOT_ROLL_AMOUNT = ATTRIBUTES.register("loot_roll_amount",
            () -> new RangedAttribute(FlatLights.MODID + ".loot_roll_amount", 0, 0, Integer.MAX_VALUE).setSyncable(true));
    
    public static final RegistryObject<Attribute> LOOT_ROLL_CHANCE = ATTRIBUTES.register("loot_roll_chance",
            () -> new RangedAttribute(FlatLights.MODID + ".loot_roll_chance", 0, 0, 100).setSyncable(true));
    
    
    @SubscribeEvent
    public static void addCustomAttributes(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> entityType : event.getTypes()) {
            if (entityType == EntityType.PLAYER) {
                event.add(entityType, DODGE_CHANCE.get());
                event.add(entityType, EMOTIONAL_DMG.get());
                event.add(entityType, XP_BOOST.get());
                event.add(entityType, HEALING_BOOST.get());
                event.add(entityType, LOOT_ROLL_AMOUNT.get());
                event.add(entityType, LOOT_ROLL_CHANCE.get());
            }
        }
    }
}
