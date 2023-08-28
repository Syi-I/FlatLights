package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.entity.VoidProjectileEntity;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.ModSoundEvents;
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
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class PrismaticBladeMk2 extends SwordItem {

    protected static final String FIRSTJOIN_TAG = "flatlights.firstjoin"; //used to check if dedicated player has been given the starter blade yet
    public static final String DAMAGE_MODE_TAG = "flatlights.megaton"; //bonus melee damage mode
    public static final String PROJECTILE_MODE_TAG = "flatlights.projectile"; //projectile shooting mode
    public static final String TIER_TAG = "flatlights.tier"; //current tier of the blade
    public static final String DAMAGE_BONUS_TAG = "flatlights.damageBonus"; //current damage = to current number of cores before next tier increase
    public static final String CORES_TAG = "flatlights.cores"; //current number of cores
    public static final String TOTAL_BONUS_TAG = "flatlights.totalBonus"; //total damage bonus = to total number of cores gained
    protected static final int TIER_MULTIPLIER = 1000; //cores needed per tier to increase tier
    public static final int TOTAL_TIERS = 7; //total tiers available

    public PrismaticBladeMk2(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    static UUID messageOwner = UUID.fromString("fabd0a49-3695-401c-9990-d95464632a6a");

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return true; }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return (stack.isEnchanted() || (stack.getTag() != null && (stack.getTag().getBoolean(DAMAGE_MODE_TAG) || stack.getTag().getBoolean(PROJECTILE_MODE_TAG))));
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
                int tier = stack.getTag().getInt(TIER_TAG) + 1;
                //grab total damage, or leave at 1 if no bonus yet
                if(stack.getTag().getBoolean(DAMAGE_MODE_TAG)) {
                    damageBonus = Math.max(stack.getTag().getInt(TOTAL_BONUS_TAG), 1);
                }
                if(stack.getTag().getBoolean(DAMAGE_MODE_TAG)) {
                    doSlash(world, target, attacker, damageBonus, tier);
                }
            }
            target.hurtResistantTime = 0;
        }
        else {
            ITextComponent fail = new StringTextComponent("This item does not belong to you.");
            attacker.sendMessage(fail, messageOwner);
            world.playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), ModSoundEvents.SQUEAK.get(), SoundCategory.PLAYERS, 0.5f, (1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
            target.heal(target.getMaxHealth());
            return false;
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_blademk2_shift"));
            if(stack.getTag() != null && stack.getTag().contains(CORES_TAG)) {
                tooltip.add(getTierData(stack));
                tooltip.add(getCoreData(stack));
                if(stack.getTag().contains(DAMAGE_MODE_TAG) || stack.getTag().contains(PROJECTILE_MODE_TAG)) {
                    tooltip.add(getSwordState(stack));
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
        }
    }

    //core count tooltip formatting
    private static ITextComponent getCoreData(ItemStack tool) {
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains(CORES_TAG)) {
            int cores = tag.getInt(CORES_TAG);
            int tier = tag.getInt(TIER_TAG) + 1;
            String totalCoresForLevelup = "/" + (tier * TIER_MULTIPLIER);
            String coresText = "" + TextFormatting.RED + cores;
            if(tier >= 7) {
                totalCoresForLevelup = "";
            }
            if(cores >= (tier * TIER_MULTIPLIER) && tier < 7) {
                coresText = "" + TextFormatting.GREEN + cores;
            }
            if (cores > 0) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Cores: " + coresText + TextFormatting.WHITE + totalCoresForLevelup + TextFormatting.AQUA + "]");
            }
        }
        return data;
    }

    //tier level tooltip formatting
    private static ITextComponent getTierData(ItemStack tool) {
        //reads nbt data and determines the tier tooltip based off the numbers
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains(CORES_TAG)) {
            int cores = tag.getInt(CORES_TAG);
            int tier = tag.getInt(TIER_TAG) + 1;
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

    //blade mode tooltip check and formatting
    private static ITextComponent getSwordState(ItemStack tool) {
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        //check if nbt tags are present yet
        if (tool.getTag() != null && tag.contains(DAMAGE_MODE_TAG) && tag.contains(PROJECTILE_MODE_TAG)) {
            //gets all relevant data from tags
            int tier = tag.getInt(TIER_TAG) > 0 ? tag.getInt(TIER_TAG) + 1 : 1;
            int totalDmg = tag.getInt(TOTAL_BONUS_TAG) > 0 ? tag.getInt(TOTAL_BONUS_TAG) : 1;
            boolean mega = tag.getBoolean(DAMAGE_MODE_TAG);
            boolean projectile = tag.getBoolean(PROJECTILE_MODE_TAG);
            DecimalFormat formatting = new DecimalFormat("#.##");
            formatting.setRoundingMode(RoundingMode.FLOOR);

            //all the tooltip formatting for each mode
            String activeState = TextFormatting.DARK_RED + "Deactivated";
            if (mega) {
                float calculatedDmg = totalDmg * ((float)tier / TOTAL_TIERS);
                activeState = TextFormatting.GREEN + "Megaton Raid";
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Mode: " + activeState + TextFormatting.WHITE + " | Increase hits by " + TextFormatting.RED + formatting.format(calculatedDmg) + TextFormatting.WHITE + " damage" + TextFormatting.AQUA + "]");
            }
            else if (projectile) {
                //black hole does half damage compared to melee since ranged would be just better
                int projectileBonus = tag != null && tag.contains(TOTAL_BONUS_TAG) ? tag.getInt(TOTAL_BONUS_TAG) : 1;
                float projectileDmg = (projectileBonus * ((float)tier / TOTAL_TIERS)) / 2;
                activeState = TextFormatting.GREEN + "Black Hole";
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Mode: " + activeState + TextFormatting.WHITE + " | Shoot a projectile dealing "  + TextFormatting.RED + formatting.format(projectileDmg) + TextFormatting.WHITE + " damage" + TextFormatting.AQUA + "]");
            }
            else {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Mode: " + activeState + TextFormatting.AQUA + "]");
            }
        }
        return data;
    }

    //right click functions for the blade
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack blade = playerIn.getHeldItem(handIn);
        if(uuidCheck(playerIn.getUniqueID())) {
            if (Screen.hasShiftDown()) {
                if(!worldIn.isRemote()) {
                    worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), ModSoundEvents.DEV_BLADE_MODE_SWITCH.get(), SoundCategory.PLAYERS, 1.25f, (1.0f + (worldIn.rand.nextFloat() * 0.05f)));
                }

                //gets appropriate tags to check current blade mode (if any), cycles between modes on SHIFT + RCLICK by setting each tag true/false
                if (blade.getTag() == null) {
                    CompoundNBT newTag = new CompoundNBT();
                    newTag.putBoolean(DAMAGE_MODE_TAG, false);
                    newTag.putBoolean(PROJECTILE_MODE_TAG, false);
                    blade.setTag(newTag);
                } else {
                    CompoundNBT tag = blade.getTag();
                    boolean mega = tag.getBoolean(DAMAGE_MODE_TAG);
                    boolean projectile = tag.getBoolean(PROJECTILE_MODE_TAG);
                    if(mega) {
                        tag.putBoolean(DAMAGE_MODE_TAG, false);
                        tag.putBoolean(PROJECTILE_MODE_TAG, true);
                    }
                    else if(projectile) {
                        tag.putBoolean(DAMAGE_MODE_TAG, false);
                        tag.putBoolean(PROJECTILE_MODE_TAG, false);
                    }
                    else {
                        tag.putBoolean(DAMAGE_MODE_TAG, true);
                        tag.putBoolean(PROJECTILE_MODE_TAG, false);
                    }
                    blade.setTag(tag);
                }
            }
            //shoot projectile if on projectile mode
            else {
                if(blade.getTag() != null) {
                    if (blade.getTag().contains(PROJECTILE_MODE_TAG) && blade.getTag().getBoolean(PROJECTILE_MODE_TAG)) {
                        int tier = blade.getTag().getInt(TIER_TAG);
                        shootProjectile(worldIn, playerIn, playerIn.getPosition());
                    }
                }
            }
        }
        return ActionResult.resultPass(blade);
    }

    private static void doSlash(World worldIn, LivingEntity targetIn, LivingEntity attackerIn, int damageBonusIn, int tierIn) {
        targetIn.hurtResistantTime = 0;
        targetIn.attackEntityFrom(ModDamageTypes.causeIndirectPhysDmg(attackerIn, attackerIn), damageBonusIn * ((float)tierIn / TOTAL_TIERS));
        targetIn.hurtResistantTime = 0;
        worldIn.playSound(null, targetIn.getPosX(), targetIn.getPosY(), targetIn.getPosZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.1f, (1.0f + (worldIn.rand.nextFloat() * 0.3f)) * 0.99f);
    }

    //summon and shoot projectile for the projectile mode
    private static void shootProjectile(World worldIn, PlayerEntity player, BlockPos pos) {
        //get direction player is looking currently
        Vector3d look = player.getLookVec();
        MiscHelpers.debugLogger("[Void Projectile Shot] Player Look Vector: " + look);
        //spawn projectile
        if (!worldIn.isRemote()){
            VoidProjectileEntity orbProjectile = new VoidProjectileEntity(ModEntityTypes.VOID_PROJECTILE.get(), player, worldIn);
            orbProjectile.shoot(look.getX(), look.getY(), look.getZ(), 3f, 0);
            orbProjectile.setNoGravity(true);
            worldIn.addEntity(orbProjectile);
            worldIn.playSound(null, pos, ModSoundEvents.VOID_PROJECTILE_SHOT.get(), SoundCategory.PLAYERS, 1, (1.0f + (worldIn.rand.nextFloat() * 0.05f)));
        }
        //do backwards dash
        double dashFactor = -2.0;
        player.setMotion(look.normalize().mul(dashFactor, dashFactor, dashFactor));
    }

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
}
