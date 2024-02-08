package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.entity.GravityLiftEntity;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.entity.PortableBlackHoleProjectileEntity;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class PortableBlackHoleItem extends Item {
    public PortableBlackHoleItem(Properties properties) {
        super(properties);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.portable_black_hole_use"));
            tooltip.add(getItemCooldown());
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    private ITextComponent getItemCooldown() {
        int cooldown = COOLDOWN_SECONDS;
        String formatting = MiscHelpers.coloredText(TextFormatting.GREEN, "" + cooldown);
        return ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Black Hole Cooldown: " + formatting + TextFormatting.WHITE + "s" + TextFormatting.AQUA + "]");
    }

    //set item use cooldown so you can't spam blackholes
    public static final int COOLDOWN_SECONDS = FlatLightsCommonConfig.blackHoleGeneratorCooldown.get() != null ? FlatLightsCommonConfig.blackHoleGeneratorCooldown.get() : 10;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        worldIn.playSound((PlayerEntity)null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        int TICK_MULTIPLIER = 20;
        playerIn.getCooldownTracker().setCooldown(this, COOLDOWN_SECONDS * TICK_MULTIPLIER);

        //summon the throwable item in the direction that the player is looking, kinda like an ender pearl
        if (!worldIn.isRemote) {
            PortableBlackHoleProjectileEntity blackHoleProjectileEntity = new PortableBlackHoleProjectileEntity(playerIn, worldIn);
            //sets velocity and direction for the projectile
            blackHoleProjectileEntity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.0F, 0F);
            worldIn.addEntity(blackHoleProjectileEntity);
        }

        return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
    }
}
