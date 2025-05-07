package com.uberhelixx.flatlights.common.item.curio.shore;

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

public class ShoreSphere extends BaseCurio {
    public ShoreSphere() {
        super();
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(pStack.getTag() != null && !pStack.getTag().isEmpty()) {
            if(!Screen.hasShiftDown()) {
                TooltipHelper.potionAttribute("Water Breathing", pTooltipComponents);
            }
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag stackTags = stack.getTag();
        
        //doesn't let you roll again if it already has the roll data
        if(!CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(pPlayer, pUsedHand, CurioSetNames.SHORE, null, null);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player player = CurioUtils.getPlayer(slotContext);
        //give potion effects again only if the player doesn't already have it applied, so we aren't spam reapplying
        if (player != null && !hasWaterBreathing(player)) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }
        super.curioTick(slotContext, stack);
    }
    
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = CurioUtils.getPlayer(slotContext);
        if (player != null) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }
        super.onEquip(slotContext, prevStack, stack);
    }
    
    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = CurioUtils.getPlayer(slotContext);
        if(player != null && hasWaterBreathing(player)) {
            player.removeEffect(MobEffects.WATER_BREATHING);
        }
        super.onUnequip(slotContext, newStack, stack);
    }
    
    /**
     * Determines if the wearer has the water breathing potion effect
     * @param playerIn The player whose potion effects are being checked
     * @return True if the player does NOT have water breathing, false if water breathing
     */
    private static boolean hasWaterBreathing(Player playerIn) {
        return !Objects.equals(playerIn.getEffect(MobEffects.WATER_BREATHING), null);
    }
    
    //uuids for the different attribute modifiers
    protected static final UUID SPHERE_DODGE = new UUID(8349240592831593L, 1748593285403285L);
    
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
            double dodgeBase = 8;
            
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
            newMap.put(ModAttributes.DODGE_CHANCE.get(), new AttributeModifier(SPHERE_DODGE, "Sphere Dodge Modifier",(dodgeBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            
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
