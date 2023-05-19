package com.uberhelixx.flatlights.util;

import com.uberhelixx.flatlights.FlatLightsConfig;
import net.minecraft.util.text.TextFormatting;

import java.util.UUID;

public class MiscHelpers {

    public static String coloredText(TextFormatting color, String input) {
        return ("" + color + input + TextFormatting.RESET);
    }

    public static boolean uuidCheck(UUID targetUuid) {
        if(FlatLightsConfig.testValue.get()) {
            //380df991-f603-344c-a090-369bad2a924a is dev1 uuid
            if (0 == targetUuid.compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"))) {
                return true;
            }
            //fabd0a49-3695-401c-9990-d95464632a6a is main1 uuid
            if (0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) {
                return true;
            }
        }
        return false;
    }
}
