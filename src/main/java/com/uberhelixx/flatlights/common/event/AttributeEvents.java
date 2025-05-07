package com.uberhelixx.flatlights.common.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.startup.registry.ModAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FlatLights.MODID)
public class AttributeEvents {
    /**
     * Checks to see if the input player has the input attribute, and if it's above the base value of 0
     * @param playerIn The player whose attributes are being checked
     * @param attributeIn The attribute that the player is being checked for
     * @return TRUE if the player has the attribute and it is modified in some way, FALSE if no attribute or the attribute isn't modified from the base value
     */
    private static boolean checkAttribute(Player playerIn, Attribute attributeIn) {
        return playerIn.getAttribute(attributeIn) != null && playerIn.getAttribute(attributeIn).getValue() > 0;
    }
    
    @SubscribeEvent
    public static void dodgeChance(LivingHurtEvent event) {
        //make sure the thing getting damaged is a player, because nothing else will have dodge chance sources
        if(event.getEntity() instanceof Player player) {
            double dodge = 0;
            if(checkAttribute(player, ModAttributes.DODGE_CHANCE.get())) {
                //dodge chance is stored as a value from 0-100, have to divide by 100 to compare with .nextDouble() values
                dodge = player.getAttribute(ModAttributes.DODGE_CHANCE.get()).getValue() / 100;
            }
            //checks against player's dodge chance, and cancels if the next value rolls under the chance
            if(player.level().getRandom().nextDouble() <= dodge && dodge > 0) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void xpBoost(PlayerXpEvent.XpChange event) {
        int baseXp = event.getAmount();
        Player player = event.getEntity();
        double xpBoost = 0;
        //check if the player has xp boost or not, then do stuff if they have it
        if(checkAttribute(player, ModAttributes.XP_BOOST.get())) {
            //this should only be changed by small, whole values since xp is lots of tiny ints
            xpBoost = player.getAttribute(ModAttributes.XP_BOOST.get()).getValue();
            //add boosted xp to the base xp
            int boostedXp = baseXp + Math.round((float) xpBoost);
            event.setAmount(boostedXp);
        }
    }
    
    //I don't know if the above XP event leads to any sort of infinite xp exploit using some xp storage device so this is a backup attempt if needed
    public static void xpOrbBoost(PlayerXpEvent.PickupXp event) {
        event.setCanceled(true);
        double x = event.getOrb().getX();
        double y = event.getOrb().getY();
        double z = event.getOrb().getZ();
        Level world = event.getOrb().level();
        Player player = event.getEntity();
        double xpBoost = 0;
        //check if the player has xp boost or not, then do stuff if they have it
        if(checkAttribute(player, ModAttributes.XP_BOOST.get())) {
            //this should only be changed by small, whole values since xp is lots of tiny ints
            xpBoost = player.getAttribute(ModAttributes.XP_BOOST.get()).getValue();
            //add boosted xp to the base xp and create a new xp orb
            int addedXp = (int) Math.round(xpBoost) + event.getOrb().getValue();
            ExperienceOrb newOrb = new ExperienceOrb(world, x, y, z, addedXp);
            //remove the old xp orb and summon in the new orb instead
            event.getOrb().remove(Entity.RemovalReason.DISCARDED);
            world.addFreshEntity(newOrb);
        }
    }
    
    @SubscribeEvent
    public static void healingBoost(LivingHealEvent event) {
        float baseHeal = event.getAmount();
        double healBoost = 0;
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
        if(player != null) {
            //do heal boost if the player has attribute
            if(checkAttribute(player, ModAttributes.HEALING_BOOST.get())) {
                healBoost = player.getAttribute(ModAttributes.HEALING_BOOST.get()).getValue();
                //just adds more to the healing amount
                event.setAmount((float) (baseHeal + healBoost));
            }
        }
    }
    
    private static final String ITEM_MARKER = FlatLights.MODID + ".marker";
    
    @SubscribeEvent
    public static void markBaseLoot(LivingDeathEvent event) {
        //make sure we aren't duping player inventories
        if(event.getEntity() instanceof Player) {
            return;
        }
        //prevents duping armor and tools from a mob so you can't drop something for a mob to hold and dupe it
        event.getEntity().getAllSlots().forEach(itemStack -> {
            if(!itemStack.isEmpty()) {
                itemStack.getOrCreateTag().putBoolean(ITEM_MARKER, true);
            }
        });
    }
    
    @SubscribeEvent
    public static void lootRollIncrease(LivingDropsEvent event) {
        //make sure we aren't duping player inventories
        if(event.getEntity() instanceof Player) {
            return;
        }
        LivingEntity mob = event.getEntity();
        //check if it's a player kill or not, and if there are any item drops in the first place
        if(event.getSource().getEntity() instanceof Player player && !event.getDrops().isEmpty()) {
            //check for increased loot roll chance
            if(checkAttribute(player, ModAttributes.LOOT_ROLL_AMOUNT.get()) && checkAttribute(player, ModAttributes.LOOT_ROLL_CHANCE.get())) {
                //roll amount and roll chance from player attributes, clamps from config options
                int bonusLootRolls = Mth.clamp(Math.round((float)player.getAttribute(ModAttributes.LOOT_ROLL_AMOUNT.get()).getValue()), 0, FlatLightsCommonConfig.maxLootRolls.get());
                double bonusRollChance = Mth.clamp(player.getAttribute(ModAttributes.LOOT_ROLL_CHANCE.get()).getValue() / 100, 0, FlatLightsCommonConfig.maxLootRollChance.get() / 100);
                //check for roll chance first
                if(player.level().getRandom().nextDouble() <= bonusRollChance) {
                    player.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 3.0F, (2.0f + (player.level().getRandom().nextFloat() * 0.3f)) * 0.99f);
                    ((ServerLevel) player.level()).sendParticles(ParticleTypes.EXPLOSION, mob.getX(), mob.getY(), mob.getZ(), 3, 0.0D, 0.0D, 0.0D, 0);
                    
                    //get list of item drops from mob
                    List<ItemEntity> newDrops = new ArrayList<>(event.getDrops());
                    for(ItemEntity item : newDrops) {
                        //do not duplicate the marked items aka tools and armor
                        if(item.getItem().hasTag() && item.getItem().getTag().contains(ITEM_MARKER)) {
                            continue;
                        }
                        //add bonus items to drop list
                        for(int i = 0; i < bonusLootRolls; i++) {
                            event.getDrops().add(new ItemEntity(player.level(), item.getX(), item.getY(), item.getZ(), item.getItem().copy()));
                        }
                    }
                    //put item drops into the world
                    double mv1 = 0.1;
                    double mvxz = 0.3;
                    for(ItemEntity item : event.getDrops()) {
                        if(!item.getItem().getItem().isDamageable(item.getItem())) {
                            item.setPos(mob.getX(), mob.getY(), mob.getZ());
                            double rando = mob.level().getRandom().nextDouble();
                            item.setDeltaMovement(-mv1 + rando * mvxz, mv1 + rando * 0.4, -mv1 + rando * mvxz);
                        }
                    }
                }
            }
        }
        //remove the marker tag from all the marked items
        event.getDrops().stream().forEach(entry -> {
            ItemStack stack = entry.getItem();
            if(stack.hasTag()) {
                assert stack.getTag() != null;
                if (stack.getTag().contains(ITEM_MARKER)) {
                    stack.getTag().remove(ITEM_MARKER);
                    if (stack.getTag().isEmpty()) {
                        stack.setTag(null);
                    }
                }
            }
            entry.setItem(stack);
        });
    }

}
