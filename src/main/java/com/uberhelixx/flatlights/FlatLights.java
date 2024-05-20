package com.uberhelixx.flatlights;

import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.container.ModContainers;
import com.uberhelixx.flatlights.data.recipes.ModRecipeTypes;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.entity.GravityLiftProjectileEntity;
import com.uberhelixx.flatlights.entity.ModAttributes;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.entity.PortableBlackHoleProjectileEntity;
import com.uberhelixx.flatlights.event.*;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.item.curio.ModCurios;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.item.tools.PrismaticSword;
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
import com.uberhelixx.flatlights.util.ModKeybinds;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;

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
        ModCurios.register(eventBus);
        ModAttributes.register(eventBus);
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
        EVENT_BUS.addListener(EnchantmentEvents::blackhandKnockback);
        EVENT_BUS.addListener(EnchantmentEvents::liftedPickupTruckArmor);
        EVENT_BUS.addListener(EnchantmentEvents::liftedPickupTruckDmg);
        //EVENT_BUS.addListener(EffectRenderer::addEntangledEffect);
        //EVENT_BUS.addListener(EffectRenderer::removeEntangledEffect);
    }

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
            render.addLayer(new DragonSphereRenderer(render));
        }

        
        //this gets all the entities and puts a new render layer on to every living entity
        for(EntityType<?> entity : ForgeRegistries.ENTITIES) {
            //EntityClassification type = entity.getClassification();
            //if(type != EntityClassification.MISC || type == EntityClassification.getClassificationByName("player")) {
                EntityRenderer<?> entityRenderer = Minecraft.getInstance().getRenderManager().renderers.get(entity);
                if(entityRenderer instanceof LivingRenderer) {
                    LOGGER.info("added layer to " + entity.toString());
                    LivingRenderer<LivingEntity, EntityModel<LivingEntity>> livingRenderer = (LivingRenderer<LivingEntity, EntityModel<LivingEntity>>) entityRenderer;
                    livingRenderer.addLayer(new EffectRenderer(livingRenderer));
                }
            //}
        }
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
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PORTABLE_BLACK_HOLE_ENTITY.get(), PortableBlackHoleRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PORTABLE_BLACK_HOLE_PROJECTILE_ENTITY.get(), new RegistryEvents.PortableBlackHoleFactory());
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GRAVITY_LIFT_ENTITY.get(), GravityLiftRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.GRAVITY_LIFT_PROJECTILE_ENTITY.get(), new RegistryEvents.GravityLiftFactory());

        //custom item property for manipulating item models
        event.enqueueWork(() ->
        {
            ItemModelsProperties.registerProperty(ModItems.PRISMATIC_BLADEMK2.get(),
                    new ResourceLocation(FlatLights.MOD_ID, "mode"), (stack, world, living) -> {
                        float mk2Mode = 0.0F;
                        if(stack.getTag() != null) {
                            if (stack.getTag().contains(PrismaticBladeMk2.SPEAR_MODE_TAG) && stack.getTag().getBoolean(PrismaticBladeMk2.SPEAR_MODE_TAG)) {
                                mk2Mode = 1.0F;
                            }
                        }
                        return mk2Mode;
                    });
            ItemModelsProperties.registerProperty(ModItems.PRISMATIC_SWORD.get(),
                    new ResourceLocation(FlatLights.MOD_ID, "mode"), (stack, world, living) -> {
                        float bombMode = 1.0F;
                        if(stack.getTag() != null) {
                            if (stack.getTag().contains(PrismaticSword.BOMB_MODE) && stack.getTag().getBoolean(PrismaticSword.BOMB_MODE)) {
                                bombMode = 0.0F;
                            }
                        }
                        return bombMode;
                    });
            //gives all curios the tier model differentiator
            for(RegistryObject<Item> entry : ModCurios.CURIOS.getEntries()) {
                ItemModelsProperties.registerProperty(entry.get(),
                        new ResourceLocation(FlatLights.MOD_ID, "tier"), (stack, world, living) -> {
                            float curioTier = 0.0F;
                            if (stack.getTag() != null) {
                                if (stack.getTag().contains(CurioUtils.TIER)) {
                                    curioTier = stack.getTag().getFloat(CurioUtils.TIER);
                                }
                            }
                            return curioTier;
                        });
            }
        });

        //keybind setup needs to be done as client setup
        ModKeybinds.register(event);
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
        @SubscribeEvent
        public static void onModelRegistryEvent(ModelRegistryEvent event) {
            MiscHelpers.debugLogger("tried to add special model idk");
            //register custom models here
            ModelLoader.addSpecialModel(VoidSphereRenderer.SPHERE_MODEL);
            ModelLoader.addSpecialModel(new ResourceLocation(FlatLights.MOD_ID, "block/motivational_chair/motivational_chair_wrapper"));
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
}
