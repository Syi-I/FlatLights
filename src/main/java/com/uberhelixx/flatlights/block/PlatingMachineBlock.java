package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLightsConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PlatingMachineBlock extends HorizontalBlock {

    //constants for block hardness (time it takes to mine the block) and resistance (what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public PlatingMachineBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .notSolid()
                .sound(SoundType.NETHERITE));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String indev = "" + MiscHelpers.coloredText(TextFormatting.RED, "This block is not fully functional.");
        ITextComponent indevTooltip = ITextComponent.getTextComponentOrEmpty(indev);
        tooltip.add(indevTooltip);
        if(!FlatLightsConfig.indevBlocks.get()) {
            String noPlace = "" + MiscHelpers.coloredText(TextFormatting.RED, "This block is disabled and cannot be placed.");
            ITextComponent noPlaceTooltip = ITextComponent.getTextComponentOrEmpty(noPlace);
            tooltip.add(noPlaceTooltip);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}