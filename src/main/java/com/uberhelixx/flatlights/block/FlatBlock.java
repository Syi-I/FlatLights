package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLightsConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

public class FlatBlock extends Block {
    //light level, change the number to whatever light level value from 0-15
    public static ToIntFunction<BlockState> FLAT_LIGHT_LEVEL = BlockState -> 15;
    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 0.4f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public FlatBlock() {
        super(AbstractBlock.Properties.create(Material.GLASS)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(FLAT_LIGHT_LEVEL)
                .sound(SoundType.GLASS));
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {

        return FlatLightsConfig.entityDamageableBlocks.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(FlatLightsConfig.entityDamageableBlocks.get()) {
            String mobDamageable = "Mob Destructible: " + MiscHelpers.coloredText(TextFormatting.RED, "TRUE");
            ITextComponent mobDamageableTooltip = ITextComponent.getTextComponentOrEmpty(mobDamageable);
            tooltip.add(mobDamageableTooltip);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
