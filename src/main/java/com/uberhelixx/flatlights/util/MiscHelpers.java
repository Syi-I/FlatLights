package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsClientConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;

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
            //db427397-20a1-4996-96c0-bbf29ca7672f is syi uuid
            if (0 == targetUuid.compareTo(UUID.fromString("db427397-20a1-4996-96c0-bbf29ca7672f"))) {
                return true;
            }
        }
        return false;
    }
}
