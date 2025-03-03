package com.uberhelixx.flatlights.loot;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

//MOD event bus for registering stuff, otherwise use Bus.FORGE
@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterLootModifierSerializer {

    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        //registering new loot modifiers here
        event.getRegistry().registerAll(
            new JogoatAdditionModifier.Serializer().setRegistryName(new ResourceLocation(FlatLights.MODID,"jogoat_fire"))
            ,new CurioStructureAdditionModifier.Serializer().setRegistryName(new ResourceLocation(FlatLights.MODID,"curio_structure_loot"))
            //,new JogoatStructureAdditionModifier.Serializer().setRegistryName(new ResourceLocation(FlatLights.MODID,"jogoat_structure_loot"))
        );
        
        //register the custom loot condition types here
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("flatlights:generic_structure_chest"), ChestCheckCondition.GENERIC_STRUCTURE_CHEST);
    }
}
