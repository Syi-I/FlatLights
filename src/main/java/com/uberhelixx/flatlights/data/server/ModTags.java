package com.uberhelixx.flatlights.data.server;

import com.uberhelixx.flatlights.FlatLights;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final class Blocks {
        public static final ITag.INamedTag<Block> FLATBLOCKS = mod("flatblock");

        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }
        private static ITag.INamedTag<Block> mod(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation(FlatLights.MOD_ID, path).toString());
        }
    }
    public static final class Items {
        public static final ITag.INamedTag<Item> FLATBLOCKS = mod("flatblock");
        public static final ITag.INamedTag<Item> INGOTS_PRISMATIC = mod("ingots/prismatic");

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }
        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation(FlatLights.MOD_ID, path).toString());
        }
    }
}
