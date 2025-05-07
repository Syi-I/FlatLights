package com.uberhelixx.flatlights.common.event;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.effect.EntangledEffect;
import com.uberhelixx.flatlights.common.effect.ModEffects;
import com.uberhelixx.flatlights.common.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EnchantmentEvents {
    //Quantum Strike entangled damage to nearby mobs
    @SubscribeEvent
    public static void entangleDmg(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        LivingEntity attacker = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null;
        //make sure that there's an attacker to cause the entangled damage
        if(attacker == null) {
            return;
        }
        double searchRadius = FlatLightsCommonConfig.entangledRange.get();
        //stop this from infinitely looping damage
        if(!event.getSource().is(ModDamageTypes.ENTANGLED) && target.hasEffect(ModEffects.ENTANGLED.get())) {
            //grab all nearby entities within the search radius and apply an instance of entangled damage
            List<Entity> entities = target.level().getEntities(target, target.getBoundingBox().inflate(searchRadius, searchRadius, searchRadius));
            for (Entity instance : entities) {
                if (instance != target && instance instanceof LivingEntity && ((LivingEntity) instance).hasEffect(ModEffects.ENTANGLED.get())) {
                    FlatLights.LOGGER.info("[Quantum Strike Enchant] Entangled mob: " + instance.getName());
                    instance.invulnerableTime = 0;
                    instance.hurt(ModDamageTypes.causeEntangledDamage(attacker), event.getAmount() * MiscUtils.damagePercentCalc(FlatLightsCommonConfig.entangledPercent.get()));
                    instance.invulnerableTime = 20;
                }
            }
        }
    }
    
    //Quantum Strike removing mobs from the entangled group when dying to reset glowing color
    @SubscribeEvent
    public static void removeFromEntangledTeam(LivingDeathEvent event) {
        LivingEntity entityIn = event.getEntity();
        Scoreboard scoreboard = entityIn.level().getScoreboard();
        //check if target entity is on the entangled team and remove if true
        if(entityIn.getTeam() != null) {
            if (entityIn.getTeam() == scoreboard.getPlayerTeam(EntangledEffect.getEntangledTeam())) {
                scoreboard.removePlayerFromTeam(entityIn.getStringUUID(), scoreboard.getPlayerTeam(EntangledEffect.getEntangledTeam()));
            }
        }
    }
    
    //Cancel knockback event for Blackhand enchantment
    @SubscribeEvent
    public static void blackhandKnockback(LivingKnockBackEvent event) {
        LivingEntity user = event.getEntity().getLastAttacker();
        ItemStack weapon = null;
        if(user != null) {
            weapon = user.getMainHandItem();
        }
        if(weapon != null && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLACKHAND.get(), user) > 0) {
            event.setCanceled(true);
        }
    }
    
    //Flash of Brilliance xp multiplier
    @SubscribeEvent
    public static void xpDropMultiplier(LivingExperienceDropEvent event) {
        LivingEntity user = event.getAttackingPlayer();
        int baseXpAmount = event.getDroppedExperience();
        Level world = user.level();
        //check if player died since the game freaks out and crashes
        if(event.getEntity() instanceof Player) {
            return;
        }
        //check if keepInventory is on
        if(event.getEntity() instanceof Player && event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FLASH_OF_BRILLIANCE.get(), user);
        //check if enchantment is present on mainhand item (the item that killed)
        if(level != 0) {
            double chanceCap = FlatLightsCommonConfig.fobChanceCap.get();
            double activeChance = level * 0.05;
            //if chance happens then multiply xp drop amount
            if(Math.random() <= Math.min(activeChance, chanceCap)) {
                event.setDroppedExperience(10 * baseXpAmount);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 5.0F, 1.0F);
                FlatLights.LOGGER.info("[Flash of Brilliance] Triggered XP multiplier.");
                FlatLights.LOGGER.info("[Flash of Brilliance] Base XP value: " + baseXpAmount + " | New XP value: " + baseXpAmount * 10);
            }
        }
    }
    
    protected static final UUID LIFTED_TRUCK_ARMOR = UUID.fromString("2e8190a1-c663-42d9-8921-1251d45c3efa");
    
    //Apply the extra armor points from the Lifted Pickup Truck curse upon equipping the armor
    @SubscribeEvent
    public static void liftedPickupTruckArmor(LivingEquipmentChangeEvent event) {
        LivingEntity entity = event.getEntity();
        Iterable<ItemStack> armorPieces = entity.getArmorSlots();
        
        //check if entity has modifier from lifted truck curse, remove if it does to reset value and prevent stacking
        AttributeInstance armor = entity.getAttribute(Attributes.ARMOR);
        if(armor != null) {
            armor.removeModifier(LIFTED_TRUCK_ARMOR);
        }
        
        //check all armor pieces for lifted pickup truck curse and get total armor value
        double totalArmor = 0;
        for(ItemStack armorPart : armorPieces) {
            //if it has the curse, double the armor amount of the piece
            if(whatArmorSlot(armorPart) != EquipmentSlot.MAINHAND & armorPart.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.LIFTED_PICKUP_TRUCK.get(), entity) > 0) {
                //get armor attribute from the item in this specific armor slot
                Multimap<Attribute, AttributeModifier> oldMap = armorPart.getAttributeModifiers(whatArmorSlot(armorPart));
                //get armor value of the cursed armor piece and double it
                Collection<AttributeModifier> initialArmorValues = oldMap.get(Attributes.ARMOR);
                for(AttributeModifier armorPoints : initialArmorValues) {
                    totalArmor += armorPoints.getAmount();
                }
                FlatLights.LOGGER.info("[Lifted Pickup Truck ARMOR] Cursed Armor Total: " + totalArmor);
            }
        }
        //only apply the modifier if there is cursed armor
        if(totalArmor > 0 && armor != null) {
            armor.addTransientModifier(new AttributeModifier(LIFTED_TRUCK_ARMOR, "Lifted Truck Armor Modifier", totalArmor, AttributeModifier.Operation.ADDITION));
            FlatLights.LOGGER.info("[Lifted Pickup Truck ARMOR] Cursed Armor Added: " + totalArmor);
            
        }
    }
    
    //gets the equipment slot of passed through armor if applicable
    private static EquipmentSlot whatArmorSlot(ItemStack item) {
        //if the item stack passed through is armor, return the armor equipment slot
        if(item.getItem() instanceof ArmorItem armorPiece) {
            return armorPiece.getEquipmentSlot();
        }
        
        //default case for if it isn't an armor piece
        return EquipmentSlot.MAINHAND;
    }
    
    //Do the increased damage to entities with the Lifted Pickup Truck curse
    @SubscribeEvent
    public static void liftedPickupTruckDmg(LivingHurtEvent event) {
        //multiplier for the increased damage, maybe give a config option in the future
        float DMG_MULTIPLIER = 3f;
        LivingEntity target = event.getEntity();
        
        //get all instances of armor attributes from the target entity
        AttributeInstance armor = target.getAttribute(Attributes.ARMOR);
        
        //check for existing armor attribute and lifted truck curse modifier
        if(armor != null && armor.getModifier(LIFTED_TRUCK_ARMOR) != null) {
            //if there is cursed armor on (value of modifier > 0), then increase damage amount
            if(Objects.requireNonNull(armor.getModifier(LIFTED_TRUCK_ARMOR)).getAmount() > 0) {
                FlatLights.LOGGER.info("[Lifted Pickup Truck DAMAGE] Initial Damage: " + event.getAmount());
                FlatLights.LOGGER.info("[Lifted Pickup Truck DAMAGE] New Damage: " + event.getAmount() * DMG_MULTIPLIER);
                event.setAmount(event.getAmount() * DMG_MULTIPLIER);
            }
        }
    }
    
    //Neutralizer damage conversion to physical
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void damageSourceConversion(LivingHurtEvent event) {
        LivingEntity target = event.getEntity();
        float damageAmount = event.getAmount();
        Entity attacker = event.getSource().getEntity();
        
        //makes sure the outside source isn't physical damage so this doesn't infinitely loop
        if(!event.getSource().is(ModDamageTypes.PHYSICAL)) {
            //if neutralizer present, cancel initial damage event and trigger equivalent physical damage instead
            if(EnchantmentHelper.getEnchantmentLevel(ModEnchantments.NEUTRALIZER.get(), target) > 0) {
                FlatLights.LOGGER.info("[Neutralizer] Neutralizer enchantment triggered");
                FlatLights.LOGGER.info("[Neutralizer] Initial un-neutralized damage: " + damageAmount);
                event.setCanceled(true);
                doPhysDmg(target, attacker, damageAmount);
            }
        }
    }
    
    //does one instance of physical damage to a target, self-explanatory
    private static void doPhysDmg(LivingEntity target, Entity attacker, float damageAmount) {
        FlatLights.LOGGER.info("[Neutralizer] Doing physical damage");
        target.invulnerableTime = 0;
        if(attacker != null) {
            target.hurt(ModDamageTypes.causePhysicalDamage(attacker), damageAmount);
        }
        else {
            target.hurt(ModDamageTypes.causePhysicalDamage(target), damageAmount);
        }
    }
    
    //Pulsing Arrow aoe damage
    @SubscribeEvent
    public static void arrowPulseDmg(ProjectileImpactEvent event) {
        //check if projectile was an arrow
        if(event.getProjectile() instanceof Arrow arrow) {
            //grab search radius from config to determine how far to look for mobs to damage
            double searchRadius = FlatLightsCommonConfig.pulsingArrowRadius.get();
            //check if arrow was shot from an actual player/entity
            if (arrow.getOwner() != null && arrow.getOwner() instanceof LivingEntity shooter) {
                FlatLights.LOGGER.info("[Pulsing Arrow Damage] Shooter of arrow: " + shooter.getName());
                ItemStack bow = shooter.getMainHandItem();
                //check enchantment level from held bow to calculate the splash damage
                int pulseLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PULSINGARROW.get(), shooter);
                if (pulseLevel != 0) {
                    List<Entity> entities = arrow.level().getEntities(arrow, arrow.getBoundingBox().inflate(searchRadius, searchRadius, searchRadius));
                    //damage all mobs found in the search radius of the arrow
                    for (Entity instance : entities) {
                        if (instance instanceof LivingEntity && !instance.is(arrow)) {
                            double arrowDamage = arrow.getBaseDamage();
                            float pulseDamage = (float) (2F * arrowDamage * (pulseLevel) * MiscUtils.damagePercentCalc(FlatLightsCommonConfig.pulsingPercent.get()));
                            instance.invulnerableTime = 0;
                            instance.hurt(ModDamageTypes.causeIndirectPhysicalDamage(arrow, shooter), pulseDamage);
                            instance.invulnerableTime = 0;
                            FlatLights.LOGGER.info("[Pulsing Arrow Damage] Pulse damaged mob: " + instance.getName());
                            FlatLights.LOGGER.info("[Pulsing Arrow Damage] Arrow damage: " + arrowDamage);
                            FlatLights.LOGGER.info("[Pulsing Arrow Damage] Initial pulse damage: " + pulseDamage);
                        }
                    }
                }
            }
        }
    }
    
    //Shimmer enchantment overcap anvil event
    @SubscribeEvent
    public static void shimmerOverload(AnvilUpdateEvent event) {
        if (Objects.requireNonNull(event.getPlayer()).level().isClientSide()) { return; }
        
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
        if (bookMap.isEmpty() || !(shimmerlvl2Only && bookMap.size() == 1) || Math.random() <= 0.5/*0.99999*/) { return; }
        
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
}
