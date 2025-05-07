package com.uberhelixx.flatlights.common.item.curio;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.common.item.curio.dragon.DragonCube;
import com.uberhelixx.flatlights.common.item.curio.dragon.DragonPrism;
import com.uberhelixx.flatlights.common.item.curio.dragon.DragonSphere;
import com.uberhelixx.flatlights.common.item.curio.shore.ShoreCube;
import com.uberhelixx.flatlights.common.item.curio.shore.ShorePrism;
import com.uberhelixx.flatlights.common.item.curio.shore.ShoreSphere;
import com.uberhelixx.flatlights.common.item.curio.sun.SunCube;
import com.uberhelixx.flatlights.common.item.curio.sun.SunPrism;
import com.uberhelixx.flatlights.common.item.curio.sun.SunSphere;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.uberhelixx.flatlights.common.item.ModItems.ITEMS;

public class ModCurios {
    public static final DeferredRegister<Item> CURIOS
            = DeferredRegister.create(ForgeRegistries.ITEMS, FlatLights.MODID);
    
    public static void register(IEventBus eventBus) {
        CURIOS.register(eventBus);
        CurioSetNames.pairUp();
    }
    
    //register this to ITEMS registry instead so that curios registry is only the actual curios
    public static final RegistryObject<Item> RANDOM_CURIO = ITEMS.register("random_curio",
            () -> new RandomCurio(new Item.Properties().stacksTo(64)));
    
    public static final RegistryObject<Item> DRAGON_CUBE = CURIOS.register("dragon_cube", DragonCube::new);
    public static final RegistryObject<Item> DRAGON_PRISM = CURIOS.register("dragon_prism", DragonPrism::new);
    public static final RegistryObject<Item> DRAGON_SPHERE = CURIOS.register("dragon_sphere", DragonSphere::new);
    
    public static final RegistryObject<Item> SHORE_CUBE = CURIOS.register("shore_cube", ShoreCube::new);
    public static final RegistryObject<Item> SHORE_PRISM = CURIOS.register("shore_prism", ShorePrism::new);
    public static final RegistryObject<Item> SHORE_SPHERE = CURIOS.register("shore_sphere", ShoreSphere::new);
    
    public static final RegistryObject<Item> SUN_CUBE = CURIOS.register("sun_cube", SunCube::new);
    public static final RegistryObject<Item> SUN_PRISM = CURIOS.register("sun_prism", SunPrism::new);
    public static final RegistryObject<Item> SUN_SPHERE = CURIOS.register("sun_sphere", SunSphere::new);
}
