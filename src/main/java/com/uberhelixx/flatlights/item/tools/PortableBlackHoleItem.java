package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.entity.PortableBlackHoleProjectileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class PortableBlackHoleItem extends Item {
    public PortableBlackHoleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        //set item use cooldown so you can't spam blackholes
        int COOLDOWN_SECONDS = 10;
        if(FlatLightsCommonConfig.blackHoleGeneratorCooldown.get() != null) {
            COOLDOWN_SECONDS = FlatLightsCommonConfig.blackHoleGeneratorCooldown.get();
        }
        int TICK_MULTIPLIER = 20;
        playerIn.getCooldownTracker().setCooldown(this, COOLDOWN_SECONDS * TICK_MULTIPLIER);

        //summon the throwable item in the direction that the player is looking, kinda like an ender pearl
        if (!worldIn.isRemote) {
            PortableBlackHoleProjectileEntity blackHoleProjectileEntity = new PortableBlackHoleProjectileEntity(playerIn, worldIn);
            blackHoleProjectileEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.addEntity(blackHoleProjectileEntity);
        }

        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
