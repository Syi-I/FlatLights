package com.uberhelixx.flatlights;

import com.google.common.collect.Ordering;
import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.container.ModContainers;
import com.uberhelixx.flatlights.data.recipes.ModRecipeTypes;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.event.*;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.painting.ModPaintings;
import com.uberhelixx.flatlights.render.*;
import com.uberhelixx.flatlights.screen.LightStorageScreen;
import com.uberhelixx.flatlights.screen.PlatingMachineScreen;
import com.uberhelixx.flatlights.screen.SpectralizerScreen;
import com.uberhelixx.flatlights.screen.SpectrumAnvilScreen;
import com.uberhelixx.flatlights.tileentity.ModTileEntities;
import com.uberhelixx.flatlights.util.MiscEventHelpers;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
import java.util.Map;

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
        ModEntityTypes.register(eventBus);
        ModSoundEvents.register(eventBus);
        ModPaintings.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, FlatLightsCommonConfig.SPEC, "flatlights-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, FlatLightsClientConfig.SPEC, "flatlights-client.toml");

        //events
        EVENT_BUS.addListener(AnvilEvents::EnchantDouble);
        EVENT_BUS.addListener(AnvilEvents::BreadEnchant);
        EVENT_BUS.addListener(ArmorEvents::DamageReduction);
        EVENT_BUS.addListener(ArmorEvents::chestplateEquip);
        EVENT_BUS.addListener(ArmorEvents::helmetEquip);
        EVENT_BUS.addListener(ArmorEvents::negateFallDamage);
        EVENT_BUS.addListener(PrismaticBladeMk2Events::EnchantStack);
        EVENT_BUS.addListener(PrismaticBladeMk2Events::handlePlayerDropsEvent);
        EVENT_BUS.addListener(PrismaticBladeMk2Events::handlePlayerCloneEvent);
        EVENT_BUS.addListener(PrismaticBladeMk2Events::killMobs);
        EVENT_BUS.addListener(PrismaticBladeMk2Events::droppedItem);
        EVENT_BUS.addListener(PrismaticBladeMk2Events::onPlayerJoin);
        EVENT_BUS.addListener(AnvilEvents::LevelCapping);
        EVENT_BUS.addListener(MiscEventHelpers::indevPlaced);
        EVENT_BUS.addListener(MiscEventHelpers::quantumDmgCheck);
        EVENT_BUS.addListener(MiscEventHelpers::entangledDmgCheck);
        EVENT_BUS.addListener(EnchantmentEvents::entangleDmg);
        EVENT_BUS.addListener(EnchantmentEvents::damageSourceConversion);
        EVENT_BUS.addListener(EnchantmentEvents::xpDropMultiplier);
        EVENT_BUS.addListener(EnchantmentEvents::shimmerOverload);
        EVENT_BUS.addListener(EnchantmentEvents::arrowPulseDmg);
        EVENT_BUS.addListener(WeaponEvents::bombSwingTrigger);
        EVENT_BUS.addListener(EnchantmentEvents::removeFromEntangledTeam);
        EVENT_BUS.addListener(EnchantmentEvents::bleedingEdgeStacks);
        EVENT_BUS.addListener(EnchantmentEvents::bonesawStacks);
        EVENT_BUS.addListener(BlockEvents::craftingTableSound);
    }

    public static Comparator<ItemStack> tabSort;
    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("Preinit stuff");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        //have to initialize packet handler here oop
        PacketHandler.init();

        //add render layers to players for both skin types
        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
        for (PlayerRenderer render : new PlayerRenderer[] {skinMap.get("default"), skinMap.get("slim")}) {
            render.addLayer(new PrismaticBladeMk2Renderer(render));
            //render.addLayer(new BladeStanceRenderer(render));
        }

        //list of all items for sorting creative inventory tab
        List<Item> itemOrder = Arrays.asList(
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

                ModBlocks.SALMON_FLATBLOCK.get().asItem(),
                ModBlocks.GOLD_FLATBLOCK.get().asItem(),
                ModBlocks.SANDY_YELLOW_FLATBLOCK.get().asItem(),
                ModBlocks.PALE_YELLOW_FLATBLOCK.get().asItem(),
                ModBlocks.SPRING_GREEN_FLATBLOCK.get().asItem(),
                ModBlocks.PASTEL_GREEN_FLATBLOCK.get().asItem(),
                ModBlocks.TEAL_FLATBLOCK.get().asItem(),
                ModBlocks.CYAN_BLUE_FLATBLOCK.get().asItem(),
                ModBlocks.CERULEAN_FLATBLOCK.get().asItem(),
                ModBlocks.SAPPHIRE_FLATBLOCK.get().asItem(),
                ModBlocks.NAVY_BLUE_FLATBLOCK.get().asItem(),
                ModBlocks.INDIGO_FLATBLOCK.get().asItem(),
                ModBlocks.dark_purple_FLATBLOCK.get().asItem(),
                ModBlocks.RED_PURPLE_FLATBLOCK.get().asItem(),
                ModBlocks.DARK_PINK_FLATBLOCK.get().asItem(),
                ModBlocks.ROSY_PINK_FLATBLOCK.get().asItem(),

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

                ModBlocks.BLACK_PANEL.get().asItem(),
                ModBlocks.BLUE_PANEL.get().asItem(),
                ModBlocks.BROWN_PANEL.get().asItem(),
                ModBlocks.CYAN_PANEL.get().asItem(),
                ModBlocks.GRAY_PANEL.get().asItem(),
                ModBlocks.GREEN_PANEL.get().asItem(),
                ModBlocks.LIGHT_BLUE_PANEL.get().asItem(),
                ModBlocks.LIGHT_GRAY_PANEL.get().asItem(),
                ModBlocks.LIME_PANEL.get().asItem(),
                ModBlocks.MAGENTA_PANEL.get().asItem(),
                ModBlocks.ORANGE_PANEL.get().asItem(),
                ModBlocks.PINK_PANEL.get().asItem(),
                ModBlocks.PURPLE_PANEL.get().asItem(),
                ModBlocks.RED_PANEL.get().asItem(),
                ModBlocks.WHITE_PANEL.get().asItem(),
                ModBlocks.YELLOW_PANEL.get().asItem(),

                ModBlocks.SALMON_PANEL.get().asItem(),
                ModBlocks.GOLD_PANEL.get().asItem(),
                ModBlocks.SANDY_YELLOW_PANEL.get().asItem(),
                ModBlocks.PALE_YELLOW_PANEL.get().asItem(),
                ModBlocks.SPRING_GREEN_PANEL.get().asItem(),
                ModBlocks.PASTEL_GREEN_PANEL.get().asItem(),
                ModBlocks.TEAL_PANEL.get().asItem(),
                ModBlocks.CYAN_BLUE_PANEL.get().asItem(),
                ModBlocks.CERULEAN_PANEL.get().asItem(),
                ModBlocks.SAPPHIRE_PANEL.get().asItem(),
                ModBlocks.NAVY_BLUE_PANEL.get().asItem(),
                ModBlocks.INDIGO_PANEL.get().asItem(),
                ModBlocks.dark_purple_PANEL.get().asItem(),
                ModBlocks.RED_PURPLE_PANEL.get().asItem(),
                ModBlocks.DARK_PINK_PANEL.get().asItem(),
                ModBlocks.ROSY_PINK_PANEL.get().asItem(),

                ModBlocks.BLACK_PILLAR.get().asItem(),
                ModBlocks.BLUE_PILLAR.get().asItem(),
                ModBlocks.BROWN_PILLAR.get().asItem(),
                ModBlocks.CYAN_PILLAR.get().asItem(),
                ModBlocks.GRAY_PILLAR.get().asItem(),
                ModBlocks.GREEN_PILLAR.get().asItem(),
                ModBlocks.LIGHT_BLUE_PILLAR.get().asItem(),
                ModBlocks.LIGHT_GRAY_PILLAR.get().asItem(),
                ModBlocks.LIME_PILLAR.get().asItem(),
                ModBlocks.MAGENTA_PILLAR.get().asItem(),
                ModBlocks.ORANGE_PILLAR.get().asItem(),
                ModBlocks.PINK_PILLAR.get().asItem(),
                ModBlocks.PURPLE_PILLAR.get().asItem(),
                ModBlocks.RED_PILLAR.get().asItem(),
                ModBlocks.WHITE_PILLAR.get().asItem(),
                ModBlocks.YELLOW_PILLAR.get().asItem(),

                ModBlocks.SALMON_PILLAR.get().asItem(),
                ModBlocks.GOLD_PILLAR.get().asItem(),
                ModBlocks.SANDY_YELLOW_PILLAR.get().asItem(),
                ModBlocks.PALE_YELLOW_PILLAR.get().asItem(),
                ModBlocks.SPRING_GREEN_PILLAR.get().asItem(),
                ModBlocks.PASTEL_GREEN_PILLAR.get().asItem(),
                ModBlocks.TEAL_PILLAR.get().asItem(),
                ModBlocks.CYAN_BLUE_PILLAR.get().asItem(),
                ModBlocks.CERULEAN_PILLAR.get().asItem(),
                ModBlocks.SAPPHIRE_PILLAR.get().asItem(),
                ModBlocks.NAVY_BLUE_PILLAR.get().asItem(),
                ModBlocks.INDIGO_PILLAR.get().asItem(),
                ModBlocks.dark_purple_PILLAR.get().asItem(),
                ModBlocks.RED_PURPLE_PILLAR.get().asItem(),
                ModBlocks.DARK_PINK_PILLAR.get().asItem(),
                ModBlocks.ROSY_PINK_PILLAR.get().asItem(),

                ModBlocks.BLACK_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.BLUE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.BROWN_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.CYAN_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.GRAY_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.GREEN_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.LIGHT_BLUE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.LIGHT_GRAY_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.LIME_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.MAGENTA_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.ORANGE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.PINK_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.PURPLE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.RED_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.WHITE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.YELLOW_HORIZONTAL_EDGE.get().asItem(),

                ModBlocks.SALMON_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.GOLD_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.SANDY_YELLOW_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.PALE_YELLOW_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.SPRING_GREEN_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.PASTEL_GREEN_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.TEAL_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.CYAN_BLUE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.CERULEAN_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.SAPPHIRE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.NAVY_BLUE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.INDIGO_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.dark_purple_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.RED_PURPLE_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.DARK_PINK_HORIZONTAL_EDGE.get().asItem(),
                ModBlocks.ROSY_PINK_HORIZONTAL_EDGE.get().asItem(),

                ModBlocks.BLACK_VERTICAL_EDGE.get().asItem(),
                ModBlocks.BLUE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.BROWN_VERTICAL_EDGE.get().asItem(),
                ModBlocks.CYAN_VERTICAL_EDGE.get().asItem(),
                ModBlocks.GRAY_VERTICAL_EDGE.get().asItem(),
                ModBlocks.GREEN_VERTICAL_EDGE.get().asItem(),
                ModBlocks.LIGHT_BLUE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.LIGHT_GRAY_VERTICAL_EDGE.get().asItem(),
                ModBlocks.LIME_VERTICAL_EDGE.get().asItem(),
                ModBlocks.MAGENTA_VERTICAL_EDGE.get().asItem(),
                ModBlocks.ORANGE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.PINK_VERTICAL_EDGE.get().asItem(),
                ModBlocks.PURPLE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.RED_VERTICAL_EDGE.get().asItem(),
                ModBlocks.WHITE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.YELLOW_VERTICAL_EDGE.get().asItem(),

                ModBlocks.SALMON_VERTICAL_EDGE.get().asItem(),
                ModBlocks.GOLD_VERTICAL_EDGE.get().asItem(),
                ModBlocks.SANDY_YELLOW_VERTICAL_EDGE.get().asItem(),
                ModBlocks.PALE_YELLOW_VERTICAL_EDGE.get().asItem(),
                ModBlocks.SPRING_GREEN_VERTICAL_EDGE.get().asItem(),
                ModBlocks.PASTEL_GREEN_VERTICAL_EDGE.get().asItem(),
                ModBlocks.TEAL_VERTICAL_EDGE.get().asItem(),
                ModBlocks.CYAN_BLUE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.CERULEAN_VERTICAL_EDGE.get().asItem(),
                ModBlocks.SAPPHIRE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.NAVY_BLUE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.INDIGO_VERTICAL_EDGE.get().asItem(),
                ModBlocks.dark_purple_VERTICAL_EDGE.get().asItem(),
                ModBlocks.RED_PURPLE_VERTICAL_EDGE.get().asItem(),
                ModBlocks.DARK_PINK_VERTICAL_EDGE.get().asItem(),
                ModBlocks.ROSY_PINK_VERTICAL_EDGE.get().asItem(),

                ModBlocks.GLASS_FLATBLOCK.get().asItem(),
                ModBlocks.GLASS_HEXBLOCK.get().asItem(),
                ModBlocks.GLASS_LARGE_HEXBLOCK.get().asItem(),
                ModBlocks.GLASS_TILES.get().asItem(),
                ModBlocks.GLASS_LARGE_TILES.get().asItem(),

                ModBlocks.PRISMATIC_BLOCK.get().asItem(),

                ModBlocks.PLATING_MACHINE.get().asItem(),
                ModBlocks.SPECTRALIZER.get().asItem(),
                ModBlocks.LIGHT_STORAGE.get().asItem(),

                ModItems.PRISMATIC_INGOT.get(),

                ModItems.HELMET_CORE.get(),
                ModItems.CHEST_CORE.get(),
                ModItems.PANTS_CORE.get(),
                ModItems.BOOTS_CORE.get(),

                ModItems.PRISMATIC_HELMET.get(),
                ModItems.PRISMATIC_CHESTPLATE.get(),
                ModItems.PRISMATIC_LEGGINGS.get(),
                ModItems.PRISMATIC_BOOTS.get(),

                ModItems.PRISMATIC_SWORD.get(),
                ModItems.PRISMATIC_BLADE.get(),
                ModItems.PRISMA_NUCLEUS.get(),
                ModItems.BIG_BREAD.get(),

                ModItems.BLACK_REUSABLE_DYE.get(),
                ModItems.BLUE_REUSABLE_DYE.get(),
                ModItems.BROWN_REUSABLE_DYE.get(),
                ModItems.CYAN_REUSABLE_DYE.get(),
                ModItems.GRAY_REUSABLE_DYE.get(),
                ModItems.GREEN_REUSABLE_DYE.get(),
                ModItems.LIGHT_BLUE_REUSABLE_DYE.get(),
                ModItems.LIGHT_GRAY_REUSABLE_DYE.get(),
                ModItems.LIME_REUSABLE_DYE.get(),
                ModItems.MAGENTA_REUSABLE_DYE.get(),
                ModItems.ORANGE_REUSABLE_DYE.get(),
                ModItems.PINK_REUSABLE_DYE.get(),
                ModItems.PURPLE_REUSABLE_DYE.get(),
                ModItems.RED_REUSABLE_DYE.get(),
                ModItems.WHITE_REUSABLE_DYE.get(),
                ModItems.YELLOW_REUSABLE_DYE.get(),
                ModItems.HUE_SHIFTING_VIAL.get(),

                ModItems.GUN_RAT.get(), //creative only items
                ModItems.PRISMATIC_BLADEMK2.get(),
                ModBlocks.MOB_B_GONE.get().asItem(),
                ModBlocks.SPECTRUM_ANVIL.get().asItem(),
                ModBlocks.LIME_BRICK.get().asItem(),
                ModBlocks.MOTIVATIONAL_CHAIR.get().asItem());
        tabSort = Ordering.explicit(itemOrder).onResultOf(ItemStack::getItem);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
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
        @SubscribeEvent
        public static void onModelRegistryEvent(ModelRegistryEvent event) {
            MiscHelpers.debugLogger("tried to add special model idk");
            ModelLoader.addSpecialModel(VoidSphereRenderer.SPHERE_MODEL);
            ModelLoader.addSpecialModel(new ResourceLocation(FlatLights.MOD_ID, "block/motivational_chair/motivational_chair_wrapper"));
        }
    }
}
