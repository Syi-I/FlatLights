package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static java.lang.Math.min;

public class PrismaticBlade extends SwordItem {

    public PrismaticBlade(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(0, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        target.addPotionEffect(new EffectInstance(Effects.GLOWING, 5));
        target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 3, 4));
        target.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 3, 2));
        //deal either x% of max hp as damage or config cap damage, whichever is lower
        target.attackEntityFrom(ModDamageTypes.causeIndirectPhys(attacker, attacker), (float) min(FlatLightsCommonConfig.healthDamageCap.get(), (target.getMaxHealth() * FlatLightsCommonConfig.healthDamagePercent.get())));
        attacker.heal((float) min(FlatLightsCommonConfig.healthDamageCap.get(), (target.getMaxHealth() * FlatLightsCommonConfig.healthDamagePercent.get())));
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_blade_shift"));
            String percentDmg = "Percent Damage: " + MiscHelpers.coloredText(TextFormatting.RED, (FlatLightsCommonConfig.healthDamagePercent.get() * 100) + "%") + " of mob's max HP. (Cap of " + MiscHelpers.coloredText(TextFormatting.RED, FlatLightsCommonConfig.healthDamageCap.get() + "") + " damage.)";
            ITextComponent percentDmgTooltip = ITextComponent.getTextComponentOrEmpty(percentDmg);
            tooltip.add(percentDmgTooltip);
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /*@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack blade = playerIn.getHeldItem(handIn);

        if(uuidCheck(playerIn.getUniqueID())) {

        }

        return ActionResult.resultPass(blade);
    }*/

}
