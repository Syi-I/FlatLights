package com.uberhelixx.flatlights.common.item.curio.shore;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioTier;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ShoreCube extends BaseCurio {
    public ShoreCube() {
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
            //add in the set effect toggle for curios that have the functionality for the set effect (cubes only)
            CurioUtils.addSetToggle(stack);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity livingEntity = slotContext.entity();
        if(livingEntity instanceof Player player) {
            CompoundTag tag = stack.getTag();
            if(tag != null && !tag.isEmpty()) {
                //make sure that the worn set effect matches this curio set and the set effect is toggled on
                if(CurioUtils.correctSetEffect(player, CurioSetNames.SHORE) && tag.contains(CurioUtils.SET_EFFECT_TOGGLE) && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                    int growthProgress = CurioUtils.getGrowthTracker(stack);
                    //bare minimum radius of effect, either from config file or 16 if config value is incorrect somehow
                    double baseRadius = FlatLightsCommonConfig.shoreSetRadius.get() > 0 ? FlatLightsCommonConfig.shoreSetRadius.get() : 16;
                    //max radius of effect, cannot be smaller than the base radius
                    double maxRadius = FlatLightsCommonConfig.shoreSetRadiusMax.get() >= baseRadius ? FlatLightsCommonConfig.shoreSetRadiusMax.get() : 32;
                    //radius of the effect
                    double expansionRadius = Mth.clamp(growthProgress + baseRadius, baseRadius, maxRadius);
                    //get all entities around the wearer
                    List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(expansionRadius));
                    for(Entity entity : entities) {
                        //ensure living entity is the only thing we're adding slowness to
                        if(entity instanceof LivingEntity) {
                            if(entity.isInWater() || player.level().isRaining()) {
                                MiscUtils.infoLog("[Shore Cube] trying to slow nearby entities");
                                float distance = player.distanceTo(entity);
                                //calculates how close the entity is to the wearer as a percentage
                                float percentMod = 1 - (float) (distance / expansionRadius);
                                //see how close the entity is to the wearer, scale slowness potency off that
                                if(percentMod >= 0.75) {
                                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 3, false, true));
                                }
                                else if(percentMod >= 0.5) {
                                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 2, false, true));
                                }
                                else if(percentMod >= 0.25) {
                                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 1, false, true));
                                }
                                else {
                                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 600, 0, false, true));
                                }
                            }
                        }
                    }
                }
            }
        }
        super.curioTick(slotContext, stack);
    }
    
    //uuids for the different attribute modifiers
    protected static final UUID CUBE_ARMOR = new UUID(3749575636224749L, 1958534741473475L);
    protected static final UUID CUBE_TOUGHNESS = new UUID(5841692614492857L, 5830972419212842L);
    protected static final UUID CUBE_SWIM = new UUID(2039453239714276L, 9371506232647184L);

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
            double armorBase = 4;
            double toughnessBase = 6;
            double swimBase = 1.5;
            
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
