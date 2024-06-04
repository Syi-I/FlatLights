package com.uberhelixx.flatlights.item.curio.shore;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioTier;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ShoreCube extends BaseCurio {
    public ShoreCube(Properties properties) {
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
            //add in the set effect toggle for curios that have the functionality for the set effect (cubes only)
            CurioUtils.addSetToggle(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            CompoundNBT tag = stack.getTag();
            if(tag != null && !tag.isEmpty()) {
                //make sure that the worn set effect matches this curio set and the set effect is toggled on
                if(CurioUtils.correctSetEffect(player, CurioSetNames.SHORE) && tag.contains(CurioUtils.SET_EFFECT_TOGGLE) && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                    int growthProgress = CurioUtils.getGrowthTracker(stack);
                    //bare minimum radius of effect, either from config file or 16 if config value is incorrect somehow
                    double baseRadius = FlatLightsCommonConfig.shoreSetRadius.get() > 0 ? FlatLightsCommonConfig.shoreSetRadius.get() : 16;
                    //max radius of effect, cannot be smaller than the base radius
                    double maxRadius = FlatLightsCommonConfig.shoreSetRadiusMax.get() >= baseRadius ? FlatLightsCommonConfig.shoreSetRadiusMax.get() : 32;
                    //radius of the effect
                    double expansionRadius = MathHelper.clamp(growthProgress + baseRadius, baseRadius, maxRadius);
                    //get all entities around the wearer
                    List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(expansionRadius));
                    for(Entity entity : entities) {
                        //ensure living entity is the only thing we're adding slowness to
                        if(entity instanceof LivingEntity) {
                            if(entity.isInWater() || player.getEntityWorld().isRaining()) {
                                float distance = player.getDistance(entity);
                                //calculates how close the entity is to the wearer as a percentage
                                float percentMod = 1 - (float) (distance / expansionRadius);
                                //see how close the entity is to the wearer, scale slowness potency off that
                                if(percentMod >= 0.75) {
                                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600, 3, false, true));
                                }
                                else if(percentMod >= 0.5) {
                                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600, 2, false, true));
                                }
                                else if(percentMod >= 0.25) {
                                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600, 1, false, true));
                                }
                                else {
                                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.SLOWNESS, 600, 0, false, true));
                                }
                            }
                        }
                    }
                }
            }
        }
        super.curioTick(identifier, index, livingEntity, stack);
    }

    //uuids for the different attribute modifiers
    protected static final UUID CUBE_ARMOR = UUID.fromString("ac08573b-be84-4cbd-ad80-f76f324b6b06");
    protected static final UUID CUBE_TOUGHNESS = UUID.fromString("c518f19a-4ae4-41ad-af84-fbaa438e0e47");
    protected static final UUID CUBE_SWIM = UUID.fromString("ebd44c13-34a7-4f69-a1d1-c25ff5423f5b");

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
            double armorBase = 2;
            double toughnessBase = 6;
            double swimBase = 1.5;

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
            newMap.put(Attributes.ARMOR, new AttributeModifier(CUBE_ARMOR, "Cube Armor Modifier", (armorBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(CUBE_TOUGHNESS, "Cube Toughness Modifier", (toughnessBase * basePower) + growthModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(ForgeMod.SWIM_SPEED.get(), new AttributeModifier(CUBE_SWIM, "Cube Swim Modifier", (swimBase * basePower) + (growthModifier / 4), AttributeModifier.Operation.ADDITION));

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
