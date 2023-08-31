package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.EntangledEffect;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EnchantmentEvents {

    //Flash of Brilliance xp multiplier
    @SubscribeEvent
    public static void xpDropMultiplier(LivingExperienceDropEvent event) {
        LivingEntity user = event.getAttackingPlayer();
        int baseXpAmount = event.getDroppedExperience();
        //check if player died since the game freaks out and crashes
        if(event.getEntity() instanceof PlayerEntity) {
            return;
        }
        //check if keepInventory is on
        if(event.getEntity() instanceof PlayerEntity && event.getEntity().getEntityWorld().getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            return;
        }

        ItemStack instance = user.getHeldItem(Hand.MAIN_HAND);
        int level = MiscHelpers.enchantLevelGrabber(instance, ModEnchantments.FLASH_OF_BRILLIANCE.get());
        //check if enchantment is present on mainhand item (the item that killed)
        if(level != 0) {
            double chanceCap = FlatLightsCommonConfig.fobChanceCap.get();
            double activeChance = level * 0.05;
            if(Math.random() <= Math.min(activeChance, chanceCap)) {
                event.setDroppedExperience(10 * baseXpAmount);
                MiscHelpers.debugLogger("[Flash of Brilliance] Triggered XP multiplier.");
                MiscHelpers.debugLogger("[Flash of Brilliance] Base XP value: " + baseXpAmount + " | New XP value: " + baseXpAmount * 10);
            }
        }
    }

    //Neutralizer damage conversion to physical
    @SubscribeEvent
    public static void damageSourceConversion(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        float damageAmount = event.getAmount();

        if(event.getSource() != ModDamageTypes.PHYSICAL) {
            for (ItemStack instance : target.getArmorInventoryList()) {
                if(MiscHelpers.enchantCheck(instance, ModEnchantments.NEUTRALIZER.get())) {
                    MiscHelpers.debugLogger("[Neutralizer] Neutralizer enchantment triggered");
                    MiscHelpers.debugLogger("[Neutralizer] Initial un-neutralized damage: " + damageAmount);
                    event.setCanceled(true);
                    doPhysDmg(target, damageAmount);
                }
            }
        }
    }

    private static void doPhysDmg(LivingEntity target, float damageAmount) {
        MiscHelpers.debugLogger("[Neutralizer] Doing physical damage");
        target.hurtResistantTime = 0;
        target.attackEntityFrom(ModDamageTypes.PHYSICAL, damageAmount);
        target.hurtResistantTime = 20;
    }

    //Pulsing Arrow aoe damage
    @SubscribeEvent
    public static void arrowPulseDmg(ProjectileImpactEvent.Arrow event) {
        //get arrow that was shot
        AbstractArrowEntity arrow = event.getArrow();
        //grab search radius from config to determine how far to look for mobs to damage
        double searchRadius = FlatLightsCommonConfig.pulsingArrowRadius.get();
        //check if arrow was shot from an actual player/entity
        if(arrow.getShooter() != null && arrow.getShooter() instanceof LivingEntity) {
            LivingEntity shooter = (LivingEntity) arrow.getShooter();
            MiscHelpers.debugLogger("[Pulsing Arrow Damage] Shooter of arrow: " + shooter.getName());
            ItemStack bow = shooter.getHeldItemMainhand();
            //check enchantment level from held bow to calculate the splash damage
            Integer pulseLevel = MiscHelpers.enchantLevelGrabber(bow, ModEnchantments.PULSINGARROW.get());
            if(pulseLevel != 0) {
                List<Entity> entities = arrow.world.getEntitiesWithinAABBExcludingEntity(arrow, arrow.getBoundingBox().grow(searchRadius, searchRadius, searchRadius));
                //damage all mobs found in the search radius of the arrow
                for (Entity instance : entities) {
                    if (instance instanceof LivingEntity) {
                        double arrowDamage = arrow.getDamage();
                        float pulseDamage = (float) (2F * arrowDamage * (pulseLevel) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.pulsingPercent.get()));
                        instance.hurtResistantTime = 0;
                        instance.attackEntityFrom(ModDamageTypes.causeIndirectPhysDmg(arrow, shooter), pulseDamage);
                        instance.hurtResistantTime = 0;
                        MiscHelpers.debugLogger("[Pulsing Arrow Damage] Pulse damaged mob: " + instance.getName());
                        MiscHelpers.debugLogger("[Pulsing Arrow Damage] Arrow damage: " + arrowDamage);
                        MiscHelpers.debugLogger("[Pulsing Arrow Damage] Initial pulse damage: " + pulseDamage);
                    }
                }
            }
        }
    }

    //Quantum Strike entangled damage to nearby mobs
    @SubscribeEvent
    public static void entangleDmg(LivingHurtEvent event) {
        LivingEntity target = event.getEntityLiving();
        double searchRadius = FlatLightsCommonConfig.entangledRange.get();
        if(event.getSource() != ModDamageTypes.ENTANGLED) {
            List<Entity> entities = target.world.getEntitiesWithinAABBExcludingEntity(target, target.getBoundingBox().grow(searchRadius, searchRadius, searchRadius));
            for (Entity instance : entities) {
                if (instance instanceof LivingEntity && ((LivingEntity) instance).isPotionActive(ModEffects.ENTANGLED.get())) {
                    MiscHelpers.debugLogger("[Quantum Strike Enchant] Entangled mob: " + instance.getName());
                    instance.hurtResistantTime = 0;
                    instance.attackEntityFrom(ModDamageTypes.ENTANGLED, event.getAmount() * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.entangledPercent.get()));
                    instance.hurtResistantTime = 20;
                }
            }
        }
    }

    //Quantum Strike removing mobs from the entangled group to reset glowing color
    @SubscribeEvent
    public static void removeFromEntangledTeam(LivingDeathEvent event) {
        Scoreboard scoreboard = event.getEntityLiving().getEntityWorld().getScoreboard();
        LivingEntity entityIn = event.getEntityLiving();
        if(entityIn.getTeam() != null) {
            if (entityIn.getTeam() == scoreboard.getTeam(EntangledEffect.getEntangledTeam())) {
                scoreboard.removePlayerFromTeam(entityIn.getCachedUniqueIdString(), scoreboard.getTeam(EntangledEffect.getEntangledTeam()));
            }
        }
    }

    //Shimmer enchantment overcap anvil event
    @SubscribeEvent
    public static void shimmerOverload(AnvilUpdateEvent event) {
        if (!Objects.requireNonNull(event.getPlayer()).isServerWorld()) { return; }

        ItemStack inputItem = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        if (inputItem == null || enchantedBook == null || enchantedBook.getItem() != Items.ENCHANTED_BOOK) { return; }

        Map<Enchantment, Integer> itemMap = EnchantmentHelper.getEnchantments(inputItem);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);
        Map<Enchantment, Integer> outputMap = new HashMap<>(itemMap);
        boolean shimmerlvl2Only = false;
        if(bookMap.containsKey(ModEnchantments.SHIMMER2.get())) {
            shimmerlvl2Only = bookMap.get(ModEnchantments.SHIMMER2.get()) == 2;
        }

        if (bookMap.isEmpty() || !(shimmerlvl2Only && bookMap.size() == 1) || Math.random() <= 0.99999) { return; }

        for (Map.Entry<Enchantment, Integer> bookEnchEntry : bookMap.entrySet()) {
            Enchantment enchantment = bookEnchEntry.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentEnchLevel = itemMap.get(bookEnchEntry.getKey());
            Integer addValue = bookEnchEntry.getValue();
            if (currentEnchLevel == null) {
                outputMap.put(bookEnchEntry.getKey(), addValue + 10);
            }
        }

        for (Map.Entry<Enchantment, Integer> existingEnch : itemMap.entrySet()) {
            Enchantment enchantment = existingEnch.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentEnchLevel = existingEnch.getValue();
            if(currentEnchLevel >= enchantment.getMaxLevel() * FlatLightsCommonConfig.enchantMultiplierCap.get()) {
                outputMap.put(existingEnch.getKey(), currentEnchLevel);
            }
            else {
                outputMap.put(existingEnch.getKey(), currentEnchLevel + 10);
            }
        }

        ItemStack enchantedItem = inputItem.copy();
        EnchantmentHelper.setEnchantments(outputMap, enchantedItem);
        event.setOutput(enchantedItem);
    }
}
