package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.event.loot.JogoatAdditionModifier;
import com.uberhelixx.flatlights.event.loot.JogoatStructureAdditionModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        //registering new loot modifiers here
        event.getRegistry().registerAll(
            new JogoatAdditionModifier.Serializer().setRegistryName(new ResourceLocation(FlatLights.MOD_ID,"jogoat_fire"))
            //,new JogoatStructureAdditionModifier.Serializer().setRegistryName(new ResourceLocation(FlatLights.MOD_ID,"firestone_in_igloo"))
        );
    }
}