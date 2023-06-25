package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS
            = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, FlatLights.MOD_ID);

    private static <T extends Enchantment>RegistryObject<T> registerEnch(String name, Supplier<T> ench) {
        return ENCHANTMENTS.register(name, ench);
    }

    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }

    public static final RegistryObject<Enchantment> QUANTUM_STRIKE = registerEnch("quantum_strike", QuantumStrikeEnchantment::new);
    public static final RegistryObject<Enchantment> BONESAW = registerEnch("bonesaw", BonesawEnchantment::new);
    public static final RegistryObject<Enchantment> BLEEDING_EDGE = registerEnch("bleeding_edge", BleedingEdgeEnchantment::new);
    public static final RegistryObject<Enchantment> NEUTRALIZER = registerEnch("neutralizer", NeutralizerEnchantment::new);
    public static final RegistryObject<Enchantment> FLASH_OF_BRILLIANCE = registerEnch("flash_of_brilliance", FlashOfBrillianceEnchantment::new);
}
