package com.uberhelixx.flatlights.item.curio.dragonsfinal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.entity.ModAttributes;
import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.PLAYER_CORETRACKER_TAG;
import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class DragonsFinalSphere extends BaseCurio {
    public DragonsFinalSphere(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT stackTags = stack.getTag();

        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !rollCheck(stackTags)) {
            setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.DRAGONSFINAL, CurioTier.GROWTH.MODEL_VALUE, Integer.MAX_VALUE);
            //setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.DRAGONSFINAL, CurioTier.EPIC.MODEL_VALUE, null);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    //there's already a tooltip formatting in BaseCurio but this overrides that since Dragon's Final is the test set
    //normally won't have to do this with other curio sets
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //info tooltip
        if(stack.getTag() != null && !stack.getTag().isEmpty()) {
            tooltip.add(getSetTooltip(stack));
            tooltip.add(getTierTooltip(stack));
            if(stack.getTag().contains(GROWTH_TRACKER)) {
                tooltip.add(getGrowthTooltip(stack, false) );
            }
        }
        //how to use curio
        else {
            ITextComponent useTooltip = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.GRAY + "Right-click to use." + TextFormatting.AQUA + "]");
            tooltip.add(useTooltip);
        }
    }

    //uuids for the different attribute modifiers
    protected static final UUID SPHERE_DODGE = UUID.fromString("dc955a56-21a9-4d65-a3e0-9c79e3fce7ca");
    protected static final UUID SPHERE_LUCK = UUID.fromString("47f80971-3ca1-44c3-98ed-52404b3c7e41");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        //get old attribute modifiers and create a new map to modify
        Multimap<Attribute, AttributeModifier> oldMap = super.getAttributeModifiers(slotContext, uuid, stack);
        ListMultimap<Attribute, AttributeModifier> newMap = ArrayListMultimap.create();

        CurioTier tier = null;
        //get curio tier after ensuring there is nbt data rolled for tier value
        if(stack.getTag() != null && stack.getTag().contains(BaseCurio.TIER)) {
            tier = getCurioTier(stack);
        }

        //ensure curio is growth tier for getting growth modifiers instead of flat ones
        if(tier != null & tier == CurioTier.GROWTH) {
            double growthModifier = 1;

            //calculate growth modifier value from core count, scale down number
            PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
            if (player != null) {
                int coresFromPlayer = getCoresFromPlayer(player);
                int cores = 0;
                if(stack.getTag().contains(BaseCurio.GROWTH_TRACKER)) {
                    int growthTracker = stack.getTag().getInt(GROWTH_TRACKER);
                    //if the tracker is behind compared to the player tracker, update growth tracker and use player tracker value
                    if(growthTracker < coresFromPlayer) {
                        CompoundNBT tag = stack.getTag();
                        tag.putInt(GROWTH_TRACKER, coresFromPlayer);
                        //you have to send packets to update the tracker data appropriately
                        PacketHandler.sendToServer(new PacketWriteNbt(tag, stack));
                        cores = coresFromPlayer;
                    }
                    //this should be the normal function
                    else {
                        cores = growthTracker;
                    }
                }
                //MiscHelpers.debugLogger("[Attribute Mapping] Total Bonus: " + cores * 0.01);
                growthModifier = cores * 0.01;
            }

            //put attribute modifiers onto the new map using the growth modifier value
            newMap.put(ModAttributes.DODGE_CHANCE.get(), new AttributeModifier(SPHERE_DODGE, "Sphere Dodge Modifier", growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(Attributes.LUCK, new AttributeModifier(SPHERE_LUCK, "Sphere Luck Modifier", growthModifier, AttributeModifier.Operation.ADDITION));

            //put attributes from old map onto new one which is being returned
            for (Attribute attribute : oldMap.keySet()) {
                newMap.putAll(attribute, oldMap.get(attribute));
            }
            //return modified attributes
            return newMap;
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    public int getCoresFromPlayer(PlayerEntity playerIn) {
        CompoundNBT data = playerIn.getPersistentData();
        CompoundNBT persistent;

        //check if player even has cores in the first place, return 0 cores if not
        if(!uuidCheck(playerIn.getUniqueID())) {
            //MiscHelpers.debugLogger("[Get Player Cores] Player does not have access to cores.");
            return 0;
        }
        //check for player persistent nbt, if none return 0 cores
        if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            //MiscHelpers.debugLogger("[Get Player Cores] No persisted NBT data, returning 0 cores.");
            return 0;
        }
        else {
            persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }

        //if core tracker stat tag in data, return core amount from tracker
        if(persistent.contains(PLAYER_CORETRACKER_TAG)) {
            //MiscHelpers.debugLogger("[Get Player Cores] Found Core Tracker data, returning " + persistent.getInt(PLAYER_CORETRACKER_TAG) + " cores.");
            return persistent.getInt(PLAYER_CORETRACKER_TAG);
        }
        else {
            //MiscHelpers.debugLogger("[Get Player Cores] No Core Tracker data, returning 0 cores.");
            return 0;
        }
    }
}
