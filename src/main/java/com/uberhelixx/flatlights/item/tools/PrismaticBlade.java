package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.max;
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
        if(uuidCheck(attacker.getUniqueID())) {
            target.attackEntityFrom(DamageSource.OUT_OF_WORLD, target.getMaxHealth() + 1);
            //target.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
        }
        else {
            target.addPotionEffect(new EffectInstance(Effects.GLOWING, 15));
            target.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 5, 4));
            target.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 5, 2));
            //deal either 10% of max hp as damage or 1000 damage, whichever is lower
            target.attackEntityFrom(DamageSource.GENERIC, (float) min(1000, (target.getMaxHealth() * 0.1)));
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_blade_shift"));
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    //trying to rip off kikoku time
    public static void EnchantDouble (AnvilUpdateEvent event) {
        if (!event.getPlayer().isServerWorld()) {
            return;
        }

        //get prismatic blade from left anvil slot, enchanted book from right anvil slot
        ItemStack prismaticBlade = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        //check that a prismatic blade and enchanted book are in the anvil
        if (prismaticBlade == null || prismaticBlade.getItem() != ModItems.PRISMATIC_BLADE.get() || enchantedBook == null || enchantedBook.getItem() != Items.ENCHANTED_BOOK) {
            return;
        }

        //grab current enchantments from blade and book
        Map<Enchantment, Integer> swordMap = EnchantmentHelper.getEnchantments(prismaticBlade);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);

        //check if book has enchants or not
        if (bookMap.isEmpty()) {
            return;
        }

        //output enchants for the blade
        Map<Enchantment, Integer> outputMap = new HashMap<>(swordMap);
        int costCounter = 0;

        //putting the enchantment into the output map and doubling enchants
        for (Map.Entry<Enchantment, Integer> entry : bookMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentValue = swordMap.get(entry.getKey());
            Integer addValue = entry.getValue();
            if (currentValue == null) {
                outputMap.put(entry.getKey(), addValue);
                costCounter += addValue;
            }
            else {
                int value = Math.min(currentValue + addValue, enchantment.getMaxLevel() * 65355);
                outputMap.put(entry.getKey(), value);
                costCounter += (currentValue + addValue) * 2;
            }
        }

        //output new blade with enchants
        event.setCost(costCounter);
        ItemStack enchantedBlade = prismaticBlade.copy();
        EnchantmentHelper.setEnchantments(outputMap, enchantedBlade);
        event.setOutput(enchantedBlade);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack blade = playerIn.getHeldItem(handIn);

        if(uuidCheck(playerIn.getUniqueID())) {

        }

        return ActionResult.resultPass(blade);
    }

    private boolean uuidCheck(UUID targetUuid) {
        //380df991-f603-344c-a090-369bad2a924a is dev uuid
        if(0 == targetUuid.compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"))) {
            return true;
        }
        if(0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) {
            return true;
        }
        return false;
    }
}
