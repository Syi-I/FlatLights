package com.uberhelixx.flatlights.util;

import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsClientConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class MiscHelpers {

    public static String coloredText(TextFormatting color, String input) {
        return ("" + color + input + TextFormatting.RESET);
    }

    public static double getItemDamage(ItemStack weapon) {
        String weaponMap = weapon.getAttributeModifiers(EquipmentSlotType.MAINHAND).get(Attributes.ATTACK_DAMAGE).toString().replaceFirst(".*?amount=([0-9]+\\.[0-9]+).*", "$1");
        double weaponDamage = 1.0;
        if(weaponMap.matches("[0-9]+\\.[0-9]+")) {
            weaponDamage = Double.parseDouble(weaponMap) + 1;
        }
        MiscHelpers.debugLogger("Mainhand weapon damage: " + weaponDamage);
        return weaponDamage;
    }

    public static float getTotalDamage(ItemStack weapon) {
        float fistDamage = 1;
        Multimap<Attribute, AttributeModifier> attributes = weapon.getAttributeModifiers(EquipmentSlotType.MAINHAND);
        Collection<AttributeModifier> collector;
        float weaponDamage = 0;

        //get the value of the input attribute
        collector = attributes.get(Attributes.ATTACK_DAMAGE);

        //make sure that collection actually has some entry in it to use
        if(!collector.isEmpty()) {
            //total up all attribute values of the input type
            for(AttributeModifier entry : collector) {
                double entryAmount = entry.getAmount();
                weaponDamage += entryAmount;
                //MiscHelpers.debugLogger("[Dmg Calc Misc Helper] Attack Dmg Attribute Name: " + Attributes.ATTACK_DAMAGE.getAttributeName());
                //MiscHelpers.debugLogger("[Dmg Calc Misc Helper] Collection Entry Name: " + entry);
            }
        }

        //base sharpness damage addition
        float sharpnessDamage = 0;

        //check if sharpness is on the held item
        if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, weapon) > 0) {
            //sharpness is 0.5 dmg + 0.5 * lvl for additional damage calcs
            sharpnessDamage = 0.5F;
            sharpnessDamage = (float) (sharpnessDamage + (0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, weapon)));
        }

        return (fistDamage + weaponDamage + sharpnessDamage);
    }

    public static float damagePercentCalc(Integer percent) {
        return (percent / 100F);
    }

    public static boolean enchantCheck(ItemStack item, Enchantment enchName) {
        if(item.isEnchanted()) {
            Map<Enchantment, Integer> instanceMap = EnchantmentHelper.getEnchantments(item);
            debugLogger("[Enchantment Checker] Checking for: " + enchName.toString());
            for (Map.Entry<Enchantment, Integer> entry : instanceMap.entrySet()) {
                debugLogger("[Enchantment Checker] Found enchantment: " + entry.toString());
                if (entry.getKey() == enchName) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Integer enchantLevelGrabber(ItemStack item, Enchantment enchName) {
        if(item.isEnchanted()) {
            Map<Enchantment, Integer> instanceMap = EnchantmentHelper.getEnchantments(item);
            debugLogger("[Enchantment Checker] Checking for: " + enchName.toString());
            for (Map.Entry<Enchantment, Integer> entry : instanceMap.entrySet()) {
                debugLogger("[Enchantment Checker] Found enchantment: " + entry.toString());
                if (entry.getKey() == enchName) {
                    return entry.getValue();
                }
            }
        }
        return 0;
    }

    //add stuff to a team with an assigned color so that the glowing effect has a different color
    public static void addToTeam(LivingEntity entityIn, String teamName, TextFormatting color) {
        //get existing scoreboard from world
        Scoreboard scoreboard = entityIn.getEntityWorld().getScoreboard();
        //try to get existing team from scoreboard or create the new team if it doesn't exist
        if (scoreboard.getTeam(teamName) == null) {
            scoreboard.createTeam(teamName);
        }
        //add entity to team and change color of team if it is not already the input color
        scoreboard.addPlayerToTeam(entityIn.getCachedUniqueIdString(), scoreboard.getTeam(teamName));
        if(scoreboard.getTeam(teamName).getColor() != color) {
            scoreboard.getTeam(teamName).setColor(color);
        }
    }

    public static void debugLogger(String message) {
        if (FlatLightsClientConfig.miscLogging.get()) {
            FlatLights.LOGGER.debug(message);
        }
    }

    public static boolean uuidCheck(UUID targetUuid) {
        if(FlatLightsClientConfig.testValue.get()) {
            //380df991-f603-344c-a090-369bad2a924a is dev1 uuid
            if (0 == targetUuid.compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"))) {
                return true;
            }
            //fabd0a49-3695-401c-9990-d95464632a6a is syi uuid
            if (0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) {
                return true;
            }
            //db427397-20a1-4996-96c0-bbf29ca7672f is hel uuid
            if (0 == targetUuid.compareTo(UUID.fromString("db427397-20a1-4996-96c0-bbf29ca7672f"))) {
                return true;
            }
        }
        return false;
    }
}
