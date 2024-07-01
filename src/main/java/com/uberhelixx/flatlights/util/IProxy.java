package com.uberhelixx.flatlights.util;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


public interface IProxy {
    
    void preInit(FMLCommonSetupEvent event);
    
    void init(FMLCommonSetupEvent event);
    
    void postInit(FMLCommonSetupEvent event);
}
