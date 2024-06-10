package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.armor.PrismaticHelm;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static com.uberhelixx.flatlights.item.armor.ModArmorItem.*;
import static com.uberhelixx.flatlights.item.armor.PrismaticChestplate.onEquip;
import static com.uberhelixx.flatlights.item.armor.PrismaticChestplate.onUnequip;

@Mod.EventBusSubscriber(modid = FlatLights.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorEvents {

    //Prismatic Armor, reduce incoming damage amount based on armor total, requires at least one piece of prisma armor on to work
    @SubscribeEvent
    public static void DamageReduction(LivingHurtEvent event) {
        if(!event.getSource().isUnblockable()) {
            if(event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if(wearingBoots(player) || wearingLegs(player) || wearingChest(player) || wearingHelm(player)) {
                    int armorTotal = player.getTotalArmorValue();
                    MiscHelpers.debugLogger("[Prismatic Armor] Total armor value is " + armorTotal);
                    MiscHelpers.debugLogger("[Prismatic Armor] Initial damage is " + event.getAmount());
                    int armorVsDiamondTotal = armorTotal - 20;
                    float reductionRatioCap = FlatLightsCommonConfig.armorDamageReduction.get() / 100f;
                    float reductionRatio;

                    //get reductionRatio, make sure percent doesn't go above reductionRatioCap %
                    if(armorVsDiamondTotal > 0 && reductionRatioCap > 0) {
                        reductionRatio = Math.min((armorVsDiamondTotal * 0.05f), reductionRatioCap);
                    }
                    //normal damage taken if at or below diamond level armor
                    else {
                        reductionRatio = 0;
                    }
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

                    MiscHelpers.debugLogger("[Prismatic Armor] Reduced damage is now " + reducedDamage);
                    event.setAmount(reducedDamage);
                }
            }
        }
    }

    //Prismatic Boots cancel fall dmg events
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void negateFallDamage(LivingHurtEvent event) {
        DamageSource dmgSrc = event.getSource();
        if(event.getEntityLiving() instanceof PlayerEntity && wearingBoots((PlayerEntity) event.getEntityLiving())) {
            if(dmgSrc == DamageSource.FALL) {
                event.setCanceled(true);
            }
        }
    }

    //Prismatic Chestplate checking for equipment changes to toggle flying capability
    @SubscribeEvent
    public static void chestplateEquip(LivingEquipmentChangeEvent event) {
        if(event.getEntityLiving() instanceof PlayerEntity) {
            if(event.getSlot() == EquipmentSlotType.CHEST) {
                if(event.getFrom() == event.getTo() && event.getFrom().getItem() == ModItems.PRISMATIC_CHESTPLATE.get()) {
                    return;
                }
                else if(event.getTo().getItem() == ModItems.PRISMATIC_CHESTPLATE.get()) {
                    onEquip((PlayerEntity) event.getEntityLiving());
                }
                else {
                    onUnequip((PlayerEntity) event.getEntityLiving());
                }
            }
        }
    }

    //Prismatic Helmet giving/removing night vision and water breathing on equipment change
    @SubscribeEvent
    public static void helmetEquip(LivingEquipmentChangeEvent event) {
        PlayerEntity player;
        boolean hasNightVis;
        boolean hasWaterBreath;
        if(event.getEntityLiving() instanceof PlayerEntity) {
            player = (PlayerEntity) event.getEntityLiving();
            hasNightVis = !Objects.equals(player.getActivePotionEffect(Effects.NIGHT_VISION), null);
            hasWaterBreath = !Objects.equals(player.getActivePotionEffect(Effects.WATER_BREATHING), null);
        }
        else {
            return;
        }
        if(event.getSlot() == EquipmentSlotType.HEAD) {
            if(event.getFrom() == event.getTo() && event.getFrom().getItem() == ModItems.PRISMATIC_HELMET.get()) {
                return;
            }
            else if(event.getTo().getItem() == ModItems.PRISMATIC_HELMET.get()) {
                PrismaticHelm.onEquip((PlayerEntity) event.getEntityLiving(), hasNightVis, hasWaterBreath);
            }
            else {
                PrismaticHelm.onUnequip((PlayerEntity) event.getEntityLiving(), hasNightVis, hasWaterBreath);
            }
        }
    }
}
