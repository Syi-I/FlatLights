package com.uberhelixx.flatlights.startup;

import com.uberhelixx.flatlights.client.renderer.EntangledEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.RisingHeatEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.player.DragonSphereRenderer;
import com.uberhelixx.flatlights.client.renderer.player.PlayerEntangledEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.player.PlayerRisingHeatEffectRenderer;
import com.uberhelixx.flatlights.client.renderer.player.PrismaticBladeMk2Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

import static com.uberhelixx.flatlights.FlatLights.LOGGER;

public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLCommonSetupEvent event) {
    
    }
    
    @Override
    public void init(FMLCommonSetupEvent event) {
    
    }
    
    @Override
    public void postInit(FMLCommonSetupEvent event) {
    
    }
    
    @Override
    public void registerEntityLayers(EntityRenderersEvent.AddLayers event) {
        //LOGGER.info("[Client Proxy] Adding player render layers");
        List<EntityRenderer<? extends Player>> skinMap = new ArrayList<>();
        skinMap.add(event.getSkin("default"));
        skinMap.add(event.getSkin("slim"));
        
        for(EntityRenderer<? extends Player> skin : skinMap) {
            if(skin instanceof PlayerRenderer playerRenderer) {
                //LOGGER.info("added layers to " + skin.toString());
                playerRenderer.addLayer(new PrismaticBladeMk2Renderer(playerRenderer));
                //playerRenderer.addLayer(new BladeStanceRenderer(playerRenderer));
                playerRenderer.addLayer(new DragonSphereRenderer(playerRenderer));
                playerRenderer.addLayer(new PlayerRisingHeatEffectRenderer(playerRenderer));
                playerRenderer.addLayer(new PlayerEntangledEffectRenderer(playerRenderer));
            }
        }
        
        LOGGER.info("[Client Proxy] Adding entity render layers");
        //this gets all the entities and puts a new render layer on to every living entity
        for(EntityType<?> entity : ForgeRegistries.ENTITY_TYPES) {
            EntityRenderer<?> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entity);
            if(entityRenderer instanceof LivingEntityRenderer) {
                //LOGGER.info("added layers to " + entity.toString());
                LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) entityRenderer;
                //vvv add render layers here vvv
                livingRenderer.addLayer(new EntangledEffectRenderer(livingRenderer));
                livingRenderer.addLayer(new RisingHeatEffectRenderer(livingRenderer));
            }
        }
    }
}
