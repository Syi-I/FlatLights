package com.uberhelixx.flatlights.common.item;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.entity.PortableBlackHoleProjectileEntity;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PortableBlackHoleItem extends Item {
    public PortableBlackHoleItem(Properties pProperties) {
        super(pProperties);
    }
    
    //set item use cooldown so you can't spam blackholes
    public static int COOLDOWN_SECONDS = 10;
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.random.nextFloat() * 0.4F + 0.8F));
        
        int TICK_MULTIPLIER = 20;
        pPlayer.getCooldowns().addCooldown(this, getCooldownSeconds() * TICK_MULTIPLIER);
        
        //summon the throwable item in the direction that the player is looking, kinda like an ender pearl
        if (!pLevel.isClientSide()) {
            PortableBlackHoleProjectileEntity blackHoleProjectileEntity = new PortableBlackHoleProjectileEntity(pPlayer, pLevel);
            //sets velocity and direction for the projectile
            blackHoleProjectileEntity.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.0F, 0F);
            blackHoleProjectileEntity.setOwner(pPlayer);
            //MiscUtils.infoLog("set owner of black hole item projectile to " + pPlayer);
            pLevel.addFreshEntity(blackHoleProjectileEntity);
        }
        
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.portable_black_hole_use");
        }
        else {
            Component cd = Component.literal(getCooldownSeconds() + "").withStyle(ChatFormatting.GREEN)
                            .append(Component.literal("s").withStyle(ChatFormatting.WHITE));
            TooltipHelper.labelBrackets(pTooltipComponents, "Black Hole Cooldown", null, cd);
            TooltipHelper.shiftHint(pTooltipComponents);
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    
    public int getCooldownSeconds() {
        if(FlatLightsCommonConfig.blackHoleGeneratorCooldown.get() != null) {
            return FlatLightsCommonConfig.blackHoleGeneratorCooldown.get();
        }
        return COOLDOWN_SECONDS;
    }
}
