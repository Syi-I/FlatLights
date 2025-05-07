package com.uberhelixx.flatlights.common.item.curio.sun;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioTier;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import com.uberhelixx.flatlights.startup.registry.ModAttributes;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class SunSphere extends BaseCurio {
    public SunSphere() {
        super();
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(pStack.getTag() != null && !pStack.getTag().isEmpty()) {
            if(!Screen.hasShiftDown()) {
                TooltipHelper.potionAttribute("Fire Resistance", pTooltipComponents);
                TooltipHelper.potionAttribute("Night Vision", pTooltipComponents);
            }
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag stackTags = stack.getTag();
        
        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(pPlayer, pUsedHand, CurioSetNames.SUN, null, null);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = CurioUtils.getPlayer(slotContext);
        //give potion effects again only if the player doesn't already have it applied, so we aren't spam reapplying
        if (player != null && !hasFireResistance(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
        }
        if (player != null && !hasNightVision(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
        super.curioTick(slotContext, stack);
    }
    
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = CurioUtils.getPlayer(slotContext);
        if (player != null) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, true, false));
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false));
        }
        super.onEquip(slotContext, prevStack, stack);
    }
    
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = CurioUtils.getPlayer(slotContext);
        if(player != null && hasFireResistance(player)) {
            player.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
        if(hasNightVision(player)) {
            player.removeEffect(MobEffects.NIGHT_VISION);
        }
        super.onUnequip(slotContext, newStack, stack);
    }
    
    /**
     * Determines if the wearer has the fire resistance potion effect
     * @param playerIn The player whose potion effects are being checked
     * @return TRUE if the player does NOT have fire resistance, FALSE if fire resistance
     */
    private static boolean hasFireResistance(Player playerIn) {
        return !Objects.equals(playerIn.getEffect(MobEffects.FIRE_RESISTANCE), null);
    }
    
    /**
     * Determines if the wearer has the night vision potion effect
     * @param playerIn The player whose potion effects are being checked
     * @return TRUE if the player does NOT have night vision, FALSE if night vision
     */
    private static boolean hasNightVision(Player playerIn) {
        return !Objects.equals(playerIn.getEffect(MobEffects.NIGHT_VISION), null);
    }
    
    //uuids for the different attribute modifiers
    protected static final UUID SPHERE_HEAL = new UUID(9248683264853021L, 3921758493409821L);
    
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
                Player player = CurioUtils.getPlayer(slotContext);
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
