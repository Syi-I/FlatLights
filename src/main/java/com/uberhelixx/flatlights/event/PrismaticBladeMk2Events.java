package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.uberhelixx.flatlights.item.tools.PrismaticBladeMk2.*;
import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class PrismaticBladeMk2Events {
    //makes it so you can't drop the item when it belongs to you (it becomes invincible and you immediately pick it up)
    @SubscribeEvent
    public static void droppedItem(ItemTossEvent event) {
        ItemEntity itemDrop = event.getEntityItem();
        ItemStack item = itemDrop.getItem();
        PlayerEntity player = event.getPlayer();
        if(!(item.getItem() instanceof PrismaticBladeMk2)) {
            return;
        }
        if(uuidCheck(player.getUniqueID())) {
            itemDrop.setNoPickupDelay();
        }
        itemDrop.setNoDespawn();
        itemDrop.setInvulnerable(true);
    }


    //check and set whether this is the player's first time joining and receiving the blade if appropriate
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity playerIn = event.getPlayer();
        if(!uuidCheck(playerIn.getUniqueID())) {
            return;
        }
        CompoundNBT data = event.getPlayer().getPersistentData();
        CompoundNBT persistent;
        //check for player nbt tag and give/set if not present or false
        if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            data.put(PlayerEntity.PERSISTED_NBT_TAG, persistent = new CompoundNBT());
        }
        else {
            persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }
        //check if they have blade or not and if they should be given the blade, then set player tag
        if(persistent.contains(FIRSTJOIN_TAG)) {
            boolean alreadyHave = false;
            for (int i = 0; i < playerIn.inventory.mainInventory.size(); ++i) {
                ItemStack stack = playerIn.inventory.mainInventory.get(i);
                if (stack.getItem() instanceof PrismaticBladeMk2) {
                    alreadyHave = true;
                }
            }
            persistent.putBoolean(FIRSTJOIN_TAG, alreadyHave);
        }
        if (!persistent.contains(FIRSTJOIN_TAG) || !persistent.getBoolean(FIRSTJOIN_TAG)) {
            persistent.putBoolean(FIRSTJOIN_TAG, true);
            playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.PRISMATIC_BLADEMK2.get()));
        }
    }

    //have to get original player inventory to add item back after death
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handlePlayerCloneEvent(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        PlayerEntity player = event.getPlayer();
        PlayerEntity oldPlayer = event.getOriginal();
        if (player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
            return;
        }
        for (int i = 0; i < oldPlayer.inventory.mainInventory.size(); ++i) {
            ItemStack stack = oldPlayer.inventory.mainInventory.get(i);
            if (addToPlayerInventory(player, stack)) {
                oldPlayer.inventory.mainInventory.set(i, ItemStack.EMPTY);
            }
        }
    }

    //event to add this item back to inventory after death drops the items
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handlePlayerDropsEvent(LivingDropsEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
                return;
            }
            if(!uuidCheck(player.getUniqueID())) {
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

    //function to actually find and add specific items to the player inventory
    private static boolean addToPlayerInventory(PlayerEntity player, ItemStack stack) {
        if (stack.isEmpty() || player == null) {
            return false;
        }
        PlayerInventory inv = player.inventory;
        for (int i = 0; i < inv.mainInventory.size(); ++i) {
            if (inv.mainInventory.get(i).isEmpty()) {
                inv.mainInventory.set(i, stack.copy());
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void EnchantStack (AnvilUpdateEvent event) {
        if (!Objects.requireNonNull(event.getPlayer()).isServerWorld()) {
            return;
        }

        ItemStack prismaticBlade = event.getLeft();
        ItemStack enchantedBook = event.getRight();

        if (prismaticBlade == null || prismaticBlade.getItem() != ModItems.PRISMATIC_BLADEMK2.get() || enchantedBook == null || enchantedBook.getItem() != Items.ENCHANTED_BOOK) {
            return;
        }

        Map<Enchantment, Integer> swordMap = EnchantmentHelper.getEnchantments(prismaticBlade);
        Map<Enchantment, Integer> bookMap = EnchantmentHelper.getEnchantments(enchantedBook);

        if (bookMap.isEmpty()) {
            return;
        }

        Map<Enchantment, Integer> outputMap = new HashMap<>(swordMap);
        int costCounter = 0;

        for (Map.Entry<Enchantment, Integer> entry : bookMap.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment == null) {
                continue;
            }
            Integer currentValue = swordMap.get(entry.getKey());
            Integer addValue = entry.getValue();
            if (currentValue == null) {
                outputMap.put(entry.getKey(), addValue);
                costCounter += addValue;
            } else if(uuidCheck(event.getPlayer().getUniqueID())) {
                int value = Math.min(currentValue + addValue, 32767);
                outputMap.put(entry.getKey(), value);
                costCounter += ((currentValue + addValue) / 2);
            }
        }

        event.setCost(costCounter);
        ItemStack enchantedBlade = prismaticBlade.copy();
        EnchantmentHelper.setEnchantments(outputMap, enchantedBlade);
        event.setOutput(enchantedBlade);
    }


    //do all core and tier math after killing entities
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void killMobs(LivingDeathEvent event) {
        Entity mob = event.getEntity();
        Entity killer = event.getSource().getTrueSource();
        if(killer instanceof PlayerEntity && mob instanceof LivingEntity) {
            World world = killer.world;
            ItemStack tool = ((PlayerEntity) killer).getHeldItemMainhand();

            //make sure item is PrismaticBladeMk2
            if(!(tool.getItem() instanceof PrismaticBladeMk2)) {
                return;
            }

            //check if nbt tags are already present, otherwise add tags
            if(!tool.hasTag()) {
                CompoundNBT newTag = new CompoundNBT();
                newTag.putInt(CORES_TAG, 0);
                newTag.putInt(TIER_TAG, 0);
                newTag.putInt(DAMAGE_BONUS_TAG, 0);
                tool.setTag(newTag);
                PacketHandler.sendToServer(new PacketWriteNbt(newTag, tool));
            }
            CompoundNBT tag = tool.getTag();
            if (tag == null) {
                return;
            }

            //grab nbt data for cores, tier to calculate new totals
            int oldCores = tag.getInt(CORES_TAG);
            int oldTier = tag.getInt(TIER_TAG);
            int newCores = oldCores + Math.max((Math.round(((LivingEntity) mob).getMaxHealth() / 20)), 1);
            tag.putInt(TOTAL_BONUS_TAG, newCores);

            //kill text notification formatting stuff
            String coreGainText = " core.";
            if(newCores - oldCores > 1) {
                coreGainText = " cores.";
            }
            ITextComponent killMessage = new StringTextComponent("You have slain a creature and gained " + (newCores - oldCores) + coreGainText);
            if(FlatLightsClientConfig.coreNoti.get()) {
                killer.sendMessage(killMessage, messageOwner);
            }

            //math for calculating if tier levels up when adding cores after a kill
            int newTier = oldTier;
            while (newCores > ((oldTier + 1) * TIER_MULTIPLIER) && (newTier + 1) < 7) {
                newCores = newCores % ((oldTier + 1) * TIER_MULTIPLIER);
                newTier++;
                world.playSound(null, killer.getPosX(), killer.getPosY(), killer.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_DEATH, SoundCategory.PLAYERS, 0.2f, (1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
                oldTier = newTier;
            }

            //update nbt data tags
            tag.putInt(CORES_TAG, newCores);
            tag.putInt(TIER_TAG, newTier);
            tag.putInt(DAMAGE_BONUS_TAG, newCores);
            tool.setTag(tag);
            PacketHandler.sendToServer(new PacketWriteNbt(tag, tool));
        }
    }
}
