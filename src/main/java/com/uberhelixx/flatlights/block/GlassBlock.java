package com.uberhelixx.flatlights.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import java.util.function.ToIntFunction;

public class GlassBlock extends Block {

    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;
    public GlassBlock() {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setOpaque(GlassBlock::isntSolid)
                .setAllowsSpawn(GlassBlock::neverAllowSpawn)
                .setSuffocates(GlassBlock::isntSolid)
                .setBlocksVision(GlassBlock::isntSolid)
                .notSolid()
                .sound(SoundType.GLASS));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return false;
    }

    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    private static Boolean neverAllowSpawn(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
        return false;
    }

}
