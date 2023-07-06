package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

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
            //fabd0a49-3695-401c-9990-d95464632a6a is help1 uuid
            if (0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) {
                return true;
            }
        }
        return false;
    }
}
