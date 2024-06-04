package com.uberhelixx.flatlights.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class RisingHeatStateCapability {
    @CapabilityInject(RisingHeatState.class)
    public static Capability<RisingHeatState> CAPABILITY_HEATED_STATE = null;
    
    public static void register() {
        CapabilityManager.INSTANCE.register(
            RisingHeatState.class,
            new RisingHeatState.HeatedStateNBTStorage(),
            RisingHeatState::createDefaultInstance
        );
    }
}
