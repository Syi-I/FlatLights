package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.*;

import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class PrismaticBladeMk2 extends SwordItem {

    public PrismaticBladeMk2(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    static UUID messageOwner = UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a");
    final static int tierMultiplier = 1000;

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return (stack.isEnchanted() || (stack.getTag() != null && stack.getTag().getBoolean("multislash")));
    }

    @Override
    public boolean isImmuneToFire() { return true; }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if(!uuidCheck(entityIn.getUniqueID())) {
            if(!(entityIn instanceof LivingEntity)) {
                return;
            }
            entityIn.attackEntityFrom(DamageSource.OUT_OF_WORLD, ((LivingEntity) entityIn).getMaxHealth() / 5);
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(0, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        World world = attacker.world;
        if(uuidCheck(attacker.getUniqueID())) {
            target.hurtResistantTime = 0;
            if (stack.getTag() != null) {
                int damageBonus = 1;
                int count = stack.getTag().getInt("tier") + 1;
                if(stack.getTag().getBoolean("multislash")) {
                    damageBonus = Math.max(stack.getTag().getInt("damageBonus"), 1);
                }
                if(stack.getTag().getBoolean("multislash")) {
                    doSlash(world, target, damageBonus, count);
                }
            }
            target.hurtResistantTime = 0;
        }
        else {
            ITextComponent fail = new StringTextComponent("This item does not belong to you.");
            attacker.sendMessage(fail, messageOwner);
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
                if(stack.getTag().contains("multislash")) {
                    tooltip.add(getMultiState(stack));
                }
            }
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
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

    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void killMobs(LivingDeathEvent event) {
        Entity mob = event.getEntity();
        Entity killer = event.getSource().getTrueSource();
        if(killer instanceof PlayerEntity && mob instanceof LivingEntity) {
            World world = killer.world;
            ItemStack tool = ((PlayerEntity) killer).getHeldItemMainhand();
            if(!(tool.getItem() instanceof PrismaticBladeMk2)) {
                return;
            }
            if(!tool.hasTag()) {
                CompoundNBT newTag = new CompoundNBT();
                newTag.putInt("cores", 0);
                newTag.putInt("tier", 0);
                newTag.putInt("damageBonus", 0);
                tool.setTag(newTag);
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
            ITextComponent killMessage = new StringTextComponent("You have slain a creature and gained " + (newCores - oldCores) + coreGainText);
            if(FlatLightsClientConfig.coreNoti.get()) {
                killer.sendMessage(killMessage, messageOwner);
            }
            int newTier = oldTier;
            while (newCores > ((oldTier + 1) * tierMultiplier) && (newTier + 1) < 7) {
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
            String totalCoresForLevelup = "/" + (tier * tierMultiplier);
            String coresText = "" + TextFormatting.RED + cores;
            if(tier == 7) {
                totalCoresForLevelup = "";
            }
            if(cores >= (tier * tierMultiplier) && tier < 7) {
                coresText = "" + TextFormatting.GREEN + cores;
            }
            if (cores > 0) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Cores: " + coresText + TextFormatting.WHITE + totalCoresForLevelup + TextFormatting.AQUA + "]");
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
                    tierName = TextFormatting.GRAY + "Dormant";
                    break;
                case 2:
                    tierName = TextFormatting.WHITE + "Awakened";
                    break;
                case 3:
                    tierName = TextFormatting.YELLOW + "Ascended";
                    break;
                case 4:
                    tierName = TextFormatting.GOLD + "Exalted";
                    break;
                case 5:
                    tierName = TextFormatting.RED + "Sacred";
                    break;
                case 6:
                    tierName = TextFormatting.LIGHT_PURPLE + "Divine";
                    break;
                case 7:
                    tierName = TextFormatting.DARK_PURPLE + "Primordial";
                    break;
                default:
                    tierName = "" + TextFormatting.BLACK + TextFormatting.OBFUSCATED + "Unknown";
            }
            if (cores > 0) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Tier: " + tierName + TextFormatting.AQUA + "]");
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
            String activeState = TextFormatting.DARK_RED + "Deactivated";
            if(active) {
                activeState = TextFormatting.GREEN + "Activated";
            }
            String tierSlash = "" + TextFormatting.RED + tier + TextFormatting.WHITE + " slash of ";
            if(tier > 1) {
                tierSlash = "" + TextFormatting.RED + tier + TextFormatting.WHITE + " slashes of ";
            }
            if (active) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Multislash: " + activeState + TextFormatting.WHITE + " | " + tierSlash + TextFormatting.RED + cores + TextFormatting.WHITE + " damage" + TextFormatting.AQUA + "]");
            }
            else {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Multislash: " + activeState + TextFormatting.AQUA + "]");
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

    private static void doSlash(World worldIn, LivingEntity targetIn, int damageBonusIn, int slashCount) {
        targetIn.hurtResistantTime = 0;
        targetIn.attackEntityFrom(DamageSource.GENERIC, damageBonusIn);
        targetIn.hurtResistantTime = 0;
        worldIn.playSound(null, targetIn.getPosX(), targetIn.getPosY(), targetIn.getPosZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.1f, (1.0f + (worldIn.rand.nextFloat() * 0.3f)) * 0.99f);
        if((slashCount - 1) > 0) {
            doSlash(worldIn, targetIn, damageBonusIn, slashCount - 1);
        }
    }

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

    private static final String NBT_KEY = "flatlights.firstjoin";

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity playerIn = event.getPlayer();
        if(!uuidCheck(playerIn.getUniqueID())) {
            return;
        }
        CompoundNBT data = event.getPlayer().getPersistentData();
        CompoundNBT persistent;
        if (!data.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
            data.put(PlayerEntity.PERSISTED_NBT_TAG, persistent = new CompoundNBT());
        }
        else {
            persistent = data.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        }
        if(persistent.contains(NBT_KEY)) {
            boolean alreadyHave = false;
            for (int i = 0; i < playerIn.inventory.mainInventory.size(); ++i) {
                ItemStack stack = playerIn.inventory.mainInventory.get(i);
                if (stack.getItem() instanceof PrismaticBladeMk2) {
                    alreadyHave = true;
                }
            }
            persistent.putBoolean(NBT_KEY, alreadyHave);
        }
        if (!persistent.contains(NBT_KEY) || !persistent.getBoolean(NBT_KEY)) {
            persistent.putBoolean(NBT_KEY, true);
            playerIn.inventory.addItemStackToInventory(new ItemStack(ModItems.PRISMATIC_BLADEMK2.get()));
        }
    }
}
