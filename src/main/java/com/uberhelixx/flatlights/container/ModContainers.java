package com.uberhelixx.flatlights.container;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, FlatLights.MOD_ID);

    public static final RegistryObject<ContainerType<SpectrumAnvilContainer>> SPECTRUM_ANVIL_CONTAINER
            = CONTAINERS.register("spectrum_anvil_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                PlayerEntity player = inv.player;
                World world = player.getEntityWorld();
                return new SpectrumAnvilContainer(windowId, inv);
            })));


    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
