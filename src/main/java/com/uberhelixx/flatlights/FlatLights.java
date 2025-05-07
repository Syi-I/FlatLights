package com.uberhelixx.flatlights;

import com.mojang.logging.LogUtils;
import com.uberhelixx.flatlights.client.screen.ModMenuTypes;
import com.uberhelixx.flatlights.common.block.ModBlocks;
import com.uberhelixx.flatlights.common.blockentity.ModBlockEntities;
import com.uberhelixx.flatlights.common.effect.ModEffects;
import com.uberhelixx.flatlights.common.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.common.entity.ModEntityTypes;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.curio.ModCurios;
import com.uberhelixx.flatlights.common.loot.ModLootModifiers;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.recipe.ModRecipeTypes;
import com.uberhelixx.flatlights.startup.*;
import com.uberhelixx.flatlights.startup.registry.ModAttributes;
import com.uberhelixx.flatlights.startup.registry.ModSoundEvents;
import com.uberhelixx.flatlights.util.ColorPairUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FlatLights.MODID)
public class FlatLights
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "flatlights";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IProxy proxy = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    
    public FlatLights()
    {
        //set up color pairs for dye and shifted version
        ColorPairUtils.pairUp();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModSoundEvents.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModEffects.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModCurios.register(modEventBus);
        ModAttributes.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlatLightsCommonConfig.SPEC, "flatlights-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FlatLightsClientConfig.SPEC, "flatlights-client.toml");
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,() -> this::doInClient);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        PacketHandler.init();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            ClientSetup.doClientSetup(event);
        }
    }
    private void doInClient(){
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerEntityLayers);
    }
    
    private void preInit(FMLCommonSetupEvent event) {
        proxy.preInit(event);
    }
    
    private void init(FMLCommonSetupEvent event) {
        proxy.init(event);
    }
    
    private void postInit(FMLCommonSetupEvent event) {
        proxy.postInit(event);
    }
    
    public void registerEntityLayers(EntityRenderersEvent.AddLayers event) {
        proxy.registerEntityLayers(event);
    }
}
