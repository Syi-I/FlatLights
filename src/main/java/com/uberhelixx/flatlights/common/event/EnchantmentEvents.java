package com.uberhelixx.flatlights.common.event;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.effect.EntangledEffect;
import com.uberhelixx.flatlights.common.effect.ModEffects;
import com.uberhelixx.flatlights.common.enchantments.ModEnchantments;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.util.ClientUtils;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.ParticleHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;

import java.awt.*;
import java.util.*;
import java.util.List;

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
                    MiscUtils.infoLog("[Quantum Strike Enchant] Entangled mob: " + instance.getName());
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
        //make sure there is an attacking player and not something like /kill
        if(user == null) {
            return;
        }
        //check if player died since the game freaks out and crashes
        if(event.getEntity() instanceof Player) {
            return;
        }
        //check if keepInventory is on
        if(event.getEntity() instanceof Player && event.getEntity().level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        
        Level world = user.level();
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FLASH_OF_BRILLIANCE.get(), user);
        //check if enchantment is present on mainhand item (the item that killed)
        if(level != 0) {
            double chanceCap = FlatLightsCommonConfig.fobChanceCap.get();
            double activeChance = level * 0.05;
            //if chance happens then multiply xp drop amount
            if(Math.random() <= Math.min(activeChance, chanceCap)) {
                event.setDroppedExperience(10 * baseXpAmount);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.NEUTRAL, 5.0F, 1.0F);
                MiscUtils.infoLog("[Flash of Brilliance] Triggered XP multiplier.");
                MiscUtils.infoLog("[Flash of Brilliance] Base XP value: " + baseXpAmount + " | New XP value: " + baseXpAmount * 10);
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
                MiscUtils.infoLog("[Lifted Pickup Truck ARMOR] Cursed Armor Total: " + totalArmor);
            }
        }
        //only apply the modifier if there is cursed armor
        if(totalArmor > 0 && armor != null) {
            armor.addTransientModifier(new AttributeModifier(LIFTED_TRUCK_ARMOR, "Lifted Truck Armor Modifier", totalArmor, AttributeModifier.Operation.ADDITION));
            MiscUtils.infoLog("[Lifted Pickup Truck ARMOR] Cursed Armor Added: " + totalArmor);
            
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
                MiscUtils.infoLog("[Lifted Pickup Truck DAMAGE] Initial Damage: " + event.getAmount());
                MiscUtils.infoLog("[Lifted Pickup Truck DAMAGE] New Damage: " + event.getAmount() * DMG_MULTIPLIER);
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
                MiscUtils.infoLog("[Neutralizer] Neutralizer enchantment triggered");
                MiscUtils.infoLog("[Neutralizer] Initial un-neutralized damage: " + damageAmount);
                event.setCanceled(true);
                doPhysDmg(target, attacker, damageAmount);
            }
        }
    }
    
    //does one instance of physical damage to a target, self-explanatory
    private static void doPhysDmg(LivingEntity target, Entity attacker, float damageAmount) {
        MiscUtils.infoLog("[Neutralizer] Doing physical damage");
        target.invulnerableTime = 0;
        if(attacker != null) {
            target.hurt(ModDamageTypes.causePhysicalDamage(attacker), damageAmount);
        }
        else {
            target.hurt(ModDamageTypes.causePhysicalDamage(target), damageAmount);
        }
    }
    
    //Pulsing Arrow aoe damage when arrow hits block instead of entity
    @SubscribeEvent
    public static void arrowPulseDmgMiss(ProjectileImpactEvent event) {
        HitResult.Type hitResult = event.getRayTraceResult().getType();
        //check if projectile was an arrow and missed any entity
        if(event.getProjectile() instanceof Arrow arrow && !hitResult.equals(HitResult.Type.ENTITY)) {
            //grab search radius from config to determine how far to look for mobs to damage
            double searchRadius = FlatLightsCommonConfig.pulsingArrowRadius.get();
            //check if arrow was shot from an actual player/entity
            if (arrow.getOwner() != null && arrow.getOwner() instanceof LivingEntity shooter) {
                MiscUtils.infoLog("[Pulsing Arrow Damage Miss] Shooter of arrow: " + shooter.getName());
                //check enchantment level from held bow to calculate the splash damage
                int pulseLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PULSINGARROW.get(), shooter);
                if (pulseLevel != 0) {
                    List<Entity> entities = arrow.level().getEntities(arrow, arrow.getBoundingBox().inflate(searchRadius, searchRadius, searchRadius));
                    //damage all mobs found in the search radius of the arrow
                    for (Entity instance : entities) {
                        //make sure it's a livingentity, not the arrow itself, and not the shooter
                        if (instance instanceof LivingEntity && !instance.is(arrow) && !instance.is(shooter)) {
                            double arrowDamage = arrow.getBaseDamage();
                            float pulseDamage = (float) (2F * arrowDamage * (pulseLevel) * MiscUtils.damagePercentCalc(FlatLightsCommonConfig.pulsingPercent.get()));
                            instance.hurt(ModDamageTypes.causeIndirectPhysicalDamage(arrow, shooter), pulseDamage);
                            
                            MiscUtils.infoLog("[Pulsing Arrow Damage Miss] Pulse damaged mob: " + instance.getName());
                            MiscUtils.infoLog("[Pulsing Arrow Damage Miss] Arrow damage: " + arrowDamage);
                            MiscUtils.infoLog("[Pulsing Arrow Damage Miss] Initial pulse damage: " + pulseDamage);
                        }
                    }
                    float[] hsbVals = Color.RGBtoHSB(36, 217, 153, null);
                    ParticleOptions pulseParticle = ParticleHelper.constructSimpleSpark(Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]),
                            0.3f, 3, 1);
                    ParticleHelper.createBall(pulseParticle, event.getRayTraceResult().getLocation(), event.getEntity().level(),
                            3, 0.8f);
                    /*float[] hsbVals2 = Color.RGBtoHSB(231, 158, 46, null);
                    ParticleOptions fragParticle = ParticleHelper.constructSimpleSpark(Color.getHSBColor(hsbVals2[0], hsbVals2[1], hsbVals2[2]),
                            0.2f, 6, 1);
                    ParticleHelper.createCylinder(fragParticle, event.getEntity().level(), 3,
                            new Vec3(0, 0.4, 0), 1.5, event.getEntity().position(), 0.6, 0.15f);*/
                }
            }
        }
    }
    
    //Pulsing Arrow aoe damage when hitting entity
    @SubscribeEvent
    public static void arrowPulseDmgHit(LivingHurtEvent event) {
        LivingEntity shooter = event.getSource().getEntity() instanceof LivingEntity ? (LivingEntity) event.getSource().getEntity() : null;
        //if no shooter then do nothing
        if(shooter == null) {
            return;
        }
        
        Entity arrow = event.getSource().getDirectEntity();
        DamageSource source = event.getSource();
        LivingEntity hitEntity = event.getEntity();
        int pulseLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PULSINGARROW.get(), shooter);
        
        MiscUtils.infoLog("[Pulsing Arrow Damage Hit] Source Entity: " + shooter);
        MiscUtils.infoLog("[Pulsing Arrow Damage Hit] Arrow: " + arrow);
        
        //only trigger if arrow damage from someone with pulsing arrow enchant
        if(source.is(DamageTypes.ARROW) && pulseLevel > 0) {
            //grab search radius from config to determine how far to look for mobs to damage
            double searchRadius = FlatLightsCommonConfig.pulsingArrowRadius.get();
            List<Entity> entities = hitEntity.level().getEntities(hitEntity, hitEntity.getBoundingBox().inflate(searchRadius, searchRadius, searchRadius));
            //damage all mobs found in the search radius of the arrow
            for (Entity instance : entities) {
                //make sure it's a livingentity, not the already damaged entity, and not the shooter
                if (instance instanceof LivingEntity && !instance.is(hitEntity) && !instance.is(shooter)) {
                    double arrowDamage = event.getAmount();
                    float pulseDamage = (float) (2F * arrowDamage * (pulseLevel) * MiscUtils.damagePercentCalc(FlatLightsCommonConfig.pulsingPercent.get()));
                    instance.hurt(ModDamageTypes.causeIndirectPhysicalDamage(arrow, shooter), pulseDamage);
                    MiscUtils.infoLog("[Pulsing Arrow Damage HIT] Pulse damaged mob: " + instance.getName());
                    MiscUtils.infoLog("[Pulsing Arrow Damage HIT] Arrow damage: " + arrowDamage);
                    MiscUtils.infoLog("[Pulsing Arrow Damage HIT] Initial pulse damage: " + pulseDamage);
                }
            }
            if(hitEntity.level().isClientSide()) {
                MiscUtils.infoLog("[Pulsing Arrow Damage HIT] Trying particle spawn.");
                float[] hsbVals = Color.RGBtoHSB(36, 217, 153, null);
                ParticleOptions pulseParticle = ParticleHelper.constructSimpleSpark(Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]),
                        0.3f, 3, 1);
                Vec3 loc = new Vec3(hitEntity.getX(), hitEntity.getY() + hitEntity.getBbHeight(), hitEntity.getZ());
                ParticleHelper.createBall(pulseParticle, loc, hitEntity.level(),
                        3, 0.8f);
                Level world = hitEntity.level();
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
        if (bookMap.isEmpty() || !(shimmerlvl2Only && bookMap.size() == 1) || Math.random() <= 0.99999/*0.99999*/) { return; }
        
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
    
    @SubscribeEvent
    public static void fragmentationDamage(LivingDamageEvent event) {
        LivingEntity hitEntity = event.getEntity();
        Level world = hitEntity.level();
        int fragLevel = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.FRAGMENTATION.get(), hitEntity);
        //if fragmentation enchantment level exists, reduce incoming damage and cap to a portion of max HP
        if(fragLevel > 0) {
            float damageCap = hitEntity.getMaxHealth() / (fragLevel + 1);
            
            MiscUtils.infoLog("[Fragmentation Hit] Fragmentation Level: " + fragLevel);
            MiscUtils.infoLog("[Fragmentation Hit] Incoming Damage: " + event.getAmount());
            MiscUtils.infoLog("[Fragmentation Hit] Fragmentation HP Cap: " + damageCap);
            
            if(event.getAmount() > damageCap) {
                MiscUtils.infoLog("[Fragmentation Hit] Fragmentation threshold exceeded");
                hitEntity.playSound(SoundEvents.TOTEM_USE, 0.4f, 0.3F / (hitEntity.level().random.nextFloat() * 0.4F + 0.8F));
                /*if(hitEntity.level().isClientSide()) {
                    float[] hsbVals = Color.RGBtoHSB(231, 158, 46, null);
                    ParticleOptions fragParticle = ParticleHelper.constructSimpleSpark(Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]),
                            0.2f, 6, 1);
                    ParticleHelper.createCylinder(fragParticle, hitEntity.level(), 3,
                            new Vec3(0, 0.4, 0), 1.5, hitEntity.position(), 0.6, 0.15f);
                    world.addParticle(ParticleTypes.SCULK_SOUL, 0, 1, 0, 0, 1, 0);
                    world.addParticle(ParticleTypes.SCULK_SOUL, 0, 1, 0, 0, 1, 0);
                    world.addParticle(ParticleTypes.SCULK_SOUL, 0, 1, 0, 0, 1, 0);
                    
                }*/
                event.setAmount(damageCap);
            }
        }
    }
}
