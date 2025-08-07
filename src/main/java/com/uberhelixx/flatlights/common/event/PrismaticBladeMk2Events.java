package com.uberhelixx.flatlights.common.event;

import com.uberhelixx.flatlights.FlatLights;
import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.common.item.ModItems;
import com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.uberhelixx.flatlights.common.item.tools.PrismaticBladeMk2.*;
import static com.uberhelixx.flatlights.util.lib.LibTagKeys.*;

@Mod.EventBusSubscriber(modid = FlatLights.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PrismaticBladeMk2Events {
    //makes it so you can't drop the item when it belongs to you (it becomes invincible and you immediately pick it up)
    @SubscribeEvent
    public static void droppedItem(ItemTossEvent event) {
        ItemEntity itemDrop = event.getEntity();
        ItemStack item = itemDrop.getItem();
        Player player = event.getPlayer();
        if(!(item.getItem() instanceof PrismaticBladeMk2)) {
            return;
        }
        if(MiscUtils.uuidCheck(player.getUUID())) {
            itemDrop.setNoPickUpDelay();
        }
        itemDrop.setUnlimitedLifetime();
        itemDrop.setInvulnerable(true);
    }
    
    
    //check and set whether this is the player's first time joining and receiving the blade if appropriate
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player playerIn = event.getEntity();
        if(!MiscUtils.uuidCheck(playerIn.getUUID())) {
            return;
        }
        CompoundTag data = playerIn.getPersistentData();
        CompoundTag persistent;
        //check for player nbt tag and give/set if not present or false
        if (!data.contains(Player.PERSISTED_NBT_TAG)) {
            data.put(Player.PERSISTED_NBT_TAG, persistent = new CompoundTag());
        }
        else {
            persistent = data.getCompound(Player.PERSISTED_NBT_TAG);
        }
        //check if it is not the first time the player joined this world, then check if blade is already present in inventory
        if(persistent.contains(HAS_SWORD_TAG)) {
            boolean alreadyHave = false;
            for (int i = 0; i < playerIn.getInventory().items.size(); ++i) {
                ItemStack stack = playerIn.getInventory().items.get(i);
                if (stack.getItem() instanceof PrismaticBladeMk2) {
                    //has prismatic blade mk2 in inventory, no need to give a new one
                    alreadyHave = true;
                }
            }
            //if already have blade, no first join so won't give blade again
            persistent.putBoolean(HAS_SWORD_TAG, alreadyHave);
        }
        //if not considered first join (firstjoin = false), then set to true and give blade to player
        if (!persistent.contains(HAS_SWORD_TAG) || !persistent.getBoolean(HAS_SWORD_TAG)) {
            persistent.putBoolean(HAS_SWORD_TAG, true);
            playerIn.getInventory().add(new ItemStack(ModItems.PRISMATIC_BLADEMK2.get()));
        }
        //if no core tracker stat tag in data, add the tag
        if(!persistent.contains(PLAYER_CORETRACKER_TAG)) {
            persistent.putInt(PLAYER_CORETRACKER_TAG, 0);
        }
    }
    
    //have to get original player inventory to add item back after death
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handlePlayerCloneEvent(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        Player player = event.getEntity();
        Player oldPlayer = event.getOriginal();
        if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
            return;
        }
        for (int i = 0; i < oldPlayer.getInventory().items.size(); ++i) {
            ItemStack stack = oldPlayer.getInventory().items.get(i);
            if(stack.getItem() instanceof PrismaticBladeMk2) {
                if (addToPlayerInventory(player, stack)) {
                    oldPlayer.getInventory().items.set(i, ItemStack.EMPTY);
                }
            }
        }
    }
    
    //event to add this item back to inventory after death drops the items
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handlePlayerDropsEvent(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
                return;
            }
            if(!MiscUtils.uuidCheck(player.getUUID())) {
                return;
            }
            Iterator<ItemEntity> iter = event.getDrops().iterator();
            while (iter.hasNext()) {
                ItemStack stack = iter.next().getItem();
                if (stack.getItem() instanceof PrismaticBladeMk2) {
                    if (addToPlayerInventory(player, stack)) {
                        iter.remove();
                    }
                }
            }
        }
    }
    
    /**
     * function to actually find and add specific items to the player inventory
     * @param player The player whose inventory is being checked for items to copy
     * @param stack The {@link ItemStack} being copied
     * @return TRUE if item is added to the player inventory, FALSE if no item is added or if player or stack is empty
     */
    private static boolean addToPlayerInventory(Player player, ItemStack stack) {
        if (stack.isEmpty() || player == null) {
            return false;
        }
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.items.size(); ++i) {
            if (inv.items.get(i).isEmpty()) {
                inv.items.set(i, stack.copy());
                return true;
            }
        }
        return false;
    }
    
    //do all core and tier math after killing entities
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void killMobs(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        Entity killer = event.getSource().getEntity();
        if(killer instanceof Player player && entity instanceof LivingEntity mob) {
            Level level = player.level();
            ItemStack tool = player.getMainHandItem();
            
            //make sure item is PrismaticBladeMk2
            if(!(tool.getItem() instanceof PrismaticBladeMk2)) {
                return;
            }
            
            //check if nbt tags are already present, otherwise add tags
            CompoundTag tag = tool.getOrCreateTag();
            if(tag.isEmpty() || !PrismaticBladeMk2.hasBladeTags(tag)) {
                tag = PrismaticBladeMk2.putFreshTags(tag);
                tool.setTag(tag);
                if(player.level().isClientSide()) {
                    PacketHandler.sendToServer(new PacketWriteNbt(tag, tool));
                }
            }
            
            //grab nbt data for cores, tier to calculate new totals
            int oldCurrCores = tag.getInt(CURR_CORES_TAG);
            int oldCurrTier = tag.getInt(TIER_TAG);
            int totalCores = tag.getInt(TOTAL_CORES_TAG);
            
            //gained cores is equal to how many times more HP the mob had compared to the player's base 20 HP
            int gainedCores = Math.max((Math.round(mob.getMaxHealth() / 20)), 1);
            //updated current core count, adding previous amount of cores and cores gained from the mob killed
            int newCurrCores = oldCurrCores + gainedCores;
            
            //double check total core count on weapon with total core count from player data tracker (always tries to get the highest total)
            //calculate total cores on blade in case something is wrong
            int bladeIter = oldCurrTier;
            int coreCheckCount = newCurrCores;
            
            MiscUtils.infoLog("[core math] gainedCores = " + gainedCores);
            MiscUtils.infoLog("[core math] oldCurrCores = " + oldCurrCores);
            MiscUtils.infoLog("[core math] newCurrCores = " + newCurrCores);
            //adds the core counts of previously filled tiers onto our freshly updated current core count
            while(bladeIter > 1) {
                bladeIter--;
                coreCheckCount = coreCheckCount + (bladeIter * TIER_MULTIPLIER);
            }
            MiscUtils.infoLog("[core math] old totalCores = " + totalCores);
            MiscUtils.infoLog("[core math] coreCheckCount = " + coreCheckCount);
            
            //if our total cores is not the same as the check, set total to the calculated check amount
            if(totalCores != coreCheckCount) {
                totalCores = coreCheckCount;
            }

            int playerTracker = 0;
            //check if this player has the playerTracker cap or not
            if(PrismaticBladeMk2.hasCoreTracker(player)) {
                playerTracker = PrismaticBladeMk2.getPlayerCores(player);
                MiscUtils.infoLog("[core math] playerCoreCount = " + playerTracker);
                //if player tracker is less than total cores from blade NBT, set player tracker to count from blade NBT
                if(playerTracker < totalCores) {
                    MiscUtils.infoLog("[core math] playerTracker <= totalCores");
                    PrismaticBladeMk2.setPlayerCores(player, totalCores);
                }
                //if total cores from blade NBT is less than player tracker, add difference between totals to newCurrCores to update blade NBT
                else {
                    MiscUtils.infoLog("[core math] playerTracker > totalCores");
                    int coreDiff = playerTracker - totalCores;
                    //update gained cores to include any core difference between player and blade trackers
                    gainedCores = gainedCores + coreDiff;
                    newCurrCores = oldCurrCores + gainedCores;
                    MiscUtils.infoLog("[core math] coreDiff = " + coreDiff);
                    MiscUtils.infoLog("[core math] gainedCores = " + gainedCores);
                    MiscUtils.infoLog("[core math] newCurrCores = " + newCurrCores);
                }
            }
            
            //kill text notification formatting stuff
            String coreGainText = " core.";
            if(newCurrCores - oldCurrCores > 1) {
                coreGainText = " cores.";
            }
            Component mobName = mob.getName();
            Component killMessage = Component.literal("You have slain a " + mobName + " and gained ")
                    .append(Component.literal(String.valueOf(newCurrCores - oldCurrCores)).withStyle(ChatFormatting.LIGHT_PURPLE))
                    .append(Component.literal(coreGainText));
            if(FlatLightsClientConfig.coreNoti.get()) {
                if(player.level().isClientSide()) {
                    player.displayClientMessage(killMessage, true);
                }
            }
            
            //math for calculating if tier levels up when adding cores after a kill
            int newTier = oldCurrTier;
            //new current core count > total for the tier, is next tier up greater than the total tier cap
            while(newCurrCores > (oldCurrTier * TIER_MULTIPLIER) && newTier < TOTAL_TIERS) {
                //get left over cores after promoting to next tier
                newCurrCores = newCurrCores - (oldCurrTier * TIER_MULTIPLIER);
                newTier++;
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_DRAGON_DEATH, SoundSource.PLAYERS, 0.2f, (1.0f + (level.getRandom().nextFloat() * 0.3f)) * 0.99f);
                oldCurrTier = newTier;
            }
            
            MiscUtils.infoLog("[core math END] totalCores + gainedCores = " + (totalCores));
            MiscUtils.infoLog("[core math END] newCurrCores = " + newCurrCores);
            
            //update nbt data of sword
            tag.putInt(TOTAL_CORES_TAG, totalCores);
            tag.putInt(CURR_CORES_TAG, newCurrCores);
            tag.putInt(TIER_TAG, newTier);
            tool.setTag(tag);
            if(player.level().isClientSide()) {
                PacketHandler.sendToServer(new PacketWriteNbt(tag, tool));
            }
        }
    }
}
