package com.uberhelixx.flatlights.item.curio.dragonsfinal;

import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DragonsFinalCube extends BaseCurio {
    public DragonsFinalCube(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT stackTags = stack.getTag();

        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !rollCheck(stackTags)) {
            setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.DRAGONSFINAL, BaseCurio.GROWTH_TIER);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
