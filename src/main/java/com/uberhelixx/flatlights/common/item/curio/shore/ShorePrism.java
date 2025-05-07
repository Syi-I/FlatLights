package com.uberhelixx.flatlights.common.item.curio.shore;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioTier;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

public class ShorePrism extends BaseCurio {
    public ShorePrism() {
        super();
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
    
    //uuids for the different attribute modifiers
    protected static final UUID PRISM_ATTACK = new UUID(5739475192359384L, 8492059172948392L);
    protected static final UUID PRISM_REACH_B = new UUID(9274837592187482L, 7893264938749288L);
    protected static final UUID PRISM_REACH_E = new UUID(8593616598346722L, 1927465828737546L);
    
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
            double attackBase = 2;
            double reachBase = 1;
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
            newMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(PRISM_ATTACK, "Prism Attack Modifier",(attackBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(PRISM_REACH_B, "Prism Block Reach Modifier",(reachBase * basePower) + (growthModifier / 2), AttributeModifier.Operation.ADDITION));
            newMap.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(PRISM_REACH_E, "Prism Entity Reach Modifier",(reachBase * basePower) + (growthModifier / 2), AttributeModifier.Operation.ADDITION));
            
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
