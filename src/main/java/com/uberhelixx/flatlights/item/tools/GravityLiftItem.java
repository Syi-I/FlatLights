package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.entity.GravityLiftEntity;
import com.uberhelixx.flatlights.entity.GravityLiftProjectileEntity;
import com.uberhelixx.flatlights.entity.PortableBlackHoleProjectileEntity;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GravityLiftItem extends Item {
    public GravityLiftItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            String liftUse = MiscHelpers.coloredText(TextFormatting.DARK_PURPLE, "Ride the gravity lift to reach higher places. Sneak to jump out from the lift early, otherwise the lift throws players out right before it expires.");
            ITextComponent liftUseTooltip = ITextComponent.getTextComponentOrEmpty(liftUse);
            tooltip.add(liftUseTooltip);
            tooltip.add(getLiftTime());
            tooltip.add(getLiftCooldown());
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private ITextComponent getLiftTime() {
        int liftTime = GravityLiftEntity.SECONDS;
        String formatting = MiscHelpers.coloredText(TextFormatting.GREEN, "" + liftTime);
        return ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Lift Lifetime: " + formatting + TextFormatting.WHITE + "s" + TextFormatting.AQUA + "]");
    }

    private ITextComponent getLiftCooldown() {
        int cooldown = COOLDOWN_SECONDS;
        String formatting = MiscHelpers.coloredText(TextFormatting.GREEN, "" + cooldown);
        return ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Lift Cooldown: " + formatting + TextFormatting.WHITE + "s" + TextFormatting.AQUA + "]");
    }

    public static final int COOLDOWN_SECONDS = FlatLightsCommonConfig.gravityLiftCooldown.get() != null ? FlatLightsCommonConfig.gravityLiftCooldown.get() : 5;
    int TICK_MULTIPLIER = 20;
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        //set item use cooldown so you can't spam lifts
        playerIn.getCooldownTracker().setCooldown(this, COOLDOWN_SECONDS * TICK_MULTIPLIER);

        //summon the throwable item in the direction that the player is looking, kinda like an ender pearl
        if (!worldIn.isRemote) {
            GravityLiftProjectileEntity gravityLiftProjectileEntity = new GravityLiftProjectileEntity(playerIn, worldIn);
            //sets velocity and direction for the projectile
            gravityLiftProjectileEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 0.4F, 0F);
            worldIn.addEntity(gravityLiftProjectileEntity);
        }

        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
