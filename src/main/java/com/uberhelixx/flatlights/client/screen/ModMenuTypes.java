package com.uberhelixx.flatlights.client.screen;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, FlatLights.MODID);
    
    public static final RegistryObject<MenuType<PlatingMachineMenu>> PLATING_MACHINE_MENU =
            registerMenuType("plating_machine_menu", PlatingMachineMenu::new);
    
    public static final RegistryObject<MenuType<LightStorageMenu>> LIGHT_STORAGE_MENU =
            registerMenuType("light_storage_menu", LightStorageMenu::new);
    
    public static final RegistryObject<MenuType<SpectralizerMenu>> SPECTRALIZER_MENU =
            registerMenuType("spectralizer_menu", SpectralizerMenu::new);
    
    
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }
    
    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
