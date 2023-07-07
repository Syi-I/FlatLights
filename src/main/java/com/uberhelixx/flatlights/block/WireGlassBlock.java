package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class WireGlassBlock extends GlassBlock {

    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;
    public WireGlassBlock() {
        super(Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setOpaque(WireGlassBlock::isntSolid)
                .setAllowsSpawn(WireGlassBlock::neverAllowSpawn)
                .setSuffocates(WireGlassBlock::isntSolid)
                .setBlocksVision(WireGlassBlock::isntSolid)
                .notSolid()
                .sound(SoundType.GLASS));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return FlatLightsCommonConfig.entityDamageableBlocks.get();
    }

    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    private static Boolean neverAllowSpawn(BlockState state, IBlockReader reader, BlockPos pos, EntityType<?> entity) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(FlatLightsCommonConfig.entityDamageableBlocks.get()) {
            String mobDamageable = "Mob Destructible: " + MiscHelpers.coloredText(TextFormatting.RED, "TRUE");
            ITextComponent mobDamageableTooltip = ITextComponent.getTextComponentOrEmpty(mobDamageable);
            tooltip.add(mobDamageableTooltip);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

}
