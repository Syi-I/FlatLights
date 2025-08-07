package com.uberhelixx.flatlights.startup;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.client.ModKeybinds;
import com.uberhelixx.flatlights.client.renderer.*;
import com.uberhelixx.flatlights.client.renderer.player.DragonSphereRenderer;
import com.uberhelixx.flatlights.client.screen.LightStorageScreen;
import com.uberhelixx.flatlights.client.screen.ModMenuTypes;
import com.uberhelixx.flatlights.client.screen.PlatingMachineScreen;
import com.uberhelixx.flatlights.client.screen.SpectralizerScreen;
import com.uberhelixx.flatlights.common.entity.ModEntityTypes;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import com.uberhelixx.flatlights.common.item.curio.ModCurios;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.util.lib.LibTagKeys;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.uberhelixx.flatlights.FlatLights.LOGGER;
import static com.uberhelixx.flatlights.util.lib.LibTagKeys.MODE_TAG;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    public static void doClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(ModItems.PRISMATIC_SWORD.get(),
                new ResourceLocation(FlatLights.MODID, "mode"), (stack, level, living, id) -> {
                    float bombMode = 0.0F;
                    if (stack.getTag() != null) {
                        if (stack.getTag().contains(MODE_TAG) && stack.getTag().getBoolean(MODE_TAG)) {
                            bombMode = 1.0F;
                        }
                    }
                    return bombMode;
                });
            //gives all curios the tier model differentiator
            for(RegistryObject<Item> entry : ModCurios.CURIOS.getEntries()) {
                ItemProperties.register(entry.get(),
                    new ResourceLocation(FlatLights.MODID, "tier"), (stack, level, living, id) -> {
                        float curioTier = 0.0F;
                        if (stack.getTag() != null) {
                            if (stack.getTag().contains(CurioUtils.TIER)) {
                                curioTier = stack.getTag().getFloat(CurioUtils.TIER);
                            }
                        }
                        return curioTier;
                    });
            }
            ItemProperties.register(ModItems.PRISMATIC_BLADEMK2.get(),
                new ResourceLocation(FlatLights.MODID, "mode"), (stack, world, living, id) -> {
                    float mk2Mode = 0.0F;
                    if(stack.getTag() != null) {
                        if (stack.getTag().contains(MODE_TAG) && stack.getTag().getInt(MODE_TAG) == PrismaticBladeMk2.SPEAR_MODE) {
                            mk2Mode = 1.0F;
                        }
                    }
                    return mk2Mode;
                });
        });
        
        //SCREENS
        LOGGER.info("[Client Setup] Registering screens");
        MenuScreens.register(ModMenuTypes.PLATING_MACHINE_MENU.get(), PlatingMachineScreen::new);
        MenuScreens.register(ModMenuTypes.LIGHT_STORAGE_MENU.get(), LightStorageScreen::new);
        MenuScreens.register(ModMenuTypes.SPECTRALIZER_MENU.get(), SpectralizerScreen::new);
    }
    
    @SubscribeEvent
    public static void registerEntities(EntityRenderersEvent.RegisterRenderers event) {
        LOGGER.info("[Client Setup] Registering entity renderers");
        event.registerEntityRenderer(ModEntityTypes.BOMB_PROJECTILE.get(), BombProjectileRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.PORTABLE_BLACK_HOLE_ENTITY.get(), PortableBlackHoleRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GRAVITY_LIFT_ENTITY.get(), GravityLiftRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GRAVITY_LIFT_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CHAIR_ENTITY.get(), ChairEntityRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MK2_PROJECTILE_ENTITY.get(), Mk2ProjectileRenderer::new);
    }
    
    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        LOGGER.info("[Client Setup] Registering model renderers");
        event.register(BombProjectileRenderer.BOMB_MODEL);
        event.register(PortableBlackHoleRenderer.SPHERE_MODEL);
        event.register(GravityLiftRenderer.LIFT_BASE_MODEL);
        event.register(new ResourceLocation(FlatLights.MODID, "block/motivational_chair/motivational_chair_wrapper"));
        event.register(Mk2ProjectileRenderer.MK2_PROJECTILE_MODEL);
        event.register(DragonSphereRenderer.INNER_SPHERE_MODEL);
        event.register(DragonSphereRenderer.OUTER_SPHERE_MODEL);
    }
    
    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(ModKeybinds.INSTANCE.CURIO_TOGGLE);
        event.register(ModKeybinds.INSTANCE.MODE_CYCLE);
    }
}
