package com.uberhelixx.flatlights.startup.registry;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.core.registries.Registries;
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
        
        public static final TagKey<Item> C_DYES = commonTag("dyes");
        public static final TagKey<Item> C_DYES_BLACK = commonTag("dyes/black");
        public static final TagKey<Item> C_DYES_BLUE = commonTag("dyes/blue");
        public static final TagKey<Item> C_DYES_BROWN = commonTag("dyes/brown");
        public static final TagKey<Item> C_DYES_CYAN = commonTag("dyes/cyan");
        public static final TagKey<Item> C_DYES_GRAY = commonTag("dyes/gray");
        public static final TagKey<Item> C_DYES_GREEN = commonTag("dyes/green");
        public static final TagKey<Item> C_DYES_LIGHT_BLUE = commonTag("dyes/light_blue");
        public static final TagKey<Item> C_DYES_LIGHT_GRAY = commonTag("dyes/light_gray");
        public static final TagKey<Item> C_DYES_LIME = commonTag("dyes/lime");
        public static final TagKey<Item> C_DYES_MAGENTA = commonTag("dyes/magenta");
        public static final TagKey<Item> C_DYES_ORANGE = commonTag("dyes/orange");
        public static final TagKey<Item> C_DYES_PINK = commonTag("dyes/pink");
        public static final TagKey<Item> C_DYES_PURPLE = commonTag("dyes/purple");
        public static final TagKey<Item> C_DYES_RED = commonTag("dyes/red");
        public static final TagKey<Item> C_DYES_WHITE = commonTag("dyes/white");
        public static final TagKey<Item> C_DYES_YELLOW = commonTag("dyes/yellow");
        
        private static TagKey<Item> commonTag(String name) {
            return ItemTags.create(new ResourceLocation("c", name));
        }
    }
}
