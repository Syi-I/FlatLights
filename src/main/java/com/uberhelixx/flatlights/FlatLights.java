package com.uberhelixx.flatlights;

import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.capability.EntangledStateCapability;
import com.uberhelixx.flatlights.capability.EntangledStateProvider;
import com.uberhelixx.flatlights.capability.RisingHeatStateCapability;
import com.uberhelixx.flatlights.capability.RisingHeatStateProvider;
import com.uberhelixx.flatlights.container.ModContainers;
import com.uberhelixx.flatlights.data.recipes.ModRecipeTypes;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.entity.ModAttributes;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.curio.ModCurios;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.painting.ModPaintings;
import com.uberhelixx.flatlights.tileentity.ModTileEntities;
import com.uberhelixx.flatlights.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FlatLights.MOD_ID)
public class FlatLights
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "flatlights";
    public static IProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public FlatLights() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        EVENT_BUS.register(this);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //register items and blocks
        ModItems.register(eventBus);
        ModBlocks.register(eventBus);
        ModEnchantments.register(eventBus);
        ModEffects.register(eventBus);
        ModTileEntities.register(eventBus);
        ModContainers.register(eventBus);
        ModRecipeTypes.register(eventBus);
        ModEntityTypes.register(eventBus);
        ModSoundEvents.register(eventBus);
        ModPaintings.register(eventBus);
        ModCurios.register(eventBus);
        ModAttributes.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlatLightsCommonConfig.SPEC, "flatlights-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FlatLightsClientConfig.SPEC, "flatlights-client.toml");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("Preinit stuff");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        //have to initialize packet handler here oop
        PacketHandler.init();
        
        EntangledStateCapability.register();
        EVENT_BUS.register(EntangledStateProvider.EntangledStateProviderEventHandler.class);
        RisingHeatStateCapability.register();
        EVENT_BUS.register(RisingHeatStateProvider.RisingHeatStateProviderEventHandler.class);
        
        MiscHelpers.servoInit();
        this.preInit(event);
        this.init(event);
        this.postInit(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ClientSetup.doClientSetup(event);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("flatlights", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});

        //register curio slots including the three custom slot types
        //InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CURIO.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("flatlights.curios.cube").icon(new ResourceLocation(MOD_ID, "item/curio/curio_cube_icon")).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("flatlights.curios.prism").icon(new ResourceLocation(MOD_ID, "item/curio/curio_prism_icon")).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("flatlights.curios.sphere").icon(new ResourceLocation(MOD_ID, "item/curio/curio_sphere_icon")).build());
    }

    private void processIMC(final InterModProcessEvent event)
    {

    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("Server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("Register blocks");
        }
        
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
}
