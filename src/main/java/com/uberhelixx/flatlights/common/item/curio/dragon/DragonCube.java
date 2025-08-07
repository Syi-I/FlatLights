package com.uberhelixx.flatlights.common.item.curio.dragon;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.item.curio.BaseCurio;
import com.uberhelixx.flatlights.common.item.curio.CurioSetNames;
import com.uberhelixx.flatlights.common.item.curio.CurioTier;
import com.uberhelixx.flatlights.common.item.curio.CurioUtils;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketWriteNbt;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

import static com.uberhelixx.flatlights.util.lib.LibTagKeys.PLAYER_CORETRACKER_TAG;

public class DragonCube extends BaseCurio {
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        //get held itemstack, which should be the input curio, and get nbt tags from it
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag stackTags = stack.getTag();
        
        //doesn't let you roll again if it already has the roll data
        if(stackTags == null || !CurioUtils.rollCheck(stackTags)) {
            CurioUtils.setCurioNbt(pPlayer, pUsedHand, CurioSetNames.DRAGON, null, null);
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
                if(CurioUtils.correctSetEffect(player, CurioSetNames.DRAGON) && tag.contains(CurioUtils.SET_EFFECT_TOGGLE) && tag.getBoolean(CurioUtils.SET_EFFECT_TOGGLE)) {
                    int growthProgress = CurioUtils.getGrowthTracker(stack);
                    //bare minimum radius of effect, either from config file or 6 if config is an incorrect value somehow
                    double baseRadius = FlatLightsCommonConfig.dragonSetRadius.get() > 0 ? FlatLightsCommonConfig.dragonSetRadius.get() : 6;
                    //max radius of effect, makes sure the max radius is not smaller than the base radius
                    double maxRadius = FlatLightsCommonConfig.dragonSetRadiusMax.get() >= baseRadius ? FlatLightsCommonConfig.dragonSetRadiusMax.get() : 32;
                    //radius of the effect
                    double expansionRadius = Mth.clamp(growthProgress + baseRadius, baseRadius, maxRadius);
                    //get all entities around the wearer
                    List<Entity> entities = player.level().getEntities(player, player.getBoundingBox().inflate(expansionRadius));
                    for(Entity entity : entities) {
                        //ensure living entity is the only thing we're trying to damage
                        if(entity instanceof LivingEntity) {
                            float distance = player.distanceTo(entity);
                            //calculates how close the entity is to the wearer as a percentage
                            float percentMod = 1 - (float) (distance / expansionRadius);
                            //damage scales off hp difference and the proximity percentage
                            float dmg = Math.max(player.getMaxHealth() - ((LivingEntity) entity).getMaxHealth(), 0) * percentMod;
                            if(dmg > 0) {
                                entity.hurt(ModDamageTypes.causeEntangledDamage(player), dmg);
                            }
                        }
                    }
                }
            }
        }
        super.curioTick(slotContext, stack);
    }
    
    //uuids for the different attribute modifiers
    protected static final UUID CUBE_ARMOR = new UUID(3497987723158374L, 9234012374839481L);
    protected static final UUID CUBE_TOUGHNESS = new UUID(1023798772315837L, 4474784559228915L);
    protected static final UUID CUBE_HEALTH = new UUID(4924895473291239L, 1923784738292572L);
    
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
            double toughnessBase = 4;
            double healthBase = 4;
            
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
        if(persistent.contains(PLAYER_CORETRACKER_TAG)) {
            return persistent.getInt(PLAYER_CORETRACKER_TAG);
        }
        else {
            return 0;
        }
    }
    
}
