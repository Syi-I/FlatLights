package com.uberhelixx.flatlights.common.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.armor.PrismaticChestplate;
import com.uberhelixx.flatlights.common.item.armor.PrismaticHelm;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static com.uberhelixx.flatlights.common.item.armor.BaseArmorItem.*;
import static com.uberhelixx.flatlights.common.item.armor.PrismaticChestplate.onEquip;
import static com.uberhelixx.flatlights.common.item.armor.PrismaticChestplate.onUnequip;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorEvents {

    //Prismatic Armor, reduce incoming damage amount based on armor total, requires at least one piece of prisma armor on to work
    @SubscribeEvent
    public static void DamageReduction(LivingHurtEvent event) {
        if(!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if(event.getEntity() instanceof Player player) {
                if(wearingBoots(player) || wearingLegs(player) || wearingChest(player) || wearingHelm(player)) {
                    int armorTotal = player.getArmorValue();
                    //FlatLights.LOGGER.info("[Prismatic Armor] Total armor value is " + armorTotal);
                    //FlatLights.LOGGER.info("[Prismatic Armor] Initial damage is " + event.getAmount());
                    float totalReduction = (float) (armorTotal * (FlatLightsCommonConfig.reductionPerPoint.get() / 100f));
                    float reductionRatioCap = FlatLightsCommonConfig.armorDamageReduction.get() / 100f;
                    //get reductionRatio, make sure percent doesn't go above reductionRatioCap %
                    float reductionRatio = Mth.clamp(totalReduction, 0, reductionRatioCap);
                    
                    float reducedDamage = event.getAmount() * (1 - reductionRatio);
                    
                    //reduces damage per piece of prismatic armor worn, instead of just once if wearing any amount of prismatic armor
                    if(FlatLightsCommonConfig.multilayerReduction.get()) {
                        reducedDamage = event.getAmount();
                        if(wearingBoots(player)) {
                            reducedDamage = reducedDamage * (1 - reductionRatio);
                        }
                        if(wearingLegs(player)) {
                            reducedDamage = reducedDamage * (1 - reductionRatio);
                        }
                        if(wearingChest(player)) {
                            reducedDamage = reducedDamage * (1 - reductionRatio);
                        }
                        if(wearingHelm(player)) {
                            reducedDamage = reducedDamage * (1 - reductionRatio);
                        }
                    }
                    
                    FlatLights.LOGGER.info("[Prismatic Armor] Reduced damage is now " + reducedDamage);
                    event.setAmount(reducedDamage);
                }
            }
        }
    }
    
    //Prismatic Boots cancel fall dmg events
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void negateFallDamage(LivingHurtEvent event) {
        DamageSource dmgSrc = event.getSource();
        if(event.getEntity() instanceof Player && wearingBoots((Player) event.getEntity())) {
            if(dmgSrc.is(DamageTypes.FALL)) {
                event.setCanceled(true);
            }
        }
    }
    
    //Prismatic Chestplate checking for equipment changes to toggle flying capability
    @SubscribeEvent
    public static void chestplateEquip(LivingEquipmentChangeEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(event.getSlot() == EquipmentSlot.CHEST) {
                //ignore if same item before and after
                if(event.getFrom().equals(event.getTo(), false)) {
                    return;
                }
                else if(event.getTo().getItem() instanceof PrismaticChestplate) {
                    onEquip(player);
                }
                else if(event.getFrom().getItem() instanceof PrismaticChestplate){
                    onUnequip(player);
                }
            }
        }
    }
    
    //Updates the player abilities for when the Prismatic Chestplate gets equipped, prevents having to double equip it
    @SubscribeEvent
    public static void playerTickChestplate(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if(player.getInventory().getArmor(2).getItem() instanceof PrismaticChestplate) {
            if(!player.getAbilities().flying && !player.getAbilities().mayfly && !player.isCreative() && !player.isSpectator()) {
                onEquip(player);
            }
        }
    }
    
    //Prismatic Helmet giving/removing night vision and water breathing on equipment change
    @SubscribeEvent
    public static void helmetEquip(LivingEquipmentChangeEvent event) {
        Player player;
        boolean hasNightVis;
        boolean hasWaterBreath;
        if(event.getEntity() instanceof Player) {
            player = (Player) event.getEntity();
            hasNightVis = !Objects.equals(player.getEffect(MobEffects.NIGHT_VISION), null);
            hasWaterBreath = !Objects.equals(player.getEffect(MobEffects.WATER_BREATHING), null);
        }
        else {
            return;
        }
        if(event.getSlot() == EquipmentSlot.HEAD) {
            if(event.getFrom() == event.getTo() && event.getFrom().getItem() == ModItems.PRISMATIC_HELMET.get()) {
                return;
            }
            else if(event.getTo().getItem() == ModItems.PRISMATIC_HELMET.get()) {
                PrismaticHelm.onEquip((Player) event.getEntity(), hasNightVis, hasWaterBreath);
            }
            else {
                PrismaticHelm.onUnequip((Player) event.getEntity(), hasNightVis, hasWaterBreath);
            }
        }
    }
}
