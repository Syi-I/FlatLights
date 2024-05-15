package com.uberhelixx.flatlights.item.curio.dragonsfinal;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioTier;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import javax.swing.*;
import java.util.List;
import java.util.UUID;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.PLAYER_CORETRACKER_TAG;
import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class DragonsFinalCube extends BaseCurio {
    public DragonsFinalCube(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT stackTags = stack.getTag();

        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            if(MiscHelpers.uuidCheck(playerIn.getUniqueID())) {
                CurioUtils.setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.DRAGONSFINAL, CurioTier.GROWTH.MODEL_VALUE, Integer.MAX_VALUE);
            }
            else {
                CurioUtils.setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.DRAGONSFINAL, null, null);
            }
            //add in the set effect toggle for curios that have the functionality for the set effect (cubes only)
            CurioUtils.addSetToggle(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    //there's already a tooltip formatting in BaseCurio but this overrides that since Dragon's Final is the test set
    //normally won't have to do this with other curio sets
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //info tooltip
        if(stack.getTag() != null && !stack.getTag().isEmpty()) {
            if(!Screen.hasShiftDown()) {
                tooltip.add(CurioUtils.getSetTooltip(stack));
                if (worldIn != null && worldIn.isRemote()) {
                    tooltip.add(CurioUtils.getSetEffectTooltip(stack));
                }
                tooltip.add(CurioUtils.getTierTooltip(stack));
                if (stack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                    if (stack.getTag().getInt(CurioUtils.GROWTH_CAP) == Integer.MAX_VALUE) {
                        tooltip.add(CurioUtils.getGrowthTooltip(stack, false));
                    } else {
                        tooltip.add(CurioUtils.getGrowthTooltip(stack, true));
                    }
                }
            }
            else {
                if (worldIn != null && worldIn.isRemote()) {
                    tooltip.add(CurioUtils.getSetEffectTooltip(stack));
                }
                tooltip.add(CurioUtils.getSetDescriptionTooltip(stack));
            }
        }
        //how to use curio
        else {
            ITextComponent useTooltip = TextHelpers.genericBrackets("Right-click to roll.", TextFormatting.GRAY);
            tooltip.add(useTooltip);
        }
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            CompoundNBT tag = stack.getTag();
            if(tag != null && !tag.isEmpty()) {
                //make sure that the worn set effect matches this curio set and the set effect is toggled on
                if(CurioUtils.correctSetEffect(player, CurioSetNames.DRAGONSFINAL) && tag.contains(CurioUtils.SET_EFFECT_TOGGLE) && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                    int growthProgress = CurioUtils.getGrowthTracker(stack);
                    //bare minimum radius of effect, either from config file or 6 if config is an incorrect value somehow
                    double baseRadius = FlatLightsCommonConfig.dragonsfinalSetRadius.get() > 0 ? FlatLightsCommonConfig.dragonsfinalSetRadius.get() : 6;
                    //max radius of effect, makes sure the max radius is not smaller than the base radius
                    double maxRadius = FlatLightsCommonConfig.dragonsfinalSetRadiusMax.get() >= baseRadius ? FlatLightsCommonConfig.dragonsfinalSetRadiusMax.get() : 32;
                    //radius of the effect
                    double expansionRadius = MathHelper.clamp(growthProgress + baseRadius, baseRadius, maxRadius);
                    //get all entities around the wearer
                    List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(expansionRadius));
                    for(Entity entity : entities) {
                        //ensure living entity is the only thing we're trying to damage
                        if(entity instanceof LivingEntity) {
                            float distance = player.getDistance(entity);
                            //calculates how close the entity is to the wearer as a percentage
                            float percentMod = (float) (distance / expansionRadius);
                            //damage scales off hp difference and the proximity percentage
                            float dmg = Math.max(player.getMaxHealth() - ((LivingEntity) entity).getMaxHealth(), 0) * percentMod;
                            if(dmg > 0) {
                                entity.attackEntityFrom(ModDamageTypes.causeIndirectEntangled(player, player), dmg);
                            }
                        }
                    }
                }
            }
        }
        super.curioTick(identifier, index, livingEntity, stack);
    }

    //uuids for the different attribute modifiers
    protected static final UUID CUBE_ARMOR = UUID.fromString("65e1dd35-4826-41fa-a9d3-3011c1a6a5a0");
    protected static final UUID CUBE_TOUGHNESS = UUID.fromString("797a8b01-1f6f-43cd-ade2-f22f5329351d");
    protected static final UUID CUBE_HEALTH = UUID.fromString("bda19a97-255e-45d8-8f85-f15d45b82b77");

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
            double basePower = tier.TIER_MULTIPLIER;
            double growthModifier = 0;
            double armorBase = 4;
            double toughnessBase = 4;
            double healthBase = 4;

            //ensure curio is growth tier for getting growth modifiers instead of flat ones
            if (tier == CurioTier.GROWTH) {
                growthModifier = 1;

                //calculate growth modifier value from core count, scale down number
                PlayerEntity player = slotContext.getWearer() instanceof PlayerEntity ? (PlayerEntity) slotContext.getWearer() : null;
                if (player != null) {
                    int coresFromPlayer = getCoresFromPlayer(player);
                    int cores = 0;
                    if (stack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                        int growthTracker = stack.getTag().getInt(CurioUtils.GROWTH_TRACKER);
                        //if the tracker is behind compared to the player tracker, update growth tracker and use player tracker value
                        if (growthTracker < coresFromPlayer) {
                            CompoundNBT tag = stack.getTag();
                            tag.putInt(CurioUtils.GROWTH_TRACKER, coresFromPlayer);
                            //you have to send packets to update the tracker data appropriately
                            PacketHandler.sendToServer(new PacketWriteNbt(tag, stack));
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
            newMap.put(Attributes.ARMOR, new AttributeModifier(CUBE_ARMOR, "Cube Armor Modifier", (armorBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(CUBE_TOUGHNESS, "Cube Toughness Modifier", (toughnessBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(Attributes.MAX_HEALTH, new AttributeModifier(CUBE_HEALTH, "Cube Health Modifier", (healthBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));

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
            return 0;
        }
        //check for player persistent nbt, if none return 0 cores
        if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            return 0;
        }
        else {
            persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }

        //if core tracker stat tag in data, return core amount from tracker
        if(persistent.contains(PLAYER_CORETRACKER_TAG)) {
            return persistent.getInt(PLAYER_CORETRACKER_TAG);
        }
        else {
            return 0;
        }
    }
}
