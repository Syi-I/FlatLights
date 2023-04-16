package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.*;

public class PrismaticBladeMk2 extends SwordItem {

    public PrismaticBladeMk2(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(0, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        World world = attacker.world;
        if(uuidCheck(attacker.getUniqueID())) {
            target.hurtResistantTime = 0;
            if(stack.getTag().getBoolean("multislash")) {
                int damageBonus = 1;
                if (stack.getTag() != null) {
                    damageBonus = Math.max(stack.getTag().getInt("damageBonus"), 1);
                }
                for(int count = 0; count < stack.getTag().getInt("tier") + 1; count++) {
                    target.attackEntityFrom(DamageSource.GENERIC, damageBonus);
                    world.playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.05f, (1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
                }
            }
            target.hurtResistantTime = 0;
        }
        else {
            //attacker.attackEntityFrom(DamageSource.OUT_OF_WORLD, attacker.getMaxHealth());
            ITextComponent fail = new StringTextComponent("This item does not belong to you.");
            UUID owner = UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a");
            attacker.sendMessage(fail, owner);
            world.playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f,(1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
            target.heal(target.getMaxHealth());
            return false;
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_blademk2_shift"));
            if(stack.getTag() != null && stack.getTag().contains("cores")) {
                tooltip.add(getTierData(stack));
                tooltip.add(getCoreData(stack));
                tooltip.add(getMultiState(stack));
            }
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @SubscribeEvent
    public static void EnchantStack (AnvilUpdateEvent event) {
        if (!event.getPlayer().isServerWorld()) {
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

    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handlePlayerDropsEvent(LivingDropsEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (player.world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) {
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

    public static boolean addToPlayerInventory(PlayerEntity player, ItemStack stack) {
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
    final static int tierMultiplier = 10;
    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void killMobs(LivingDeathEvent event) {
        Entity mob = event.getEntity();
        Entity killer = event.getSource().getTrueSource();
        UUID owner = UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a");
        if(killer instanceof PlayerEntity && mob instanceof LivingEntity) {
            World world = killer.world;
            ItemStack tool = ((PlayerEntity) killer).getHeldItemMainhand();
            if(!tool.hasTag()) {
                CompoundNBT newTag = new CompoundNBT();
                newTag.putInt("cores", 0);
                newTag.putInt("tier", 0);
                newTag.putInt("damageBonus", 0);
                tool.setTag(newTag);
                killer.sendMessage(new StringTextComponent("tried to write new nbt tag"), owner);
            }
            CompoundNBT tag = tool.getTag();
            if (tag == null) {
                return;
            }
            int oldCores = tag.getInt("cores");
            int oldTier = tag.getInt("tier");
            int newCores = oldCores + Math.max((Math.round(((LivingEntity) mob).getMaxHealth() / 20)), 1);
            String coreGainText = " core.";
            if(newCores - oldCores > 1) {
                coreGainText = " cores.";
            }
            ITextComponent killMessage = new StringTextComponent("You have slain a mob and gained " + (newCores - oldCores) + coreGainText);
            killer.sendMessage(killMessage, owner);
            int newTier = oldTier;
            while (newCores > ((oldTier + 1) * tierMultiplier) && newTier < 7) {
                newCores = newCores % ((oldTier + 1) * tierMultiplier);
                newTier++;
                world.playSound(null, killer.getPosX(), killer.getPosY(), killer.getPosZ(), SoundEvents.ENTITY_ENDER_DRAGON_DEATH, SoundCategory.PLAYERS, 0.2f, (1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
                oldTier = newTier;
            }
            tag.putInt("cores", newCores);
            tag.putInt("tier", newTier);
            tag.putInt("damageBonus", newCores);
            tool.setTag(tag);
        }
    }

    public static ITextComponent getCoreData(ItemStack tool) {
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains("cores")) {
            int cores = tag.getInt("cores");
            int tier = tag.getInt("tier") + 1;
            if (cores > 0) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + " [" + "Cores: " + TextFormatting.WHITE + cores + "/" + (tier * tierMultiplier) + "]");
            }
        }
        return data;
    }

    public static ITextComponent getTierData(ItemStack tool) {
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains("cores")) {
            int cores = tag.getInt("cores");
            int tier = tag.getInt("tier") + 1;
            String tierName;
            switch(tier) {
                case 1:
                    tierName = "Dormant";
                    break;
                case 2:
                    tierName = "Awakened";
                    break;
                case 3:
                    tierName = "Ascended";
                    break;
                case 4:
                    tierName = "Transcendent";
                    break;
                case 5:
                    tierName = "Supreme";
                    break;
                case 6:
                    tierName = "Sacred";
                    break;
                case 7:
                    tierName = "Divine";
                    break;
                default:
                    tierName = "Normal";
            }
            if (cores > 0) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + " [" + "Tier: " + TextFormatting.WHITE + tierName + "]");
            }
        }
        return data;
    }

    public static ITextComponent getMultiState(ItemStack tool) {
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains("multislash")) {
            int cores = tag.getInt("cores");
            int tier = tag.getInt("tier") + 1;
            boolean active = tag.getBoolean("multislash");
            String activeState = "Deactivated";
            if(active) {
                activeState = "Activated";
            }
            String tierSlash = tier + " slash of ";
            if(tier > 1) {
                tierSlash = tier + " slashes of ";
            }
            if (active) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + " [" + "Multislash: " + TextFormatting.WHITE + activeState + ", causes " + tierSlash + cores + " damage]");
            }
            else {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.WHITE + " [" + "Multislash: " + TextFormatting.WHITE + activeState + "]");
            }
        }
        return data;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack blade = playerIn.getHeldItem(handIn);
        if(Screen.hasShiftDown()) {
            if (uuidCheck(playerIn.getUniqueID())) {
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.PLAYERS, 0.2f, (1.0f + (worldIn.rand.nextFloat() * 0.2f)) * 0.1f);
                if (blade.getTag() == null) {
                    CompoundNBT newTag = new CompoundNBT();
                    newTag.putBoolean("multislash", true);
                    blade.setTag(newTag);
                } else {
                    CompoundNBT tag = blade.getTag();
                    boolean active = tag.getBoolean("multislash");
                    tag.putBoolean("multislash", !active);
                    blade.setTag(tag);
                }
            }
        }
        return ActionResult.resultPass(blade);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return (stack.isEnchanted() || stack.getTag().getBoolean("multislash"));
    }

    private static boolean uuidCheck(UUID targetUuid) {
        //380df991-f603-344c-a090-369bad2a924a is dev uuid
        if(0 == targetUuid.compareTo(UUID.fromString("380df991-f603-344c-a090-369bad2a924a"))) { return true; }
        if(0 == targetUuid.compareTo(UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a"))) { return true; }
        return false;
    }
}
