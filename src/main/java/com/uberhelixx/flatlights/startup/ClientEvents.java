package com.uberhelixx.flatlights.startup;

import com.uberhelixx.flatlights.common.entity.GravityLiftProjectileEntity;
import com.uberhelixx.flatlights.common.entity.PortableBlackHoleProjectileEntity;
import com.uberhelixx.flatlights.client.renderer.BombSwingProjectileRenderer;
import com.uberhelixx.flatlights.client.renderer.GravityLiftRenderer;
import com.uberhelixx.flatlights.client.renderer.VoidSphereRenderer;
import com.uberhelixx.flatlights.client.renderer.player.DragonSphereRenderer;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.Mod;

import static com.uberhelixx.flatlights.FlatLights.LOGGER;
import static com.uberhelixx.flatlights.FlatLights.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        LOGGER.info("[Model Registry Event] tried to add special models idk");
        //register custom models here
        ModelLoader.addSpecialModel(VoidSphereRenderer.SPHERE_MODEL);
        ModelLoader.addSpecialModel(new ResourceLocation(MODID, "block/motivational_chair/motivational_chair_wrapper"));
        ModelLoader.addSpecialModel(GravityLiftRenderer.LIFT_BASE_MODEL);
        ModelLoader.addSpecialModel(BombSwingProjectileRenderer.BOMB_MODEL);
        ModelLoader.addSpecialModel(DragonSphereRenderer.INNER_SPHERE_MODEL);
        ModelLoader.addSpecialModel(DragonSphereRenderer.OUTER_SPHERE_MODEL);
    }
    
    //needed for rendering throwable item
    public static class PortableBlackHoleFactory implements IRenderFactory<PortableBlackHoleProjectileEntity> {
        @Override
        public EntityRenderer<? super PortableBlackHoleProjectileEntity> createRenderFor(EntityRendererManager manager) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            return new SpriteRenderer<>(manager, itemRenderer);
        }
    }
    
    //needed for rendering throwable item
    public static class GravityLiftFactory implements IRenderFactory<GravityLiftProjectileEntity> {
        @Override
        public EntityRenderer<? super GravityLiftProjectileEntity> createRenderFor(EntityRendererManager manager) {
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            return new SpriteRenderer<>(manager, itemRenderer);
        }
    }
    
    @SubscribeEvent
    public static void curiosIconRegistryEvent(TextureStitchEvent.Pre event) {
        LOGGER.info("[Texture Stitch Event] Adding new curio icons");
        //register curio slot custom icons
        event.addSprite(new ResourceLocation(MODID, "item/curio/curio_cube_icon"));
        event.addSprite(new ResourceLocation(MODID, "item/curio/curio_prism_icon"));
        event.addSprite(new ResourceLocation(MODID, "item/curio/curio_sphere_icon"));
    }
}
