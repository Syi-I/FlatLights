package com.uberhelixx.flatlights.startup.registry;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> NEEDS_PRISMATIC_TOOL = tag("needs_prismatic_tool");
        public static final TagKey<Block> FLATBLOCKS = tag("flatblock");
        
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(FlatLights.MODID, name));
        }
    }
    
    public static class Items {
        public static final TagKey<Item> FLATBLOCKS = tag("flatblock");
        public static final TagKey<Item> INGOTS_PRISMATIC = tag("ingots/prismatic");
        
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(FlatLights.MODID, name));
        }
    }
}
