package com.uberhelixx.flatlights.startup;

import com.uberhelixx.flatlights.client.renderer.EntangledEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.RisingHeatEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.player.DragonSphereRenderer;
import com.uberhelixx.flatlights.client.renderer.player.PlayerEntangledEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.player.PlayerRisingHeatEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.player.PrismaticBladeMk2Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

import static com.uberhelixx.flatlights.FlatLights.LOGGER;

public class ClientProxy implements IProxy {
    
    @Override
    public void preInit(FMLCommonSetupEvent event) {
    
    }
    
    @Override
    public void init(FMLCommonSetupEvent event) {
        LOGGER.info("[Common Setup] Adding player render layers");
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        for (PlayerRenderer render : new PlayerRenderer[] {skinMap.get("default"), skinMap.get("slim")}) {
            render.addLayer(new PrismaticBladeMk2Renderer(render));
            //render.addLayer(new BladeStanceRenderer(render));
            render.addLayer(new DragonSphereRenderer(render));
            render.addLayer(new PlayerRisingHeatEffectRenderer(render));
            render.addLayer(new PlayerEntangledEffectRenderer(render));
        }
        
        LOGGER.info("[Common Setup] Adding entity render layers");
        //this gets all the entities and puts a new render layer on to every living entity
        for(EntityType<?> entity : ForgeRegistries.ENTITIES) {
            EntityRenderer<?> entityRenderer = Minecraft.getInstance().getRenderManager().renderers.get(entity);
            if(entityRenderer instanceof LivingRenderer) {
                LOGGER.info("added layers to " + entity.toString());
                LivingRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingRenderer<LivingEntity, EntityModel<LivingEntity>>) entityRenderer;
                //vvv add render layers here vvv
                livingRenderer.addLayer(new EntangledEffectRenderer(livingRenderer));
                livingRenderer.addLayer(new RisingHeatEffectRenderer(livingRenderer));
            }
        }
    }
    
    @Override
    public void postInit(FMLCommonSetupEvent event) {
    
    }
}
