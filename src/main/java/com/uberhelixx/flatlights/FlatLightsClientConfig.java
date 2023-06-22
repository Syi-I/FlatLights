package com.uberhelixx.flatlights;

import net.minecraftforge.common.ForgeConfigSpec;

public final class FlatLightsClientConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> testValue;
    public static final ForgeConfigSpec.ConfigValue<Boolean> coreNoti;
    public static final ForgeConfigSpec.ConfigValue<Boolean> miscLogging;

    static {

        BUILDER.push("Enable/Disable Features");
        {
            BUILDER.comment("Enable chat notification for use with Prismatic Blade MK2. [Default: false]");
            coreNoti = BUILDER.define("CoreNotification", false);
        }
        BUILDER.pop();

        BUILDER.comment("If you don't know what you're doing just leave the values at default.").push("Dev Testing");
        {
            BUILDER.comment("Basic test value to see if config works properly, shouldn't do anything important. [Default: false]");
            testValue = BUILDER.define("TestValue", false);

            BUILDER.comment("Some extra logging things only meant for dev work. [Default: false]");
            miscLogging = BUILDER.define("MiscLogging", false);
        }
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
