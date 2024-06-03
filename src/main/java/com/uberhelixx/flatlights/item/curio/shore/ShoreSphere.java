package com.uberhelixx.flatlights.item.curio.shore;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.entity.ModAttributes;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.armor.PrismaticHelm;
import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioTier;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Objects;
import java.util.UUID;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.PLAYER_CORETRACKER_TAG;
import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class ShoreSphere extends BaseCurio {
    public ShoreSphere(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT stackTags = stack.getTag();

        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.SHORE, null, null);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
        if (player != null) {
            player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, Integer.MAX_VALUE, 0, true, false));
        }
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
        if(player != null && hasWaterBreathing(player)) {
            player.removePotionEffect(Effects.WATER_BREATHING);
        }
        super.onUnequip(slotContext, newStack, stack);
    }

    /**
     * Determines if the wearer has the water breathing potion effect
     * @param playerIn The player whose potion effects are being checked
     * @return True if the player does NOT have water breathing, false if water breathing
     */
    private static boolean hasWaterBreathing(PlayerEntity playerIn) {
        return !Objects.equals(playerIn.getActivePotionEffect(Effects.WATER_BREATHING), null);
    }

    //uuids for the different attribute modifiers
    protected static final UUID SPHERE_DODGE = UUID.fromString("1551a215-c483-4afd-aa7a-ffe01e4859ad");
    protected static final UUID SPHERE_LOOT_ROLLS = UUID.fromString("6e9f8a60-1a8a-4cb8-b963-53473d67990a");
    protected static final UUID SPHERE_LOOT_CHANCE = UUID.fromString("996d721e-075a-4bcf-b3d0-856ce0110e27");

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
            double rollAmountBase = 2;
            double rollChanceBase = 10;

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
            newMap.put(ModAttributes.DODGE_CHANCE.get(), new AttributeModifier(SPHERE_DODGE, "Sphere Dodge Modifier",(dodgeBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(ModAttributes.LOOT_ROLL_AMOUNT.get(), new AttributeModifier(SPHERE_LOOT_ROLLS, "Sphere Roll Amount Modifier",(rollAmountBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(ModAttributes.LOOT_ROLL_CHANCE.get(), new AttributeModifier(SPHERE_LOOT_CHANCE, "Sphere Roll Chance Modifier",(rollChanceBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));

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
