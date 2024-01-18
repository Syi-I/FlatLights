package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.EntangledEffect;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
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
        //int level = MiscHelpers.enchantLevelGrabber(instance, ModEnchantments.FLASH_OF_BRILLIANCE.get());
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FLASH_OF_BRILLIANCE.get(), instance);
        //check if enchantment is present on mainhand item (the item that killed)
        if(level != 0) {
            double chanceCap = FlatLightsCommonConfig.fobChanceCap.get();
            double activeChance = level * 0.05;
            //if chance happens then multiply xp drop amount
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

        //makes sure the outside source isn't physical damage so this doesn't infinitely loop
        if(event.getSource() != ModDamageTypes.PHYSICAL) {
            for (ItemStack instance : target.getArmorInventoryList()) {
                //if neutralizer present, cancel initial damage event and trigger equivalent physical damage instead
                if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.NEUTRALIZER.get(), instance) > 0) {
                    MiscHelpers.debugLogger("[Neutralizer] Neutralizer enchantment triggered");
                    MiscHelpers.debugLogger("[Neutralizer] Initial un-neutralized damage: " + damageAmount);
                    event.setCanceled(true);
                    doPhysDmg(target, damageAmount);
                }
            }
        }
    }

    //does one instance of physical damage to a target, self-explanatory
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
            //int pulseLevel = MiscHelpers.enchantLevelGrabber(bow, ModEnchantments.PULSINGARROW.get());
            int pulseLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PULSINGARROW.get(), bow);
            if(pulseLevel != 0) {
                List<Entity> entities = arrow.world.getEntitiesWithinAABBExcludingEntity(arrow, arrow.getBoundingBox().grow(searchRadius, searchRadius, searchRadius));
                //damage all mobs found in the search radius of the arrow
                for (Entity instance : entities) {
                    if (instance instanceof LivingEntity) {
                        double arrowDamage = arrow.getDamage();
                        float pulseDamage = (float) (2F * arrowDamage * (pulseLevel) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.pulsingPercent.get()));
                        instance.hurtResistantTime = 0;
                        instance.attackEntityFrom(ModDamageTypes.causeIndirectPhys(arrow, shooter), pulseDamage);
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
        //stop this from infinitely looping damage
        if(event.getSource() != ModDamageTypes.ENTANGLED) {
            //grab all nearby entities within the search radius and apply an instance of entangled damage
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

    //Quantum Strike removing mobs from the entangled group when dying to reset glowing color
    @SubscribeEvent
    public static void removeFromEntangledTeam(LivingDeathEvent event) {
        Scoreboard scoreboard = event.getEntityLiving().getEntityWorld().getScoreboard();
        LivingEntity entityIn = event.getEntityLiving();
        //check if target entity is on the entangled team and remove if true
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

        //gets enchantments and levels on item, book; creates new map for outputting new combined enchantments
        Map<Enchantment, Integer> itemMap = EnchantmentHelper.getEnchantments(inputItem);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);
        Map<Enchantment, Integer> outputMap = new HashMap<>(itemMap);
        boolean shimmerlvl2Only = false;
        //checks if its specifically shimmer 2 being applied, won't trigger otherwise
        if(bookMap.containsKey(ModEnchantments.SHIMMER2.get())) {
            shimmerlvl2Only = bookMap.get(ModEnchantments.SHIMMER2.get()) == 2;
        }

        //if book has no enchants, shimmer isn't lvl 2, or just fails the 0.001% chance of triggering
        if (bookMap.isEmpty() || !(shimmerlvl2Only && bookMap.size() == 1) || Math.random() <= 0.99999) { return; }

        //goes through each enchant on the book, adds 10 levels to each enchant
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

        //goes through each existing enchant on the item, adds 10 levels compared against the enchant multiplier cap so things can't exceed that config by accident
        for (Map.Entry<Enchantment, Integer> existingEnch : itemMap.entrySet()) {
            Enchantment enchantment = existingEnch.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentEnchLevel = existingEnch.getValue();
            //if value is higher than typical enchant cap, ignore changing this value so one can't infinitely increase the level or accidentally lower an overleveled enchant
            if(currentEnchLevel >= enchantment.getMaxLevel() * FlatLightsCommonConfig.enchantMultiplierCap.get()) {
                outputMap.put(existingEnch.getKey(), currentEnchLevel);
            }
            else {
                outputMap.put(existingEnch.getKey(), currentEnchLevel + 10);
            }
        }

        //set new output enchant map to the item
        ItemStack enchantedItem = inputItem.copy();
        EnchantmentHelper.setEnchantments(outputMap, enchantedItem);
        event.setOutput(enchantedItem);
    }

    //Bleeding Edge apply bleeding effect or increase stack count (amplifier level) when hit
    @SubscribeEvent
    public static void bleedingEdgeStacks(LivingHurtEvent event) {
        int stackCap = FlatLightsCommonConfig.bleedStacks.get() - 1;
        LivingEntity target = event.getEntityLiving();
        //source of damage for applying bleed, also gets player if existing
        Entity user = event.getSource().getTrueSource();
        int level = 0;
        //grab bleeding edge enchantment level if any is applied
        //make sure user isn't applying more stacks to themselves when damage ticks, if they are bleeding but also have an item with the enchant
        if(user instanceof PlayerEntity && target != user) {
            ItemStack weapon = ((PlayerEntity) user).getHeldItem(Hand.MAIN_HAND);
            level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLEEDING_EDGE.get(), weapon);
        }

        //check if target exists, enchant is applied, if damage is directly from player action
        if(target != null && level > 0 && user == event.getSource().getImmediateSource()) {
            if(target.isPotionActive(ModEffects.BLEED.get())) {
                //if potion effect is active then get amplifier level (0-4) and duration left (in ticks)
                int amplifier = Objects.requireNonNull(target.getActivePotionEffect(ModEffects.BLEED.get())).getAmplifier();
                int duration = Objects.requireNonNull(target.getActivePotionEffect(ModEffects.BLEED.get())).getDuration();
                ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bleeding Edge] Amplifier level: " + amplifier);
                //user.sendMessage(message, user.getUniqueID());
                //allow for reapplication if duration is low as otherwise bleed damage may never trigger from constantly being reapplied
                //allow for reapplication if next level of amplifier (aka bleed stacks present) is lower than the config stack cap
                if(duration <= (5 * 20) || amplifier + 1 <= stackCap) {
                    target.addPotionEffect(new EffectInstance(ModEffects.BLEED.get(), 600, Math.min(amplifier + 1, stackCap)));
                }
            }
            //base application if no bleed effect is active
            else {
                ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bleeding Edge] Enchant level: " + level);
                //user.sendMessage(message, user.getUniqueID());
                target.addPotionEffect(new EffectInstance(ModEffects.BLEED.get(), 600, Math.min(level - 1, stackCap)));
            }
        }
    }

    //Bonesaw apply armor shred effect or increase stacks (amplifier level) when hit
    @SubscribeEvent
    public static void bonesawStacks(LivingHurtEvent event) {
        int stackCap = FlatLightsCommonConfig.bonesawStacks.get() - 1;
        LivingEntity target = event.getEntityLiving();
        Entity user = event.getSource().getTrueSource();
        int level = 0;

        //check if user is a player and then look for if the mainhand item is enchanted with bonesaw, grab level if true
        //make sure user isn't applying more stacks to themselves when hit but also have an item with the enchant
        if(user instanceof PlayerEntity && target != user) {
            ItemStack weapon = ((PlayerEntity) user).getHeldItem(Hand.MAIN_HAND);
            level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BONESAW.get(), weapon);
        }

        //check if target exists, enchant is applied, if damage is directly from player action
        if(target != null && level > 0 && user == event.getSource().getImmediateSource()) {
            //if target has armor shred effect already, increase amplifier (aka bonesaw stacks)
            if(target.isPotionActive(ModEffects.ARMOR_SHRED.get())) {
                int amplifier = Objects.requireNonNull(target.getActivePotionEffect(ModEffects.ARMOR_SHRED.get())).getAmplifier();
                ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bonesaw] Amplifier level: " + amplifier);
                //user.sendMessage(message, user.getUniqueID());
                target.addPotionEffect(new EffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(amplifier + 1, stackCap)));
            }
            //initial application of armor shred effect when it is not already present on target
            else {
                ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bonesaw] Enchant level: " + level);
                //user.sendMessage(message, user.getUniqueID());
                target.addPotionEffect(new EffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(level - 1, stackCap)));
            }
        }
    }

    //Cancel knockback event for Blackhand enchantment
    @SubscribeEvent
    public static void blackhandKnockback(LivingKnockBackEvent event) {
        LivingEntity user = event.getEntityLiving().getAttackingEntity();
        ItemStack weapon = null;
        if(user != null) {
            weapon = user.getHeldItemMainhand();
        }
        if(weapon != null && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLACKHAND.get(), weapon) > 0) {
            event.setCanceled(true);
        }
    }
}
