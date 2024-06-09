package com.uberhelixx.flatlights.item.curio.sun;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.entity.ModAttributes;
import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioTier;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SunSphere extends BaseCurio {
    public SunSphere(Properties properties) {
        super(properties);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if(stack.getTag() != null && !stack.getTag().isEmpty()) {
            if(!Screen.hasShiftDown()) {
                tooltip.add(TextHelpers.potionAttribute("Fire Resistance"));
                tooltip.add(TextHelpers.potionAttribute("Night Vision"));
            }
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT stackTags = stack.getTag();

        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.SUN, null, null);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
        if (player != null) {
            player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
            player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
        if(player != null && hasFireResistance(player)) {
            player.removePotionEffect(Effects.FIRE_RESISTANCE);
        }
        if(hasNightVision(player)) {
            player.removePotionEffect(Effects.NIGHT_VISION);
        }
        super.onUnequip(slotContext, newStack, stack);
    }

    /**
     * Determines if the wearer has the fire resistance potion effect
     * @param playerIn The player whose potion effects are being checked
     * @return TRUE if the player does NOT have fire resistance, FALSE if fire resistance
     */
    private static boolean hasFireResistance(PlayerEntity playerIn) {
        return !Objects.equals(playerIn.getActivePotionEffect(Effects.FIRE_RESISTANCE), null);
    }
    
    /**
     * Determines if the wearer has the night vision potion effect
     * @param playerIn The player whose potion effects are being checked
     * @return TRUE if the player does NOT have night vision, FALSE if night vision
     */
    private static boolean hasNightVision(PlayerEntity playerIn) {
        return !Objects.equals(playerIn.getActivePotionEffect(Effects.NIGHT_VISION), null);
    }

    //uuids for the different attribute modifiers
    protected static final UUID SPHERE_HEAL = UUID.fromString("6e9f8a60-1a8a-4cb8-b963-53473d67990a");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        //get old attribute modifiers and create a new map to modify
        Multimap<Attribute, AttributeModifier> oldMap = super.getAttributeModifiers(slotContext, uuid, stack);
        ListMultimap<Attribute, AttributeModifier> newMap = ArrayListMultimap.create();

        CurioTier tier = null;
        //get curio tier after ensuring there is nbt data rolled for tier value
        if(stack.getTag() != null && stack.getTag().contains(CurioUtils.TIER)) {
            tier = CurioUtils.getCurioTier(stack);
        }

        if(tier != null) {
            double basePower = CurioUtils.getTierMultiplier(stack);
            double growthModifier = 0;
            double healBase = 2;

            //ensure curio is growth tier for getting growth modifiers instead of flat ones
            if (tier == CurioTier.GROWTH) {
                growthModifier = 1;

                //calculate growth modifier value from core count, scale down number
                PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
                if (player != null) {
                    int cores = 0;
                    if (stack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                        cores = stack.getTag().getInt(CurioUtils.GROWTH_TRACKER);
                    }
                    growthModifier = cores * 0.01;
                }
            }
            //put attribute modifiers onto the new map using the growth modifier value
            newMap.put(ModAttributes.HEALING_BOOST.get(), new AttributeModifier(SPHERE_HEAL, "Sphere Healing Modifier",(healBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            
            //put attributes from old map onto new one which is being returned
            for (Attribute attribute : oldMap.keySet()) {
                newMap.putAll(attribute, oldMap.get(attribute));
            }
            //return modified attributes
            return newMap;
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
