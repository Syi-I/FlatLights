package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.entity.GravityLiftProjectileEntity;
import com.uberhelixx.flatlights.entity.PortableBlackHoleProjectileEntity;
import com.uberhelixx.flatlights.render.BombSwingProjectileRenderer;
import com.uberhelixx.flatlights.render.GravityLiftRenderer;
import com.uberhelixx.flatlights.render.VoidSphereRenderer;
import com.uberhelixx.flatlights.render.player.DragonSphereRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.Mod;

import static com.uberhelixx.flatlights.FlatLights.MOD_ID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {
    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        MiscHelpers.debugLogger("tried to add special model idk");
        //register custom models here
        ModelLoader.addSpecialModel(VoidSphereRenderer.SPHERE_MODEL);
        ModelLoader.addSpecialModel(new ResourceLocation(MOD_ID, "block/motivational_chair/motivational_chair_wrapper"));
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
        //register curio slot custom icons
        event.addSprite(new ResourceLocation(MOD_ID, "item/curio/curio_cube_icon"));
        event.addSprite(new ResourceLocation(MOD_ID, "item/curio/curio_prism_icon"));
        event.addSprite(new ResourceLocation(MOD_ID, "item/curio/curio_sphere_icon"));
    }
}
