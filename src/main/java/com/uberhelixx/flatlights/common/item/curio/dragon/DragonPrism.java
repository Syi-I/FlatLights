package com.uberhelixx.flatlights.common.item.curio.dragon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioTier;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class DragonPrism extends BaseCurio {
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag stackTags = stack.getTag();
        
        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            if(MiscUtils.uuidCheck(pPlayer.getUUID())) {
                CurioUtils.setCurioNbt(pPlayer, pUsedHand, CurioSetNames.DRAGON, CurioTier.getModel(CurioTier.GROWTH), Integer.MAX_VALUE);
            }
            else {
                CurioUtils.setCurioNbt(pPlayer, pUsedHand, CurioSetNames.DRAGON, null, null);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    //there's already a tooltip formatting in BaseCurio but this overrides that since dragon is the test set
    //normally won't have to do this with other curio sets
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        //info tooltip
        if(pStack.getTag() != null && !pStack.getTag().isEmpty()) {
            if(!Screen.hasShiftDown()) {
                CurioUtils.getSetTooltip(pStack, pTooltipComponents);
                if (pLevel != null && pLevel.isClientSide()) {
                    CurioUtils.getSetEffectTooltip(pStack, pTooltipComponents);
                }
                CurioUtils.getTierTooltip(pStack, pTooltipComponents);
                if (pStack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                    if (pStack.getTag().getInt(CurioUtils.GROWTH_CAP) == Integer.MAX_VALUE) {
                        CurioUtils.getGrowthTooltip(pStack, false, pTooltipComponents);
                    } else {
                        CurioUtils.getGrowthTooltip(pStack, true, pTooltipComponents);
                    }
                }
            }
            else {
                if (pLevel != null && pLevel.isClientSide()) {
                    CurioUtils.getSetEffectTooltip(pStack, pTooltipComponents);
                }
                CurioUtils.getSetDescriptionTooltip(pStack, pTooltipComponents);
            }
        }
        //how to use curio
        else {
            Style color = Style.EMPTY.withColor(ChatFormatting.GRAY);
            TooltipHelper.genericBrackets(pTooltipComponents, "Right-click to roll.", color);
        }
    }
    
    //uuids for the different attribute modifiers
    protected static final UUID PRISM_ATTACK = new UUID(3478945378634578L, 5934297823478934L);
    protected static final UUID PRISM_ATK_SPEED = new UUID(7895645902437454L, 3458723478923478L);
    
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
            double speedBase = 1;
            
            //ensure curio is growth tier for getting growth modifiers instead of flat ones
            if (tier == CurioTier.GROWTH) {
                growthModifier = 1;
                
                //calculate growth modifier value from core count, scale down number
                Player player = slotContext.entity() instanceof Player ? (Player) slotContext.entity() : null;
                if (player != null) {
                    int coresFromPlayer = getCoresFromPlayer(player);
                    int cores = 0;
                    if (stack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                        int growthTracker = stack.getTag().getInt(CurioUtils.GROWTH_TRACKER);
                        //if the tracker is behind compared to the player tracker, update growth tracker and use player tracker value
                        if (growthTracker < coresFromPlayer) {
                            CompoundTag tag = stack.getTag();
                            tag.putInt(CurioUtils.GROWTH_TRACKER, coresFromPlayer);
                            //you have to send packets to update the tracker data appropriately
                            if(player.level().isClientSide()) {
                                PacketHandler.sendToServer(new PacketWriteNbt(tag, stack));
                            }
                            cores = coresFromPlayer;
                        }
                        //this should be the normal function
                        else {
                            cores = growthTracker;
                        }
                    }
                    growthModifier = cores * 0.01;
                }
            }
            
            //put attribute modifiers onto the new map using the growth modifier value
            newMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(PRISM_ATTACK, "Prism Attack Modifier",(attackBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(Attributes.ATTACK_SPEED, new AttributeModifier(PRISM_ATK_SPEED, "Prism Attack Speed Modifier",(speedBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            
            //put attributes from old map onto new one which is being returned
            for (Attribute attribute : oldMap.keySet()) {
                newMap.putAll(attribute, oldMap.get(attribute));
            }
            //return modified attributes
            return newMap;
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
    
    public int getCoresFromPlayer(Player playerIn) {
        CompoundTag data = playerIn.getPersistentData();
        CompoundTag persistent;
        
        //check if player even has cores in the first place, return 0 cores if not
        if(!MiscUtils.uuidCheck(playerIn.getUUID())) {
            return 0;
        }
        //check for player persistent nbt, if none return 0 cores
        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            return 0;
        }
        else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }
        
        //if core tracker stat tag in data, return core amount from tracker
        if(persistent.contains(PrismaticBladeMk2.PLAYER_CORETRACKER_TAG)) {
            return persistent.getInt(PrismaticBladeMk2.PLAYER_CORETRACKER_TAG);
        }
        else {
            return 0;
        }
    }
}
