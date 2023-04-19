package com.uberhelixx.flatlights.util;

import java.util.UUID;

public class MiscHelpers {

    public static boolean uuidCheck(UUID targetUuid) {
        //380df991-f603-344c-a090-369bad2a924a is dev uuid
        if(0 == targetUuid.compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"))) { return true; }
        if(0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) { return true; }
        return false;
    }
}
