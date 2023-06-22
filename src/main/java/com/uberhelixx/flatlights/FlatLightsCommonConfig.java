package com.uberhelixx.flatlights;

import net.minecraftforge.common.ForgeConfigSpec;

public final class FlatLightsCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> enchantMultiplierCap;
    public static final ForgeConfigSpec.ConfigValue<Integer> healthDamageCap;
    public static final ForgeConfigSpec.ConfigValue<Double> healthDamagePercent;
    public static final ForgeConfigSpec.ConfigValue<Integer> armorDamageReduction;
    public static final ForgeConfigSpec.ConfigValue<Boolean> chestplateFlight;
    public static final ForgeConfigSpec.ConfigValue<Boolean> entityDamageableBlocks;
    public static final ForgeConfigSpec.ConfigValue<Boolean> indevBlocks;
    public static final ForgeConfigSpec.ConfigValue<Integer> entangledPercent;
    public static final ForgeConfigSpec.ConfigValue<Integer> quantumPercent;
    public static final ForgeConfigSpec.ConfigValue<Double> entangledRange;
    public static final ForgeConfigSpec.ConfigValue<Integer> bonesawStacks;


    static {
        BUILDER.push("Stat Balancing");
        {
            BUILDER.comment("How high the enchantment level multipler cap can go to when enchanting the Prismatic Blade with an anvil. (e.g. double the cap = 2, triple the cap = 3, etc.) [Default: 2]");
            enchantMultiplierCap = BUILDER.defineInRange("EnchantmentMultiplierCap", 2, 1, Short.MAX_VALUE);

            BUILDER.comment("Damage cap for the health damage dealt by the Prismatic Blade. (Deals x% bonus damage up to this value) [Default: 100 damage]");
            healthDamageCap = BUILDER.defineInRange("HealthDamageCap", 100, 0, Integer.MAX_VALUE);

            BUILDER.comment("Percentage of target's max HP taken to be dealt as damage by the Prismatic Blade on hit. (Deals x% bonus damage, with 1.00 = 100% of target's max HP) [Default: 0.05 (5%)]");
            healthDamagePercent = BUILDER.defineInRange("HealthDamagePercent", 0.05, 0.00, 1.00);

            BUILDER.comment("Max percentage of damage reduction one can achieve using the Prismatic Armor. Damage reduction percentage is increased based on total armor value, each point above diamond granting +5% reduction ((totalArmorValue - 20) * 0.05). Does not require full set of Prismatic Armor to activate. (Reduces incoming damage by x% up to this cap) [Default 25%]");
            armorDamageReduction = BUILDER.defineInRange("ArmorDamageReduction", 25, 0, 100);

            BUILDER.comment("Max percentage of damage the Entangled effect can trigger per hit. (Calculates total dmg% such that entangledDmg = initialDmg * ((100 - EntangledPercent)/100)) [Default: 0 -> 100% damage]");
            entangledPercent = BUILDER.defineInRange("EntangledPercentDmg", 0, 0, 99);

            BUILDER.comment("Max percentage of damage the Quantum Strike follow up attack can trigger. (Calculates total dmg% such that quantumDmg = baseWeaponDmg * ((100 - QuantumPercent)/100)) [Default: 0 -> 100% damage]");
            quantumPercent = BUILDER.defineInRange("QuantumPercentDmg", 0, 0, 99);

            BUILDER.comment("Radius that the Entangled effect searches for other entangled mobs. (Radius from the mob being attacked) [Default: 16]");
            entangledRange = BUILDER.defineInRange("EntangledRange", 16.0D, 1.0D, 32.0D);

            BUILDER.comment("Amount of Armor Shred effect stacks allowed from the Bonesaw enchantment. Each stack adds -0.1 to armor MULTIPLY_TOTAL attribute value. (Applies 1 new stack per hit up to this value) [Default: 5]");
            bonesawStacks = BUILDER.defineInRange("BonesawStacks", 5, 1, 10);
        }
        BUILDER.pop();

        BUILDER.push("Enable/Disable Features");
        {
            BUILDER.comment("Are Flatblock/Hexblock/Large Hexblock/Tiles/Large Tiles/Glass blocks destructible from mobs (e.g. Wither destroying blocks) [Default: false]");
            entityDamageableBlocks = BUILDER.define("EntityDamageableBlocks", false);

            BUILDER.comment("Does the Prismatic Chestplate give flight abilities? [Default: true]");
            chestplateFlight = BUILDER.define("ChestplateFlight", true);
        }
        BUILDER.pop();

        BUILDER.comment("If you don't know what you're doing just leave the values at default.").push("Dev Testing");
        {
            BUILDER.comment("Enable or disable placement of indev blocks. These blocks are non-functional and have no recipes currently, but have models and stuff so you could use them for decoration I guess? [Default: true]");
            indevBlocks = BUILDER.define("IndevBlocks", true);
        }
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
