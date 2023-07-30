package com.uberhelixx.flatlights;

import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.block.SpectrumAnvilBlock;
import com.uberhelixx.flatlights.container.ModContainers;
import com.uberhelixx.flatlights.container.SpectralizerContainer;
import com.uberhelixx.flatlights.data.recipes.ModRecipeTypes;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.enchantments.*;
import com.uberhelixx.flatlights.item.BreadButHighQuality;
import com.uberhelixx.flatlights.item.armor.ModArmorItem;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.armor.PrismaticBoots;
import com.uberhelixx.flatlights.item.armor.PrismaticChestplate;
import com.uberhelixx.flatlights.item.armor.PrismaticHelm;
import com.uberhelixx.flatlights.item.tools.PrismaticBlade;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.screen.LightStorageScreen;
import com.uberhelixx.flatlights.screen.PlatingMachineScreen;
import com.uberhelixx.flatlights.screen.SpectralizerScreen;
import com.uberhelixx.flatlights.screen.SpectrumAnvilScreen;
import com.uberhelixx.flatlights.tileentity.ModTileEntities;
import com.uberhelixx.flatlights.util.MiscEventHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FlatLights.MOD_ID)
public class FlatLights
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "flatlights";

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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlatLightsCommonConfig.SPEC, "flatlights-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FlatLightsClientConfig.SPEC, "flatlights-client.toml");

        //events
        EVENT_BUS.addListener(PrismaticBlade::EnchantDouble);
        EVENT_BUS.addListener(BreadButHighQuality::BreadEnchant);
        EVENT_BUS.addListener(ModArmorItem::DamageReduction);
        EVENT_BUS.addListener(PrismaticChestplate::equipCheck);
        EVENT_BUS.addListener(PrismaticHelm::equipCheck);
        EVENT_BUS.addListener(PrismaticBoots::negateFallDamage);
        EVENT_BUS.addListener(PrismaticBladeMk2::EnchantStack);
        EVENT_BUS.addListener(PrismaticBladeMk2::handlePlayerDropsEvent);
        EVENT_BUS.addListener(PrismaticBladeMk2::handlePlayerCloneEvent);
        EVENT_BUS.addListener(PrismaticBladeMk2::killMobs);
        EVENT_BUS.addListener(PrismaticBladeMk2::droppedItem);
        EVENT_BUS.addListener(PrismaticBladeMk2::onPlayerJoin);
        EVENT_BUS.addListener(SpectrumAnvilBlock::LevelCapping);
        EVENT_BUS.addListener(MiscEventHelpers::indevPlaced);
        EVENT_BUS.addListener(MiscEventHelpers::quantumDmgCheck);
        EVENT_BUS.addListener(MiscEventHelpers::entangledDmgCheck);
        EVENT_BUS.addListener(QuantumStrikeEnchantment::entangleDmg);
        EVENT_BUS.addListener(NeutralizerEnchantment::damageSourceConversion);
        EVENT_BUS.addListener(FlashOfBrillianceEnchantment::xpDropMultiplier);
        EVENT_BUS.addListener(Shimmer2Enchantment::shimmerOverload);
        EVENT_BUS.addListener(PulsingArrowEnchantment::arrowPulseDmg);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("Preinit stuff");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //changes rendering for the glass blocks so its transparent
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_HEXBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_LARGE_HEXBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_TILES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_LARGE_TILES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_FLATBLOCK.get(), RenderType.getTranslucent());

        //register screen for PlatingMachine
        ScreenManager.registerFactory(ModContainers.PLATING_MACHINE_CONTAINER.get(), PlatingMachineScreen::new);
        ScreenManager.registerFactory(ModContainers.SPECTRUM_ANVIL_CONTAINER.get(), SpectrumAnvilScreen::new);
        ScreenManager.registerFactory(ModContainers.LIGHT_STORAGE_CONTAINER.get(), LightStorageScreen::new);
        ScreenManager.registerFactory(ModContainers.SPECTRALIZER_CONTAINER.get(), SpectralizerScreen::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("flatlights", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
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
}
