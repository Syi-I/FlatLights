package com.uberhelixx.flatlights.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;

import java.util.function.ToIntFunction;

public class FlatlightsPlatingMachine extends HorizontalBlock {

    //constants for block hardness (time it takes to mine the block) and resistance (what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public FlatlightsPlatingMachine(Properties builder) {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .sound(SoundType.NETHERITE));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }
}
