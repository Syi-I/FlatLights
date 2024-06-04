package com.uberhelixx.flatlights.item.curio.sun;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.item.curio.BaseCurio;
import com.uberhelixx.flatlights.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.item.curio.CurioTier;
import com.uberhelixx.flatlights.item.curio.CurioUtils;
import com.uberhelixx.flatlights.network.PacketEntangledUpdate;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketRisingHeatUpdate;
import com.uberhelixx.flatlights.util.MiscHelpers;
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
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.uberhelixx.flatlights.capability.RisingHeatStateProvider.getHeatedState;

public class SunCube extends BaseCurio {
    public SunCube(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT stackTags = stack.getTag();

        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(playerIn, handIn, worldIn, CurioSetNames.SUN, null, null);
            //add in the set effect toggle for curios that have the functionality for the set effect (cubes only)
            CurioUtils.addSetToggle(stack);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    //uuids for the different attribute modifiers
    protected static final UUID CUBE_ARMOR = UUID.fromString("edb8f5f5-e2de-44a0-a0b2-f42a08294f1b");
    protected static final UUID CUBE_HEALTH = UUID.fromString("2fb3dec2-991e-4f7d-93cc-d480075b16f0");
    
    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            CompoundNBT tag = stack.getTag();
            if(tag != null && !tag.isEmpty()) {
                //make sure that the worn set effect matches this curio set and the set effect is toggled on
                if(CurioUtils.correctSetEffect(player, CurioSetNames.SUN) && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    int growthProgress = CurioUtils.getGrowthTracker(stack);
                    //bare minimum radius of effect, either from config file or 8 if config value is incorrect somehow
                    double baseRadius = FlatLightsCommonConfig.sunSetRadius.get() > 0 ? FlatLightsCommonConfig.sunSetRadius.get() : 8;
                    //max radius of effect, cannot be smaller than the base radius
                    double maxRadius = FlatLightsCommonConfig.sunSetRadiusMax.get() >= baseRadius ? FlatLightsCommonConfig.sunSetRadiusMax.get() : 32;
                    //radius of the effect
                    double expansionRadius = MathHelper.clamp(growthProgress + baseRadius, baseRadius, maxRadius);
                    //get all entities around the wearer
                    List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(expansionRadius + 1));
                    for(Entity entity : entities) {
                        //ensure living entity is the only thing we're adding the capability to
                        if(entity instanceof LivingEntity) {
                            LivingEntity le = (LivingEntity) entity;
                            float distance = player.getDistance(entity);
                            //set mob to heat state true if distance is within the radius
                            if(getHeatedState(le).isPresent() && distance < expansionRadius && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                                getHeatedState(le).ifPresent(heatedState -> {
                                    if(!heatedState.isHeated()) {
                                        heatedState.setHeatState(true);
                                        MiscHelpers.debugLogger("[sun set effect] changed heat state to true");
                                        if(!le.getEntityWorld().isRemote()) {
                                            Supplier<Entity> supplier = () -> le;
                                            PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketRisingHeatUpdate(le.getEntityId(), true));
                                        }
                                    }
                                });
                            }
                            //if distance is farther than the radius, set heat state to false
                            //causes issues if more than one person is using the same set, but with it toggled on vs off, causing flickering back and forth between states
                            if(getHeatedState(le).isPresent() && (distance > expansionRadius && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) || !tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                                getHeatedState(le).ifPresent(heatedState -> {
                                    if(heatedState.isHeated()) {
                                        heatedState.setHeatState(false);
                                        MiscHelpers.debugLogger("[sun set effect] changed heat state to false");
                                        if(!le.getEntityWorld().isRemote()) {
                                            Supplier<Entity> supplier = () -> le;
                                            PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketRisingHeatUpdate(le.getEntityId(), false));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
        super.curioTick(identifier, index, livingEntity, stack);
    }
    
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
            double healthBase = 8;

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
}
