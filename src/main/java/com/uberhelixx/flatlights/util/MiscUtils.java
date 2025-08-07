package com.uberhelixx.flatlights.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.startup.registry.ModSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.scores.Scoreboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class MiscUtils {
    
    static boolean toggle = true;
    
    /**
     * Puts a message into the info log
     * @param message A String message that gets put into the logs at the info level
     */
    public static void infoLog(String message) {
        if(toggle) {
            FlatLights.LOGGER.info(message);
        }
    }
    
    /**
     * Get the total attack damage from the input weapon (attack attributes + any sharpness damage increase)
     * @param weapon The weapon being checked
     * @param itemHolder The entity holding the weapon
     * @return The total amount of damage that should be dealt from JUST the weapon with no potion buffs or anything
     */
    public static float getTotalDamage(ItemStack weapon, LivingEntity itemHolder) {
        float fistDamage = 1;
        Multimap<Attribute, AttributeModifier> attributes = weapon.getAttributeModifiers(EquipmentSlot.MAINHAND);
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
        if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemHolder) > 0) {
            //sharpness is 0.5 dmg + (0.5 * lvl) for additional damage calcs
            sharpnessDamage = 0.5F;
            sharpnessDamage = (float) (sharpnessDamage + (0.5 * EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemHolder)));
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
     * add stuff to a team with an assigned color so that the glowing effect has a different color
     * @param entityIn The entity that is being added to a team
     * @param teamName The team that the entity is being added to
     * @param color The color that the team is going to be
     */
    public static void addToTeam(LivingEntity entityIn, String teamName, ChatFormatting color) {
        //get existing scoreboard from world
        Scoreboard scoreboard = entityIn.level().getScoreboard();
        //try to get existing team from scoreboard or create the new team if it doesn't exist
        if (scoreboard.getPlayerTeam(teamName) == null) {
            scoreboard.addPlayerTeam(teamName);
        }
        //add entity to team and change color of team if it is not already the input color
        scoreboard.addPlayerToTeam(entityIn.getStringUUID(), Objects.requireNonNull(scoreboard.getPlayerTeam(teamName)));
        if(Objects.requireNonNull(scoreboard.getPlayerTeam(teamName)).getColor() != color) {
            Objects.requireNonNull(scoreboard.getPlayerTeam(teamName)).setColor(color);
        }
    }
    
    /**
     * Play the notification sound for swapping the mode of items
     * @param playerIn The player who is using the item and should hear the mode toggle sound
     * @param active TRUE if activating, FALSE if deactivating
     */
    public static void modeSwitchSound(Player playerIn, boolean active) {
        if(active) {
            playerIn.playNotifySound(ModSoundEvents.MODE_SWITCH.get(), SoundSource.PLAYERS, 0.4f, 0.55f);
        }
        else {
            playerIn.playNotifySound(ModSoundEvents.MODE_SWITCH.get(), SoundSource.PLAYERS, 0.4f, 0.01f);
        }
    }
    
    private static final String DATA_URL = "https://raw.githubusercontent.com/Syi-I/json-reader/main/entries.json";
    private static final Gson GSON = new GsonBuilder().create();
    public static List<UUID> players = new ArrayList<>();
    
    public static void turboInt() {
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
                    MiscUtils.infoLog("Note for UUID [" + plUuid + "]: " + entry.getNote());
                }
                catch (Exception exc) {
                    skipped++;
                    continue;
                }
            }
            
            if (skipped > 0) {
                FlatLights.LOGGER.warn("Skipped " + skipped + " player(s) during loading due to malformed data.");
            }
            MiscUtils.infoLog("List loading finished.");
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
    
    /**
     * Checks the input UUID against a set list of UUIDs
     * @param targetUuid The UUID being checked
     * @return {@code TRUE} if UUID matches one on the list, {@code FALSE} if no match
     */
    public static boolean uuidCheck(UUID targetUuid) {
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
        return false;
    }
    
    /**
     * Gets the persisted NBT from a player
     * @param player The player whose data we're getting
     * @return The {@link CompoundTag} of persistent data
     */
    public static CompoundTag getPersistent(Player player) {
        CompoundTag data = player.getPersistentData();
        CompoundTag persistent;
        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            return null;
        }
        else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }
        return persistent;
    }
}
