package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.entity.ChairEntity;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class MotivationalChairBlock extends HorizontalBlock {

    static final float BLOCK_HARDNESS = 0.01f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public MotivationalChairBlock() {
        super(AbstractBlock.Properties.create(Material.BAMBOO)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .notSolid()
                .setOpaque(MotivationalChairBlock::isntSolid)
                .sound(SoundType.BAMBOO));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
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
        String funfact = MiscHelpers.coloredText(TextFormatting.GRAY, "This model has nearly triple the triangles and double the vertices of Mario Kart Wii Coconut Mall. Motivational.");
        ITextComponent funfactTip = ITextComponent.getTextComponentOrEmpty(funfact);
        String mainTooltipText = MiscHelpers.coloredText(TextFormatting.GRAY, "A great spot to AFK");
        ITextComponent mainTooltip = ITextComponent.getTextComponentOrEmpty(mainTooltipText);
        if(Screen.hasShiftDown()) {
            tooltip.add(funfactTip);
        }
        else {
            tooltip.add(mainTooltip);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ChairEntity.create(worldIn, pos, 0.4, player);
        return ActionResultType.SUCCESS;
    }
}
