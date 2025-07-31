package com.uberhelixx.flatlights.common.item.curio.sun;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioTier;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketRisingHeatUpdate;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.uberhelixx.flatlights.common.capability.ModCapabilities.getHeatedState;

public class SunCube extends BaseCurio {
    public SunCube() {
        super();
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag stackTags = stack.getTag();
        
        //doesn't let you roll again if it already has the roll data
        if(!CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(pPlayer, pUsedHand, CurioSetNames.SUN, null, null);
            //add in the set effect toggle for curios that have the functionality for the set effect (cubes only)
            CurioUtils.addSetToggle(stack);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if(slotContext.entity() instanceof Player player) {
            CompoundTag tag = stack.getTag();
            if(tag != null && !tag.isEmpty()) {
                //make sure that the worn set effect matches this curio set and the set effect is toggled on
                if(CurioUtils.correctSetEffect(player, CurioSetNames.SUN) && tag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                    int growthProgress = CurioUtils.getGrowthTracker(stack);
                    //bare minimum radius of effect, either from config file or 8 if config value is incorrect somehow
                    double baseRadius = FlatLightsCommonConfig.sunSetRadius.get() > 0 ? FlatLightsCommonConfig.sunSetRadius.get() : 8;
                    //max radius of effect, cannot be smaller than the base radius
                    double maxRadius = FlatLightsCommonConfig.sunSetRadiusMax.get() >= baseRadius ? FlatLightsCommonConfig.sunSetRadiusMax.get() : 32;
                    //radius of the effect
                    double expansionRadius = Mth.clamp(growthProgress + baseRadius, baseRadius, maxRadius);
                    //get all entities around the wearer
                    List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(expansionRadius + 1));
                    
                    //main function for applying the SUN set effect
                    for(Entity entity : entities) {
                        //ensure living entity is the only thing we're adding the capability to
                        if(entity instanceof LivingEntity) {
                            LivingEntity le = (LivingEntity) entity;
                            float distance = player.distanceTo(le);
                            boolean otherEffectUsers = false;
                            
                            //get surrounding players for this specific entity, to check if anyone else could influence the RisingHeatState
                            List<Entity> surroundingPlayers = player.level().getEntities(player, le.getBoundingBox().inflate(maxRadius + 1));
                            //leaves only players in the list of surrounding entities
                            surroundingPlayers.removeIf(nextEntity -> !(nextEntity instanceof Player));
                            //getting any nearby players who could also be triggering the SUN set effect
                            List<Player> activeSunEffectPlayers = new ArrayList<>();
                            for(Entity nextEntity : surroundingPlayers) {
                                //make sure we aren't checking the actual wearer or the entity we are checking the surroundings of over and over
                                if(!nextEntity.equals(player) && !nextEntity.equals(le)) {
                                    //have to get the players' cube to check if they have the SUN set and if the effect is toggled on
                                    Player playerToCheck = (Player) nextEntity;
                                    ItemStack cubeCurio = CurioUtils.getCurioFromSlot(playerToCheck, CurioUtils.CUBE_SLOT_ID);
                                    if(cubeCurio != null) {
                                        CompoundTag checkPlayerTag = cubeCurio.hasTag() ? cubeCurio.getTag() : null;
                                        //check if the other player(s) in the radius can trigger the SUN set's effect
                                        if (checkPlayerTag != null && CurioUtils.correctSetEffect(playerToCheck, CurioSetNames.SUN) && checkPlayerTag.contains(CurioUtils.SET_EFFECT_TOGGLE)) {
                                            //if player being checked has the SUN effect toggled on, add to the list of players
                                            if (checkPlayerTag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                                                activeSunEffectPlayers.add(playerToCheck);
                                            }
                                        }
                                    }
                                }
                            }
                            
                            //check only if there are players with the sun effect active
                            if(!activeSunEffectPlayers.isEmpty()) {
                                //go through each of the players with the active effect, check if the radius can overlap or not
                                for(Player nextPlayer : activeSunEffectPlayers) {
                                    ItemStack cubeCurio = CurioUtils.getCurioFromSlot(nextPlayer, CurioUtils.CUBE_SLOT_ID);
                                    if (cubeCurio != null) {
                                        int nextPlayerGrowthTracker = CurioUtils.getGrowthTracker(cubeCurio);
                                        //there is another player whose active effect radius overlaps with the entity, so don't set state to false
                                        //larger radius from this player than the wearer guarantees to overlap the areas
                                        if (nextPlayerGrowthTracker > growthProgress) {
                                            otherEffectUsers = true;
                                        } else {
                                            float nextPlayerDistance = nextPlayer.distanceTo(le);
                                            //radius of the effect
                                            double nextPlayerExpansionRadius = Mth.clamp(nextPlayerGrowthTracker + baseRadius, baseRadius, maxRadius);
                                            //same distance check for the other players in the wearer's radius, if their AOE is smaller than the wearers (doesn't guarantee overlap)
                                            if (nextPlayerDistance < nextPlayerExpansionRadius) {
                                                otherEffectUsers = true;
                                            }
                                        }
                                    }
                                }
                            }
                            
                            //set mob to heat state true if distance is within the radius
                            if(getHeatedState(le).isPresent() && distance < expansionRadius && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                                getHeatedState(le).ifPresent(heatedState -> {
                                    if(!heatedState.isHeated()) {
                                        heatedState.setHeatState(true);
                                        MiscUtils.infoLog("[sun set effect] changed heat state to true");
                                        if(!le.level().isClientSide()) {
                                            Supplier<Entity> supplier = () -> le;
                                            PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketRisingHeatUpdate(le.getId(), true));
                                        }
                                    }
                                });
                            }
                            //if distance is farther than the radius, set heat state to false
                            //causes issues if more than one person is using the same set, but with it toggled on vs off, causing flickering back and forth between states
                            if(getHeatedState(le).isPresent() && (((distance > expansionRadius && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) || !tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE))) && !otherEffectUsers) {
                                getHeatedState(le).ifPresent(heatedState -> {
                                    if(heatedState.isHeated()) {
                                        heatedState.setHeatState(false);
                                        MiscUtils.infoLog("[sun set effect] changed heat state to false");
                                        if(!le.level().isClientSide()) {
                                            Supplier<Entity> supplier = () -> le;
                                            PacketHandler.sendToDistributor(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(supplier), new PacketRisingHeatUpdate(le.getId(), false));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
        super.curioTick(slotContext, stack);
    }
    
    //uuids for the different attribute modifiers
    protected static final UUID CUBE_ARMOR  = new UUID(9357120593765298L, 4859356201958437L);
    protected static final UUID CUBE_HEALTH = new UUID(5783937593494583L, 1848239348948943L);
    
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
