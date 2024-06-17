package com.uberhelixx.flatlights.entity;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID)
public class AttributeFunctions {
    
    /**
     * Checks to see if the input player has the input attribute, and if it's above the base value of 0
     * @param playerIn The player whose attributes are being checked
     * @param attributeIn The attribute that the player is being checked for
     * @return TRUE if the player has the attribute and it is modified in some way, FALSE if no attribute or the attribute isn't modified from the base value
     */
    private static boolean checkAttribute(PlayerEntity playerIn, Attribute attributeIn) {
        return playerIn.getAttribute(attributeIn) != null && playerIn.getAttribute(attributeIn).getValue() > 0;
    }
    
    @SubscribeEvent
    public static void dodgeChance(LivingHurtEvent event) {
        //make sure the thing getting damaged is a player, because nothing else will have dodge chance sources
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            double dodge = 0;
            if(checkAttribute(player, ModAttributes.DODGE_CHANCE.get())) {
                //dodge chance is stored as a value from 0-100, have to divide by 100 to compare with .nextDouble() values
                dodge = player.getAttribute(ModAttributes.DODGE_CHANCE.get()).getValue() / 100;
            }
            //checks against player's dodge chance, and cancels if the next value rolls under the chance
            if(player.getEntityWorld().getRandom().nextDouble() <= dodge && dodge > 0) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void xpBoost(PlayerXpEvent.XpChange event) {
        int baseXp = event.getAmount();
        PlayerEntity player = event.getPlayer();
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
        double x = event.getOrb().getPosX();
        double y = event.getOrb().getPosY();
        double z = event.getOrb().getPosZ();
        World world = event.getOrb().getEntityWorld();
        PlayerEntity player = event.getPlayer();
        double xpBoost = 0;
        //check if the player has xp boost or not, then do stuff if they have it
        if(checkAttribute(player, ModAttributes.XP_BOOST.get())) {
            //this should only be changed by small, whole values since xp is lots of tiny ints
            xpBoost = player.getAttribute(ModAttributes.XP_BOOST.get()).getValue();
            //add boosted xp to the base xp and create a new xp orb
            int addedXp = (int) Math.round(xpBoost) + event.getOrb().getXpValue();
            ExperienceOrbEntity newOrb = new ExperienceOrbEntity(world, x, y, z, addedXp);
            //remove the old xp orb and summon in the new orb instead
            event.getOrb().remove();
            world.addEntity(newOrb);
        }
    }
    
    @SubscribeEvent
    public static void healingBoost(LivingHealEvent event) {
        float baseHeal = event.getAmount();
        double healBoost = 0;
        PlayerEntity player = event.getEntityLiving() instanceof PlayerEntity ? (PlayerEntity) event.getEntityLiving() : null;
        if(player != null) {
            //do heal boost if the player has attribute
            if(checkAttribute(player, ModAttributes.HEALING_BOOST.get())) {
                healBoost = player.getAttribute(ModAttributes.HEALING_BOOST.get()).getValue();
                //just adds more to the healing amount
                event.setAmount((float) (baseHeal + healBoost));
            }
        }
    }
    
    private static final String ITEM_MARKER = FlatLights.MOD_ID + ".marker";
    
    @SubscribeEvent
    public static void markBaseLoot(LivingDeathEvent event) {
        //make sure we aren't duping player inventories
        if(event.getEntityLiving() instanceof PlayerEntity) {
            return;
        }
        //prevents duping armor and tools from a mob so you can't drop something for a mob to hold and dupe it
        event.getEntityLiving().getEquipmentAndArmor().forEach(itemStack -> {
            if(!itemStack.isEmpty()) {
                itemStack.getOrCreateTag().putBoolean(ITEM_MARKER, true);
            }
        });
    }
    
    @SubscribeEvent
    public static void lootRollIncrease(LivingDropsEvent event) {
        //make sure we aren't duping player inventories
        if(event.getEntity() instanceof PlayerEntity) {
            return;
        }
        LivingEntity mob = event.getEntityLiving();
        //check if it's a player kill or not, and if there are any item drops in the first place
        if(event.getSource().getTrueSource() instanceof PlayerEntity && !event.getDrops().isEmpty()) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            //check for increased loot roll chance
            if(checkAttribute(player, ModAttributes.LOOT_ROLL_AMOUNT.get()) && checkAttribute(player, ModAttributes.LOOT_ROLL_CHANCE.get())) {
                //roll amount and roll chance from player attributes, clamps from config options
                int bonusLootRolls = MathHelper.clamp(Math.round((float)player.getAttribute(ModAttributes.LOOT_ROLL_AMOUNT.get()).getValue()), 0, FlatLightsCommonConfig.maxLootRolls.get());
                double bonusRollChance = MathHelper.clamp(player.getAttribute(ModAttributes.LOOT_ROLL_CHANCE.get()).getValue() / 100, 0, FlatLightsCommonConfig.maxLootRollChance.get() / 100);
                //check for roll chance first
                if(player.getEntityWorld().getRandom().nextDouble() <= bonusRollChance) {
                    player.getEntityWorld().playSound(null, mob.getPosX(), mob.getPosY(), mob.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 3.0F, (2.0f + (player.getEntityWorld().getRandom().nextFloat() * 0.3f)) * 0.99f);
                    ((ServerWorld) player.getEntityWorld()).spawnParticle(ParticleTypes.EXPLOSION, mob.getPosX(), mob.getPosY(), mob.getPosZ(), 3, 0.0D, 0.0D, 0.0D, 0);
                    
                    //get list of item drops from mob
                    List<ItemEntity> newDrops = new ArrayList<>(event.getDrops());
                    for(ItemEntity item : newDrops) {
                        //do not duplicate the marked items aka tools and armor
                        if(item.getItem().hasTag() && item.getItem().getTag().contains(ITEM_MARKER)) {
                            continue;
                        }
                        //add bonus items to drop list
                        for(int i = 0; i < bonusLootRolls; i++) {
                            event.getDrops().add(new ItemEntity(player.getEntityWorld(), item.getPosX(), item.getPosY(), item.getPosZ(), item.getItem().copy()));
                        }
                    }
                    //put item drops into the world
                    double mv1 = 0.1;
                    double mvxz = 0.3;
                    for(ItemEntity item : event.getDrops()) {
                        if(!item.getItem().getItem().isDamageable()) {
                            item.setPosition(mob.getPosX(), mob.getPosY(), mob.getPosZ());
                            double rando = mob.getEntityWorld().getRandom().nextDouble();
                            item.setMotion(-mv1 + rando * mvxz, mv1 + rando * 0.4, -mv1 + rando * mvxz);
                        }
                    }
                }
            }
        }
        //remove the marker tag from all the marked items
        event.getDrops().stream().forEach(entry -> {
            ItemStack stack = entry.getItem();
            if(stack.hasTag() && stack.getTag().contains(ITEM_MARKER)) {
                stack.getTag().remove(ITEM_MARKER);
                if(stack.getTag().isEmpty()) {
                    stack.setTag(null);
                }
            }
            entry.setItem(stack);
        });
    }
}
