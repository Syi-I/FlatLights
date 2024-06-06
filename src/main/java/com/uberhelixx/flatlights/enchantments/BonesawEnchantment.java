package com.uberhelixx.flatlights.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class BonesawEnchantment extends Enchantment {
    public BonesawEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentType.WEAPON, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMaxLevel() { return 1; }

    //mnoved to EnchantmentEvents because apparently onEntityDamaged double fires, leading to double stack gain
    /*@Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        int stackCap = FlatLightsCommonConfig.bonesawStacks.get() - 1;

        if(target instanceof LivingEntity) {
            LivingEntity hitEntity = (LivingEntity) target;
            if(hitEntity.isPotionActive(ModEffects.ARMOR_SHRED.get())) {
                int amplifier = Objects.requireNonNull(hitEntity.getActivePotionEffect(ModEffects.ARMOR_SHRED.get())).getAmplifier();
                if(user instanceof PlayerEntity) {
                    ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bonesaw] Amplifier level: " + amplifier);
                    user.sendMessage(message, user.getUniqueID());
                }
                hitEntity.addPotionEffect(new EffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(amplifier + 1, stackCap)));
            }
            else {
                if(user instanceof PlayerEntity) {
                    ITextComponent message = ITextComponent.getTextComponentOrEmpty("[Bonesaw] Enchant level: " + level);
                    user.sendMessage(message, user.getUniqueID());
                }
                hitEntity.addPotionEffect(new EffectInstance(ModEffects.ARMOR_SHRED.get(), 600, Math.min(level - 1, stackCap)));
            }
        }
    }*/
}
