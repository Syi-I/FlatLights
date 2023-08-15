package com.uberhelixx.flatlights.enchantments;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.effect.EntangledEffect;
import com.uberhelixx.flatlights.effect.ModEffects;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class QuantumStrikeEnchantment extends Enchantment {
    public QuantumStrikeEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 5; }


    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        if(target instanceof LivingEntity) {
            ItemStack weapon = user.getHeldItemMainhand();
            double weaponDamage = MiscHelpers.getItemDamage(weapon);
            target.hurtResistantTime = 0;
            target.attackEntityFrom(ModDamageTypes.causeIndirectQuantum(user, null), (float) (weaponDamage * (1 + (0.1F * level))) * MiscHelpers.damagePercentCalc(FlatLightsCommonConfig.quantumPercent.get()));
            ((LivingEntity) target).addPotionEffect(new EffectInstance(ModEffects.ENTANGLED.get(), 600, level));
            ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.GLOWING, 600));
            MiscHelpers.addToTeam((LivingEntity) target, EntangledEffect.getEntangledTeam(), TextFormatting.BLUE);
            //target.hurtResistantTime = 20;
        }
    }

    /*public float calcDamageByCreature(int level, CreatureAttribute creatureType) {
        return 1 + (0.75f * level);
    }*/

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
}
