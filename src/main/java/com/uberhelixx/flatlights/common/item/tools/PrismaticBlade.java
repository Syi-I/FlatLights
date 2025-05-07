package com.uberhelixx.flatlights.common.item.tools;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.item.tools.basetools.BaseSword;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static java.lang.Math.min;

public class PrismaticBlade extends BaseSword {
    public PrismaticBlade(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    
    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pTarget.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
        pTarget.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60));
        pTarget.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 60));
        pTarget.hurt(ModDamageTypes.causePhysicalDamage(pAttacker), (float) min(FlatLightsCommonConfig.healthDamageCap.get(), (pTarget.getMaxHealth() * FlatLightsCommonConfig.healthDamagePercent.get())));
        pAttacker.heal((float) min(FlatLightsCommonConfig.healthDamageCap.get(), (pTarget.getMaxHealth() * FlatLightsCommonConfig.healthDamagePercent.get())));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_blade_shift");
        }
        else {
            Component percentDesc = Component.literal((FlatLightsCommonConfig.healthDamagePercent.get() * 100) + "%").withStyle(ChatFormatting.RED)
                    .append(Component.literal(" of target's max HP. (Cap of ").withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(FlatLightsCommonConfig.healthDamageCap.get() + "").withStyle(ChatFormatting.RED))
                    .append(Component.literal(" damage)").withStyle(ChatFormatting.WHITE));
            TooltipHelper.labelBrackets(pTooltipComponents, "Percent Damage", null, percentDesc);
            TooltipHelper.shiftHint(pTooltipComponents);
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
