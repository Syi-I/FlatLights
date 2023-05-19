package com.uberhelixx.flatlights;

import net.minecraftforge.common.ForgeConfigSpec;

public final class FlatLightsConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> testValue;
    public static final ForgeConfigSpec.ConfigValue<Integer> enchantMultiplierCap;
    public static final ForgeConfigSpec.ConfigValue<Boolean> coreNoti;
    public static final ForgeConfigSpec.ConfigValue<Integer> healthDamageCap;
    public static final ForgeConfigSpec.ConfigValue<Double> healthDamagePercent;
    public static final ForgeConfigSpec.ConfigValue<Integer> armorDamageReduction;
    public static final ForgeConfigSpec.ConfigValue<Boolean> chestplateFlight;
    public static final ForgeConfigSpec.ConfigValue<Boolean> entityDamageableBlocks;
    public static final ForgeConfigSpec.ConfigValue<Boolean> indevBlocks;

    static {
        BUILDER.push("Item Stat Balancing");
        {
            BUILDER.comment("How high the enchantment level multipler cap can go to when enchanting the Prismatic Blade with an anvil. (e.g. double the cap = 2, triple the cap = 3, etc.) [Default: 2]");
            enchantMultiplierCap = BUILDER.defineInRange("EnchantmentMultiplierCap", 2, 1, Short.MAX_VALUE);

            BUILDER.comment("Damage cap for the percentage of health damage dealt by the Prismatic Blade. (Deals x% bonus damage up to this value) [Default: 100 damage]");
            healthDamageCap = BUILDER.defineInRange("HealthDamageCap", 100, 0, Integer.MAX_VALUE);

            BUILDER.comment("Percentage of target's max HP taken to be dealt as damage by the Prismatic Blade on hit. (Deals x% bonus damage, with 1.00 meaning 100% of target's max HP) [Default: 0.05 (5%)]");
            healthDamagePercent = BUILDER.defineInRange("HealthDamagePercent", 0.05, 0.00, 1.00);

            BUILDER.comment("The max percentage of damage reduction one can achieve using the Prismatic Armor. Damage reduction percentage is increased based on total armor value, each point above diamond granting +5% reduction. Does not require full set of Prismatic Armor to activate. (Reduces incoming damage by x% up to this cap) [Default 25%]");
            armorDamageReduction = BUILDER.defineInRange("ArmorDamageReduction", 25, 0, 100);
        }
        BUILDER.pop();

        BUILDER.push("Enable/Disable Features");
        {
            BUILDER.comment("Are Flatblock/Hexblock/Large Hexblock/Tiles/Large Tiles/Glass blocks destructible from mobs (e.g. Wither destroying blocks) [Default: false]");
            entityDamageableBlocks = BUILDER.define("EntityDamageableBlocks", false);

            BUILDER.comment("Does the Prismatic Chestplate give flight abilities? [Default: true]");
            chestplateFlight = BUILDER.define("ChestplateFlight", true);

            BUILDER.comment("Enable core notification for use with Prismatic Blade MK2. [Default: true]");
            coreNoti = BUILDER.define("CoreNotification", true);
        }
        BUILDER.pop();

        BUILDER.comment("If you don't know what you're doing just leave the values at default.").push("Dev Testing");
        {
            BUILDER.comment("Basic test value to see if config works properly, shouldn't do anything outside of like one random dev test but just in case leave it alone lol [Default: true]");
            testValue = BUILDER.define("TestValue", true);

            BUILDER.comment("Enable or disable placement of indev blocks. These blocks are non-functional and have no recipes currently, but have models and stuff so you could use them for decoration I guess? [Default: true]");
            indevBlocks = BUILDER.define("IndevBlocks", true);
        }
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
