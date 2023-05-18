package com.uberhelixx.flatlights;

import com.google.common.collect.Ordering;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.block.SpectrumAnvilBlock;
import com.uberhelixx.flatlights.container.ModContainers;
import com.uberhelixx.flatlights.item.BreadButHighQuality;
import com.uberhelixx.flatlights.item.armor.ModArmorItem;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.armor.PrismaticBoots;
import com.uberhelixx.flatlights.item.armor.PrismaticChestplate;
import com.uberhelixx.flatlights.item.armor.PrismaticHelm;
import com.uberhelixx.flatlights.item.tools.PrismaticBlade;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.util.MiscEventHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
        ModContainers.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlatLightsConfig.SPEC, "flatlights-common.toml");

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
    }

    public static Comparator<ItemStack> tabSort;

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("Preinit stuff");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        List<Item> tabOrder = Arrays.asList(
                //FLATBLOCKS
                ModBlocks.BLACK_FLATBLOCK.get().asItem(),
                ModBlocks.BLUE_FLATBLOCK.get().asItem(),
                ModBlocks.BROWN_FLATBLOCK.get().asItem(),
                ModBlocks.CYAN_FLATBLOCK.get().asItem(),
                ModBlocks.GRAY_FLATBLOCK.get().asItem(),
                ModBlocks.GREEN_FLATBLOCK.get().asItem(),
                ModBlocks.LIGHT_BLUE_FLATBLOCK.get().asItem(),
                ModBlocks.LIGHT_GRAY_FLATBLOCK.get().asItem(),
                ModBlocks.LIME_FLATBLOCK.get().asItem(),
                ModBlocks.MAGENTA_FLATBLOCK.get().asItem(),
                ModBlocks.ORANGE_FLATBLOCK.get().asItem(),
                ModBlocks.PINK_FLATBLOCK.get().asItem(),
                ModBlocks.PURPLE_FLATBLOCK.get().asItem(),
                ModBlocks.RED_FLATBLOCK.get().asItem(),
                ModBlocks.WHITE_FLATBLOCK.get().asItem(),
                ModBlocks.YELLOW_FLATBLOCK.get().asItem(),
                //HEXBLOCKS
                ModBlocks.BLACK_HEXBLOCK.get().asItem(),
                ModBlocks.BLUE_HEXBLOCK.get().asItem(),
                ModBlocks.BROWN_HEXBLOCK.get().asItem(),
                ModBlocks.CYAN_HEXBLOCK.get().asItem(),
                ModBlocks.GRAY_HEXBLOCK.get().asItem(),
                ModBlocks.GREEN_HEXBLOCK.get().asItem(),
                ModBlocks.LIGHT_BLUE_HEXBLOCK.get().asItem(),
                ModBlocks.LIGHT_GRAY_HEXBLOCK.get().asItem(),
                ModBlocks.LIME_HEXBLOCK.get().asItem(),
                ModBlocks.MAGENTA_HEXBLOCK.get().asItem(),
                ModBlocks.ORANGE_HEXBLOCK.get().asItem(),
                ModBlocks.PINK_HEXBLOCK.get().asItem(),
                ModBlocks.PURPLE_HEXBLOCK.get().asItem(),
                ModBlocks.RED_HEXBLOCK.get().asItem(),
                ModBlocks.WHITE_HEXBLOCK.get().asItem(),
                ModBlocks.YELLOW_HEXBLOCK.get().asItem(),
                //LARGE HEXBLOCKS
                ModBlocks.BLACK_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.BLUE_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.BROWN_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.CYAN_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.GRAY_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.GREEN_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.LIGHT_BLUE_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.LIGHT_GRAY_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.LIME_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.MAGENTA_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.ORANGE_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.PINK_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.PURPLE_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.RED_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.WHITE_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.YELLOW_LARGE_HEXBLOCK.get().asItem(),
                //TILES
                ModBlocks.BLACK_TILES.get().asItem(),
                ModBlocks.BLUE_TILES.get().asItem(),
                ModBlocks.BROWN_TILES.get().asItem(),
                ModBlocks.CYAN_TILES.get().asItem(),
                ModBlocks.GRAY_TILES.get().asItem(),
                ModBlocks.GREEN_TILES.get().asItem(),
                ModBlocks.LIGHT_BLUE_TILES.get().asItem(),
                ModBlocks.LIGHT_GRAY_TILES.get().asItem(),
                ModBlocks.LIME_TILES.get().asItem(),
                ModBlocks.MAGENTA_TILES.get().asItem(),
                ModBlocks.ORANGE_TILES.get().asItem(),
                ModBlocks.PINK_TILES.get().asItem(),
                ModBlocks.PURPLE_TILES.get().asItem(),
                ModBlocks.RED_TILES.get().asItem(),
                ModBlocks.WHITE_TILES.get().asItem(),
                ModBlocks.YELLOW_TILES.get().asItem(),
                //LARGE TILES
                ModBlocks.BLACK_LARGE_TILES.get().asItem(),
                ModBlocks.BLUE_LARGE_TILES.get().asItem(),
                ModBlocks.BROWN_LARGE_TILES.get().asItem(),
                ModBlocks.CYAN_LARGE_TILES.get().asItem(),
                ModBlocks.GRAY_LARGE_TILES.get().asItem(),
                ModBlocks.GREEN_LARGE_TILES.get().asItem(),
                ModBlocks.LIGHT_BLUE_LARGE_TILES.get().asItem(),
                ModBlocks.LIGHT_GRAY_LARGE_TILES.get().asItem(),
                ModBlocks.LIME_LARGE_TILES.get().asItem(),
                ModBlocks.MAGENTA_LARGE_TILES.get().asItem(),
                ModBlocks.ORANGE_LARGE_TILES.get().asItem(),
                ModBlocks.PINK_LARGE_TILES.get().asItem(),
                ModBlocks.PURPLE_LARGE_TILES.get().asItem(),
                ModBlocks.RED_LARGE_TILES.get().asItem(),
                ModBlocks.WHITE_LARGE_TILES.get().asItem(),
                ModBlocks.YELLOW_LARGE_TILES.get().asItem(),
                //GLASS
                ModBlocks.GLASS_FLATBLOCK.get().asItem(),
                ModBlocks.GLASS_HEXBLOCK.get().asItem(),
                ModBlocks.GLASS_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.GLASS_TILES.get().asItem(),
                ModBlocks.GLASS_LARGE_TILES.get().asItem(),
                //OTHER
                ModBlocks.PRISMATIC_BLOCK.get().asItem(),
                ModBlocks.LIME_BRICK.get().asItem(),
                //ITEMS
                ModItems.BIG_BREAD.get(),
                ModItems.GUN_RAT.get(),
                ModItems.PRISMATIC_BLADE.get(),
                ModItems.PRISMATIC_BLADEMK2.get(),
                ModItems.PRISMA_NUCLEUS.get(),
                ModItems.PRISMATIC_BOOTS.get(),
                ModItems.PRISMATIC_LEGGINGS.get(),
                ModItems.PRISMATIC_CHESTPLATE.get(),
                ModItems.PRISMATIC_HELMET.get(),
                //indev
                ModBlocks.MOB_B_GONE.get().asItem(),
                ModBlocks.PLATING_MACHINE.get().asItem(),
                ModBlocks.SPECTRUM_ANVIL.get().asItem()
        );
        tabSort = Ordering.explicit(tabOrder).onResultOf(ItemStack::getItem);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //changes rendering for the glass blocks so its transparent
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_HEXBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_LARGE_HEXBLOCK.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_TILES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_LARGE_TILES.get(), RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(ModBlocks.GLASS_FLATBLOCK.get(), RenderType.getTranslucent());
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
