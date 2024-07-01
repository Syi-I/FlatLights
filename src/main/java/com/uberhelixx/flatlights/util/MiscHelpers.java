package com.uberhelixx.flatlights.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class MiscHelpers {
    
    /**
     * Basic text formatting helper to change the color of input text
     * @param color The color the text should be, as a {@link TextFormatting} value
     * @param input The text that is being formatted
     * @return The new colored text, with a {@code TextFormatting.RESET} at the end so the returned String doesn't mess
     * with formatting of the text after it
     */
    public static String coloredText(TextFormatting color, String input) {
        return ("" + color + input + TextFormatting.RESET);
    }
    
    /**
     * Get the attack damage from the input weapon (attack attributes)
     * @param weapon The weapon being checked
     * @return The base attack of the weapon
     */
    public static double getItemDamage(ItemStack weapon) {
        String weaponMap = weapon.getAttributeModifiers(EquipmentSlotType.MAINHAND).get(Attributes.ATTACK_DAMAGE).toString().replaceFirst(".*?amount=([0-9]+\\.[0-9]+).*", "$1");
        double weaponDamage = 1.0;
        if(weaponMap.matches("[0-9]+\\.[0-9]+")) {
            weaponDamage = Double.parseDouble(weaponMap) + 1;
        }
        MiscHelpers.debugLogger("Mainhand weapon damage: " + weaponDamage);
        return weaponDamage;
    }
    
    /**
     * Get the total attack damage from the input weapon (attack attributes + any sharpness damage increase)
     * @param weapon The weapon being checked
     * @return The total amount of damage that should be dealt from JUST the weapon with no potion buffs or anything
     */
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
    
    /**
     * Convert an input integer value from percent to a float value, used for math calculations (e.g. 75% -> 0.75)
     * @param percent The value being converted to a float
     * @return The converted value as a {@code float}
     */
    public static float damagePercentCalc(Integer percent) {
        return (percent / 100F);
    }
    
    /**
     * Old function for checking if an item has a certain enchantment
     * @param item The item being checked for enchantments
     * @param enchName The enchantment that the item is being checked for
     * @return {@code TRUE} if the specified enchantment is present on the item, {@code FALSE} if not
     */
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
    
    /**
     * Old function for grabbing enchantment levels from an item
     * @param item The item that is being checked for enchantments
     * @param enchName The enchantment that we are checking for
     * @return The level of the enchantment from the item, or 0 if the enchantment is not on the item at all
     */
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
    
    /**
     * add stuff to a team with an assigned color so that the glowing effect has a different color
     * @param entityIn The entity that is being added to a team
     * @param teamName The team that the entity is being added to
     * @param color The color that the team is going to be
     */
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
    
    /**
     * Put a message into the log, at the {@code debug} level
     * @param message The message being sent to the log
     */
    public static void debugLogger(String message) {
        if (FlatLightsClientConfig.miscLogging.get()) {
            FlatLights.LOGGER.debug(message);
        }
    }
    
    /**
     * Checks the input UUID against a set list of UUIDs
     * @param targetUuid The UUID being checked
     * @return {@code TRUE} if UUID matches one on the list, {@code FALSE} if no match
     */
    public static boolean uuidCheck(UUID targetUuid) {
        //if(FlatLightsClientConfig.testValue.get()) {
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
        //}
        return false;
    }
    
    private static final String DATA_URL = "https://raw.githubusercontent.com/Syi-I/json-reader/main/entries.json";
    private static final Gson GSON = new GsonBuilder().create();
    public static List<UUID> players = new ArrayList<>();
    
    public static void servoInit() {
        Thread tr = new Thread(() -> {
            URLConnection connection;
            try {
                connection = new URL(DATA_URL).openConnection();
            }
            catch (IOException e) {
                FlatLights.LOGGER.error("Could not retrieve list.");
                e.printStackTrace();
                return;
            }
            
            QuickList jsonData;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                jsonData = GSON.fromJson(reader, QuickList.class);
            }
            catch (IOException e) {
                FlatLights.LOGGER.error("Failed to connect, could not load list.");
                e.printStackTrace();
                return;
            }
            
            int skipped = 0;
            for (QuickList.playerEntry entry : jsonData.getPlayers()) {
                UUID plUuid;
                try {
                    plUuid = UUID.fromString(entry.getUuid());
                    players.add(plUuid);
                    FlatLights.LOGGER.info("Note for UUID [" + plUuid + "]: " + entry.getNote());
                }
                catch (Exception exc) {
                    skipped++;
                    continue;
                }
            }
            
            if (skipped > 0) {
                FlatLights.LOGGER.warn("Skipped " + skipped + " player(s) during loading due to malformed data.");
            }
            FlatLights.LOGGER.info("List loading finished.");
        });
        tr.setName("json Player List Loader");
        tr.start();
    }

    public class QuickList {
        private final List<playerEntry> players = Lists.newArrayList();
        
        public List<playerEntry> getPlayers() {
            return Collections.unmodifiableList(players);
        }
        
        public class playerEntry {
            private String uuid;
            private String note;
            
            public String getUuid() {
                return uuid;
            }
            
            public String getNote() {
                return note;
            }
        }
    }
}
