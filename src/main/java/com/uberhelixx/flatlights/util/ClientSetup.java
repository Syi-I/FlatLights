package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.container.ModContainers;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.item.curio.ModCurios;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.item.tools.PrismaticSword;
import com.uberhelixx.flatlights.render.*;
import com.uberhelixx.flatlights.screen.LightStorageScreen;
import com.uberhelixx.flatlights.screen.PlatingMachineScreen;
import com.uberhelixx.flatlights.screen.SpectralizerScreen;
import com.uberhelixx.flatlights.screen.SpectrumAnvilScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void doClientSetup(FMLClientSetupEvent event) {
        //changes rendering for the glass blocks so its transparent
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_HEXBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_LARGE_HEXBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_TILES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_LARGE_TILES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_FLATBLOCK.get(), RenderType.getTranslucent());
        
        //register screens for containers
        ScreenManager.registerFactory(ModContainers.PLATING_MACHINE_CONTAINER.get(), PlatingMachineScreen::new);
        ScreenManager.registerFactory(ModContainers.SPECTRUM_ANVIL_CONTAINER.get(), SpectrumAnvilScreen::new);
        ScreenManager.registerFactory(ModContainers.LIGHT_STORAGE_CONTAINER.get(), LightStorageScreen::new);
        ScreenManager.registerFactory(ModContainers.SPECTRALIZER_CONTAINER.get(), SpectralizerScreen::new);
        
        //register entity renderers
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.VOID_PROJECTILE.get(), VoidProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BOMB_SWING_PROJECTILE.get(), BombSwingProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.VOID_SPHERE.get(), VoidSphereRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CHAIR_ENTITY.get(), ChairEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PORTABLE_BLACK_HOLE_ENTITY.get(), PortableBlackHoleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY.get(), new ClientEvents.PortableBlackHoleFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GRAVITY_LIFT_ENTITY.get(), GravityLiftRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GRAVITY_LIFT_PROJECTILE_ENTITY.get(), new ClientEvents.GravityLiftFactory());
        
        //custom item property for manipulating item models
        event.enqueueWork(() ->
        {
            ItemModelsProperties.registerProperty(ModItems.PRISMATIC_BLADEMK2.get(),
                    new ResourceLocation(FlatLights.MOD_ID, "mode"), (stack, world, living) -> {
                        float mk2Mode = 0.0F;
                        if(stack.getTag() != null) {
                            if (stack.getTag().contains(PrismaticBladeMk2.SPEAR_MODE_TAG) && stack.getTag().getBoolean(PrismaticBladeMk2.SPEAR_MODE_TAG)) {
                                mk2Mode = 1.0F;
                            }
                        }
                        return mk2Mode;
                    });
            ItemModelsProperties.registerProperty(ModItems.PRISMATIC_SWORD.get(),
                    new ResourceLocation(FlatLights.MOD_ID, "mode"), (stack, world, living) -> {
                        float bombMode = 1.0F;
                        if(stack.getTag() != null) {
                            if (stack.getTag().contains(PrismaticSword.BOMB_MODE) && stack.getTag().getBoolean(PrismaticSword.BOMB_MODE)) {
                                bombMode = 0.0F;
                            }
                        }
                        return bombMode;
                    });
            //gives all curios the tier model differentiator
            for(RegistryObject<Item> entry : ModCurios.CURIOS.getEntries()) {
                ItemModelsProperties.registerProperty(entry.get(),
                        new ResourceLocation(FlatLights.MOD_ID, "tier"), (stack, world, living) -> {
                            float curioTier = 0.0F;
                            if (stack.getTag() != null) {
                                if (stack.getTag().contains(CurioUtils.TIER)) {
                                    curioTier = stack.getTag().getFloat(CurioUtils.TIER);
                                }
                            }
                            return curioTier;
                        });
            }
        });
        
        //keybind setup needs to be done as client setup
        ModKeybinds.register(event);
    }
}
