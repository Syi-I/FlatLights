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
    public static final ForgeConfigSpec.ConfigValue<Integer> bleedStacks;
    public static final ForgeConfigSpec.ConfigValue<Double> fobChanceCap;
    public static final ForgeConfigSpec.ConfigValue<Integer> entangledEndDmg;
    public static final ForgeConfigSpec.ConfigValue<Double> pulsingArrowRadius;
    public static final ForgeConfigSpec.ConfigValue<Integer> pulsingPercent;
    public static final ForgeConfigSpec.ConfigValue<Boolean> multilayerReduction;
    public static final ForgeConfigSpec.ConfigValue<Integer> craftingJumpscareChance;

    static {
        BUILDER.push("Stat Balancing");
        {
            BUILDER.comment("How high the enchantment level multipler cap can go to when enchanting the Prismatic Blade with an anvil. (e.g. double the cap = 2, triple the cap = 3, etc.) [Default: 2]");
            enchantMultiplierCap = BUILDER.defineInRange("EnchantmentMultiplierCap", 2, 1, Short.MAX_VALUE);

            BUILDER.comment("Damage cap for the health damage dealt by the Prismatic Blade. (Deals x% bonus damage up to this value) [Default: 100 damage]");
            healthDamageCap = BUILDER.defineInRange("HealthDamageCap", 100, 0, Integer.MAX_VALUE);

            BUILDER.comment("Percentage of target's max HP taken to be dealt as damage by the Prismatic Blade on hit. (Deals x% bonus damage, with 1.00 = 100% of target's max HP) [Default: 0.05 (5%)]");
            healthDamagePercent = BUILDER.defineInRange("HealthDamagePercent", 0.05, 0.00, 1.00);

            BUILDER.comment("Max percentage of damage reduction one can achieve using the Prismatic Armor. Damage reduction percentage is increased based on total armor value, each point above 20 granting +5% reduction ((totalArmorValue - 20) * 0.05). Means that the armor by itself can only achieve up to 50% reduction at most (it totals up to 30 points), but when used with other armor pieces with higher stats the reduction could reach higher values if configured to do so. Does not require full set of Prismatic Armor to activate. (Reduces incoming damage by x% up to this cap) [Default: 25%]");
            armorDamageReduction = BUILDER.defineInRange("ArmorDamageReduction", 25, 0, 100);

            BUILDER.comment("Max percentage of damage the Entangled effect can trigger per hit. (Calculates total dmg% such that entangledDmg = initialDmg * ((EntangledPercent)/100) ) [Default: 100 -> 100% damage]");
            entangledPercent = BUILDER.defineInRange("EntangledPercentDmg", 100, 0, 100);

            BUILDER.comment("Max percentage of damage the Quantum Strike follow up attack can trigger. (Calculates total dmg% such that quantumDmg = baseWeaponDmg * ((QuantumPercent)/100) ) [Default: 100 -> 100% damage]");
            quantumPercent = BUILDER.defineInRange("QuantumPercentDmg", 100, 0, 100);

            BUILDER.comment("Radius that the Entangled effect searches for other entangled mobs. (Radius from the mob being attacked) [Default: 16]");
            entangledRange = BUILDER.defineInRange("EntangledRange", 16.0D, 1.0D, 32.0D);

            BUILDER.comment("Amount of Armor Shred effect stacks allowed from the Bonesaw enchantment. Each stack adds -0.1 to armor MULTIPLY_TOTAL attribute value. (Applies 1 new stack per hit up to this value) [Default: 5]");
            bonesawStacks = BUILDER.defineInRange("BonesawStacks", 5, 1, 10);

            BUILDER.comment("Amount of Bleed effect stacks allowed from the Bleeding Edge enchantment. Each stack deals +3% damage of target's max HP value. (Applies 1 new stack per hit up to this value) [Default: 5]");
            bleedStacks = BUILDER.defineInRange("BleedStacks", 5, 1, 33);

            BUILDER.comment("Max chance that Flash of Brilliance has to activate. (Chance for 10x XP drops, each level adds +5% chance for max of 25% at level 5) [Default: 0.25 -> 25%]");
            fobChanceCap = BUILDER.defineInRange("FobChanceCap", 0.25, 0.001, 1.0);

            BUILDER.comment("Damage cap for the Entangled effect's ending damage. (Based off percentage of target's max HP, each level of Quantum Strike gives 10% more damage for 'dmg = maxHP * 0.1 * level * cap%')  [Default: 100 -> 100%]");
            entangledEndDmg = BUILDER.defineInRange("EntangledEndingDamage", 100, 1, Integer.MAX_VALUE);

            BUILDER.comment("Effect radius for pulsing arrow damage.  [Default: 2]");
            pulsingArrowRadius = BUILDER.defineInRange("PulsingArrowRadius", 2.0, 0.1, 5.0);

            BUILDER.comment("Max percentage of damage the Pulsing Arrows enchant can trigger per hit. (Calculates total dmg% such that pulsingAreaDmg = arrowDmg * (level + 1) * 2 * ((PulsingPercent)/100) ) [Default: 100 -> 100% damage]");
            pulsingPercent = BUILDER.defineInRange("PulsingArrowDamagePercent", 100, 0, 100);
        }
        BUILDER.pop();

        BUILDER.push("Enable/Disable Features");
        {
            BUILDER.comment("Are Flatblock/Hexblock/Large Hexblock/Tiles/Large Tiles/Glass blocks destructible from mobs (e.g. Wither destroying blocks) [Default: false]");
            entityDamageableBlocks = BUILDER.define("EntityDamageableBlocks", false);

            BUILDER.comment("Does the Prismatic Chestplate give flight abilities? [Default: true]");
            chestplateFlight = BUILDER.define("ChestplateFlight", true);

            BUILDER.comment("Does each individual Prismatic Armor piece reduce damage independently or only calculate once if wearing any Prismatic Armor piece? [Default: true]");
            multilayerReduction = BUILDER.define("MultilayerReduction", true);

            BUILDER.comment("Chance for the crafting table jumpscare sound to play. [Default: 1%]");
            craftingJumpscareChance = BUILDER.defineInRange("CraftingTableJumpscareChance", 12, 0, 100);
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
