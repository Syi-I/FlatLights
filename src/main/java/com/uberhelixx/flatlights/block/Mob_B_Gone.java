package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

public class Mob_B_Gone extends Block {

    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;
    public static ToIntFunction<BlockState> MOB_B_GONE_LIGHT_LEVEL = BlockState -> 7;

    public Mob_B_Gone() {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(MOB_B_GONE_LIGHT_LEVEL)
                .notSolid()
                .setOpaque(Mob_B_Gone::isntSolid)
                .sound(SoundType.LANTERN));
    }
    //LivingSpawnEvent.CheckSpawn();
    //getChunk();
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add();
    }

    protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D);
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }
    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) { return 1.0F; }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) { return true; }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String indev = "" + MiscHelpers.coloredText(TextFormatting.RED, "This block is not fully functional.");
        ITextComponent indevTooltip = ITextComponent.getTextComponentOrEmpty(indev);
        tooltip.add(indevTooltip);
        if(!FlatLightsCommonConfig.indevBlocks.get()) {
            String noPlace = "" + MiscHelpers.coloredText(TextFormatting.RED, "This block is disabled and cannot be placed.");
            ITextComponent noPlaceTooltip = ITextComponent.getTextComponentOrEmpty(noPlace);
            tooltip.add(noPlaceTooltip);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
