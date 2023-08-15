package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class PulsingArrowEnchantment extends Enchantment {
    public PulsingArrowEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.BOW, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 5; }

    @SubscribeEvent
    public static void arrowPulseDmg(ProjectileImpactEvent.Arrow event) {
        AbstractArrowEntity arrow = event.getArrow();
        double searchRadius = FlatLightsCommonConfig.pulsingArrowRadius.get();
        if(arrow.getShooter() != null && arrow.getShooter() instanceof LivingEntity) {
            LivingEntity shooter = (LivingEntity) arrow.getShooter();
            MiscHelpers.debugLogger("[Pulsing Arrow Damage] Shooter of arrow: " + shooter.getName());
            ItemStack bow = shooter.getHeldItemMainhand();
            Integer pulseLevel = MiscHelpers.enchantLevelGrabber(bow, ModEnchantments.PULSINGARROW.get());
            if(pulseLevel != 0) {
                List<Entity> entities = arrow.world.getEntitiesWithinAABBExcludingEntity(arrow, arrow.getBoundingBox().grow(searchRadius, searchRadius, searchRadius));
                for (Entity instance : entities) {
                    if (instance instanceof LivingEntity) {
                        double arrowDamage = arrow.getDamage();
                        float pulseDamage = (float) (2F * arrowDamage * (pulseLevel) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.pulsingPercent.get()));
                        instance.hurtResistantTime = 0;
                        instance.attackEntityFrom(DamageSource.LIGHTNING_BOLT, pulseDamage);
                        instance.hurtResistantTime = 0;
                        MiscHelpers.debugLogger("[Pulsing Arrow Damage] Pulse damaged mob: " + instance.getName());
                        MiscHelpers.debugLogger("[Pulsing Arrow Damage] Arrow damage: " + arrowDamage);
                        MiscHelpers.debugLogger("[Pulsing Arrow Damage] Initial pulse damage: " + pulseDamage);
                    }
                }
            }
        }
    }
}
