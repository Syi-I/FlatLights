package com.uberhelixx.flatlights.common.item.tools;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.uberhelixx.flatlights.common.entity.Mk2ProjectileEntity;
import com.uberhelixx.flatlights.common.entity.ModEntityTypes;
import com.uberhelixx.flatlights.common.item.IMultiModeItem;
import com.uberhelixx.flatlights.common.network.PacketHandler;
import com.uberhelixx.flatlights.common.network.packets.PacketWriteNbt;
import com.uberhelixx.flatlights.startup.registry.ModDamageTypes;
import com.uberhelixx.flatlights.startup.registry.ModSoundEvents;
import com.uberhelixx.flatlights.util.ClientUtils;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.TooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import static com.uberhelixx.flatlights.util.lib.LibTagKeys.*;

public class PrismaticBladeMk2 extends SwordItem implements IMultiModeItem {
    public static final int DEFAULT_MODE = 0;
    public static final int DMG_MODE = 1;
    public static final int AURA_MODE = 2;
    public static final int SPEAR_MODE = 3;
    public static final int TOTAL_TIERS = 7;
    public static final int TIER_MULTIPLIER = 1000;
    public static final int REACH_DISTANCE = 4;
    
    public PrismaticBladeMk2(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    
    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if(pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(0, pEntityLiving, (livingEntity) -> {
                livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }
    
    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }
    
    @Override
    public boolean isFireResistant() {
        return true;
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            //check for if this player is able to use the item in the first place, if not give the default tooltip with no information
            if(ClientUtils.getPlayer() != null && !MiscUtils.uuidCheck(ClientUtils.getPlayer().getUUID())) {
                TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_blademk2_default");
            }
            else {
                TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_blademk2_shift");
            }
        }
        //normally display the stats and shift display hint
        else {
            if(ClientUtils.getPlayer() != null && MiscUtils.uuidCheck(ClientUtils.getPlayer().getUUID())) {
                if (pStack.hasTag() && pStack.getTag() != null) {
                    //display core data if present
                    if(pStack.getTag().contains(CURR_CORES_TAG)) {
                        getTierData(pStack, pTooltipComponents);
                        getCoreData(pStack, pTooltipComponents);
                    }
                    //display blade mode if present
                    if (pStack.getTag().contains(MODE_TAG)) {
                        getSwordState(pStack, pTooltipComponents);
                    }
                }
            }
            TooltipHelper.shiftHint(pTooltipComponents);
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    
    @Override
    public boolean isFoil(ItemStack pStack) {
        return super.isFoil(pStack) || (pStack.hasTag() && pStack.getTag().contains(MODE_TAG) && pStack.getTag().getInt(MODE_TAG) > 0);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);
        if(MiscUtils.uuidCheck(pPlayer.getUUID())) {
            //standard right click use functions
            if(pStack.getTag() != null && pStack.getTag().contains(MODE_TAG) && !pPlayer.isCrouching()) {
                CompoundTag tag = pStack.getTag();
                int mode = tag.getInt(MODE_TAG);
                //do dash if in damage mode
                if(mode == DMG_MODE) {
                    doDash(pPlayer.level(), pPlayer);
                }
                //try doing spear launch if on spear mode
                if(mode == SPEAR_MODE) {
                    pPlayer.startUsingItem(pUsedHand);
                    return InteractionResultHolder.consume(pStack);
                }
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
    
    @Override
    public int getUseDuration(ItemStack pStack) {
        //if the item stack is in spear mode, return a different use duration
        if(pStack.getTag() != null && pStack.getTag().contains(MODE_TAG)) {
            int mode = pStack.getTag().getInt(MODE_TAG);
            if(mode == SPEAR_MODE) {
                //same duration as a vanilla trident has
                return 72000;
            }
        }
        return super.getUseDuration(pStack);
    }
    
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if(pStack.hasTag() && pStack.getTag() != null) {
            //check if the weapon is in spear mode or not
            if(pStack.getTag().contains(MODE_TAG) && pStack.getTag().getInt(MODE_TAG) == SPEAR_MODE) {
                //make sure this is a player using the item and not something else
                if(pLivingEntity instanceof Player playerEntity) {
                    int timeUsed = this.getUseDuration(pStack) - pTimeCharged;
                    //check how long it's been since starting to use the item
                    if(timeUsed >= 2) {
                        //get riptide level if present, to modify launch velocity
                        int riptideModifier = EnchantmentHelper.getRiptide(pStack) > 0 ? EnchantmentHelper.getRiptide(pStack) + 4 : 4;
                        //haha yeah let's pretend that this unbreakable item can break for a second
                        if (!pLevel.isClientSide()) {
                            pStack.hurtAndBreak(0, playerEntity, (player) -> {
                                player.broadcastBreakEvent(playerEntity.getUsedItemHand());
                            });
                        }
                        
                        //vvv actual logic for launching the player and doing the riptide spin attack vvv
                        float yaw = playerEntity.getYRot();
                        float pitch = playerEntity.getXRot();
                        
                        //calculate xyz directions from player's view direction, for setting player velocity later
                        float xDir = -Mth.sin(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
                        float yDir = -Mth.sin(pitch * ((float) Math.PI / 180F));
                        float zDir = Mth.cos(yaw * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
                        float f4 = Mth.sqrt(xDir * xDir + yDir * yDir + zDir * zDir);
                        float f5 = 3.0F * ((1.0F + (float) riptideModifier) / 4.0F);
                        xDir = xDir * (f5 / f4);
                        yDir = yDir * (f5 / f4);
                        zDir = zDir * (f5 / f4);
                        
                        //throw the player in this direction based off where the player is looking
                        playerEntity.push(xDir, yDir, zDir);
                        //the actual spinning riptide attack and how long it lasts
                        playerEntity.startAutoSpinAttack(15);
                        
                        //get the player off the ground a bit if they are standing instead of in the air
                        if (playerEntity.onGround()) {
                            playerEntity.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
                        }
                        
                        //sound effect that plays when using the weapon
                        SoundEvent soundEvent = SoundEvents.TRIDENT_RIPTIDE_3;
                        pLevel.playSound(null, playerEntity, soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }
    
    protected static final UUID E_REACH_MOD = new UUID(8395284197621950L, 9827436454129586L);
    protected static final UUID B_REACH_MOD = new UUID(3944721983778684L, 1834462839823474L);
    protected static final UUID CORE_DMG_MOD = new UUID(1292398942389764L, 5934782334298182L);
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> oldMap = super.getAttributeModifiers(slot, stack);
        ListMultimap<Attribute, AttributeModifier> newMap = ArrayListMultimap.create();
        CompoundTag tag = stack.getTag();
        double attackModifier = tag != null && tag.contains(TOTAL_CORES_TAG) ? tag.getInt(TOTAL_CORES_TAG) * 0.001 : 0;
        double reachModifier = tag != null && tag.contains(MODE_TAG) && tag.getInt(MODE_TAG) == SPEAR_MODE ? REACH_DISTANCE : 0;
        if (slot == EquipmentSlot.MAINHAND) {
            newMap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(CORE_DMG_MOD, "Core Count Modifier", attackModifier, AttributeModifier.Operation.MULTIPLY_TOTAL));
            newMap.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(E_REACH_MOD, "E Reach Modifier", reachModifier, AttributeModifier.Operation.ADDITION));
            newMap.put(ForgeMod.BLOCK_REACH.get(), new AttributeModifier(B_REACH_MOD, "B Reach Modifier", reachModifier, AttributeModifier.Operation.ADDITION));
        }
        for (Attribute attribute : oldMap.keySet()) {
            newMap.putAll(attribute, oldMap.get(attribute));
        }
        return newMap;
    }
    
    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(0, pAttacker, (livingEntity) -> {
            livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        Level pLevel = pAttacker.level();
        if(MiscUtils.uuidCheck(pAttacker.getUUID())) {
            pTarget.invulnerableTime = 0;
            if (pStack.getTag() != null) {
                int damageBonus = 1;
                int tier = pStack.getTag().getInt(TIER_TAG);
                //grab total damage, or leave at 1 if no bonus yet, then do big damage slash
                if(pStack.getTag().getInt(MODE_TAG) == DMG_MODE) {
                    damageBonus = Math.max(pStack.getTag().getInt(TOTAL_CORES_TAG), 1);
                }
                if(pStack.getTag().getInt(MODE_TAG) == DMG_MODE) {
                    doSlash(pLevel, pTarget, pAttacker, damageBonus, tier);
                }
            }
            pTarget.invulnerableTime = 0;
        }
        else {
            if(pAttacker instanceof Player player) {
                Component fail = Component.literal("This item does not belong to you.");
                player.displayClientMessage(fail, false);
                pLevel.playSound(null, pTarget.getX(), pTarget.getY(), pTarget.getZ(), ModSoundEvents.SQUEAK.get(), SoundSource.PLAYERS, 0.5f, (1.0f + (pLevel.getRandom().nextFloat() * 0.3f)) * 0.99f);
            }
            pTarget.heal(pTarget.getMaxHealth());
            return false;
        }
        return true;
    }
    
    //number of modes the sword has
    @Override
    public int getNumModes(ItemStack stack) {
        return 4;
    }
    
    //what to do when cycling modes
    @Override
    public void onModeChange(Player pPlayer, ItemStack pStack) {
        if(MiscUtils.uuidCheck(pPlayer.getUUID())) {
            //get or create the blade mode tag
            CompoundTag tag = pStack.getOrCreateTag();
            if (tag.isEmpty() || !hasBladeTags(tag)) {
                tag = putFreshTags(tag);
                pStack.setTag(tag);
                if (pPlayer.level().isClientSide()) {
                    PacketHandler.sendToServer(new PacketWriteNbt(tag, pStack));
                }
            }
            else {
                int bladeMode = tag.getInt(MODE_TAG);
                Component toggleText;
                //display text for damage mode
                if (bladeMode == DMG_MODE) {
                    toggleText = Component.literal("Mode Cycled: ").withStyle(ChatFormatting.WHITE)
                            .append(Component.literal("[").withStyle(ChatFormatting.AQUA))
                            .append(Component.literal("Annihilation").withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("]").withStyle(ChatFormatting.AQUA));
                }
                //display text for projectile mode
                else if (bladeMode == AURA_MODE) {
                    toggleText = Component.literal("Mode Cycled: ").withStyle(ChatFormatting.WHITE)
                            .append(Component.literal("[").withStyle(ChatFormatting.AQUA))
                            .append(Component.literal("Soul Sword").withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("]").withStyle(ChatFormatting.AQUA));
                }
                //display text for spear mode
                else if (bladeMode == SPEAR_MODE) {
                    toggleText = Component.literal("Mode Cycled: ").withStyle(ChatFormatting.WHITE)
                            .append(Component.literal("[").withStyle(ChatFormatting.AQUA))
                            .append(Component.literal("Spear").withStyle(ChatFormatting.GREEN))
                            .append(Component.literal("]").withStyle(ChatFormatting.AQUA));
                }
                //display text for inactive mode
                else {
                    toggleText = Component.literal("Mode Cycled: ").withStyle(ChatFormatting.WHITE)
                            .append(Component.literal("[").withStyle(ChatFormatting.AQUA))
                            .append(Component.literal("Inactive").withStyle(ChatFormatting.RED))
                            .append(Component.literal("]").withStyle(ChatFormatting.AQUA));
                }
                //clientside mode cycling notification
                if (!pPlayer.level().isClientSide()) {
                    pPlayer.displayClientMessage(toggleText, true);
                    MiscUtils.modeSwitchSound(pPlayer, bladeMode != SPEAR_MODE);
                }
            }
        }
    }
    
    /**
     * Does the additional slash damage for the blade's damage mode
     * @param levelIn World that the player is in, used for sound event
     * @param targetIn Target being attacked by the slash
     * @param attackerIn The attacker who is performing the slash
     * @param damageBonusIn The amount of damage that is being dealt from the slash
     * @param tierIn The tier of the sword, used in the damage calculation
     */
    private void doSlash(Level levelIn, LivingEntity targetIn, LivingEntity attackerIn, int damageBonusIn, int tierIn) {
        targetIn.invulnerableTime = 0;
        targetIn.hurt(ModDamageTypes.causeQuantumDamage(attackerIn), damageBonusIn * ((float)tierIn / TOTAL_TIERS));
        targetIn.invulnerableTime = 0;
        levelIn.playSound(null, targetIn.getX(), targetIn.getY(), targetIn.getZ(), SoundEvents.ANVIL_PLACE, SoundSource.PLAYERS, 0.1f, (1.0f + (levelIn.getRandom().nextFloat() * 0.3f)) * 0.99f);
    }
    
    private void doDash(Level levelIn, Player playerIn) {
        //get direction player is looking currently
        Vec3 look = playerIn.getLookAngle();
        levelIn.playSound(playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.75f, (1.5f + (levelIn.getRandom().nextFloat() * 0.05f)));
        //do dash
        double dashFactor = 2.0;
        playerIn.setDeltaMovement(look.normalize().multiply(dashFactor, dashFactor, dashFactor));
    }
    
    /**
     * Summon and shoot projectile for the projectile mode
     * @param levelIn World that the projectile is being summoned in
     * @param player Player who is shooting the projectile
     * @param pos Position where the projectile is being summoned
     */
    public static void shootProjectile(Level levelIn, Player player, BlockPos pos) {
        //get direction player is looking currently
        Vec3 look = player.getLookAngle();
        //spawn projectile
        if (!levelIn.isClientSide()){
            Mk2ProjectileEntity bladeProjectile = new Mk2ProjectileEntity(ModEntityTypes.MK2_PROJECTILE_ENTITY.get(), player, levelIn);
            bladeProjectile.shoot(look.x(), look.y(), look.z(), 2.5f, 0);
            bladeProjectile.setNoGravity(true);
            levelIn.addFreshEntity(bladeProjectile);
        }
        levelIn.playSound(null, pos, ModSoundEvents.VOID_PROJECTILE_SHOT.get(), SoundSource.PLAYERS, 0.3f, (0.8f + (levelIn.getRandom().nextFloat() * 0.05f)));
    }
    
    /**
     * Tier level tooltip formatting
     * If the amount of tiers changes, have to manually add things here so that it still has names and formatting, otherwise any amount of tiers over 7 defaults to {@code Unknown}
     * @param tool The tool that is getting the formatted tooltip
     * @param tooltipIn The tooltip for the item
     */
    private static void getTierData(ItemStack tool, List<Component> tooltipIn) {
        //reads nbt data and determines the tier tooltip based off the numbers
        CompoundTag tag = tool.getTag();
        if(tool.getTag() != null && tool.getTag().contains(CURR_CORES_TAG)) {
            int cores = tag.getInt(CURR_CORES_TAG);
            int tier = tag.getInt(TIER_TAG);
            Component tierName = switch(tier) {
                case 1 -> Component.literal("Dormant").withStyle(ChatFormatting.GRAY);
                case 2 -> Component.literal("Awakened").withStyle(ChatFormatting.WHITE);
                case 3 -> Component.literal("Ascended").withStyle(ChatFormatting.YELLOW);
                case 4 -> Component.literal("Exalted").withStyle(ChatFormatting.GOLD);
                case 5 -> Component.literal("Sacred").withStyle(ChatFormatting.RED);
                case 6 -> Component.literal("Divine").withStyle(ChatFormatting.LIGHT_PURPLE);
                case 7 -> Component.literal("Primordial").withStyle(ChatFormatting.DARK_PURPLE);
                default ->
                        Component.literal("Unknown").withStyle(ChatFormatting.BLACK).withStyle(ChatFormatting.OBFUSCATED);
            };
            if(cores > 0) {
                TooltipHelper.labelBrackets(tooltipIn, "Tier", null, tierName);
            }
        }
    }
    
    /**
     * Blade mode tooltip check and formatting
     * @param tool The tool that is getting this tooltip
     * @param tooltipIn The tooltip for the item
     */
    private static void getSwordState(ItemStack tool, List<Component> tooltipIn) {
        CompoundTag tag = tool.getTag();
        Component activeState = Component.literal("Deactivated").withStyle(ChatFormatting.DARK_RED);
        //check if nbt tags are present yet
        if(tag != null && hasBladeTags(tag)) {
            //gets all relevant data from tags
            int tier = tag.getInt(TIER_TAG) > 0 ? tag.getInt(TIER_TAG) : 1;
            int totalDmg = tag.getInt(TOTAL_CORES_TAG) > 0 ? tag.getInt(TOTAL_CORES_TAG) : 1;
            int mode = tag.getInt(MODE_TAG);
            DecimalFormat formatting = new DecimalFormat("#.##");
            formatting.setRoundingMode(RoundingMode.FLOOR);
            
            //all the tooltip formatting for each mode
            if(mode == DMG_MODE) {
                float calculatedDmg = totalDmg * ((float)tier / TOTAL_TIERS);
                activeState = Component.literal("Annihilation").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(" | Increases damage of hits by ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(formatting.format(calculatedDmg)).withStyle(ChatFormatting.RED))
                        .append(Component.literal(" damage").withStyle(ChatFormatting.WHITE));
            }
            else if(mode == AURA_MODE) {
                float projectileDmg = calcProjectileDmg(tag);
                activeState = Component.literal("Soul Sword").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(" | Swings launch a projectile dealing ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(formatting.format(projectileDmg)).withStyle(ChatFormatting.RED))
                        .append(Component.literal(" damage per hit").withStyle(ChatFormatting.WHITE));
            }
            else if(mode == SPEAR_MODE) {
                activeState = Component.literal("Spear").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(" | Perform a leaping attack").withStyle(ChatFormatting.WHITE));
            }
        }
        TooltipHelper.labelBrackets(tooltipIn, "Mode", null, activeState);
    }
    
    
    /**
     * Core count tooltip formatting
     * @param tool The tool getting the formatted tooltip
     * @return The formatted tooltip containing the amount of Cores the tool accumulated out of the total Cores for the tier
     */
    private static void getCoreData(ItemStack tool, List<Component> tooltipIn) {
        CompoundTag tag = tool.getTag();
        Component data = Component.empty();
        if (tool.getTag() != null && tool.getTag().contains(CURR_CORES_TAG)) {
            int cores = tag.getInt(CURR_CORES_TAG);
            int tier = tag.getInt(TIER_TAG);
            String totalCoresForLevelup = "/" + (tier * TIER_MULTIPLIER);
            Component coresText = Component.literal(String.valueOf(cores)).withStyle(ChatFormatting.RED);
            if(tier >= TOTAL_TIERS) {
                totalCoresForLevelup = "";
            }
            if(cores >= (tier * TIER_MULTIPLIER) && tier < TOTAL_TIERS) {
                coresText = Component.literal(String.valueOf(cores)).withStyle(ChatFormatting.GREEN);
            }
            if(cores > 0) {
                data = coresText.copy().append(Component.literal(totalCoresForLevelup).withStyle(ChatFormatting.WHITE));
            }
            TooltipHelper.labelBrackets(tooltipIn, "Cores", null, data);
        }
    }
    
    /**
     * Checks if the tag has all 4 blade tags
     * @param tag The {@link CompoundTag} of the item
     * @return TRUE if the item has all tags, FALSE if missing any
     */
    public static boolean hasBladeTags(CompoundTag tag) {
        return tag.contains(MODE_TAG) && tag.contains(CURR_CORES_TAG) && tag.contains(TOTAL_CORES_TAG) && tag.contains(TIER_TAG);
    }
    
    /**
     * Puts all 4 tags onto the sword
     * @param tag The {@link CompoundTag} of the item
     * @return The updated tag
     */
    public static CompoundTag putFreshTags(CompoundTag tag) {
        tag.putInt(MODE_TAG, DEFAULT_MODE);
        tag.putInt(CURR_CORES_TAG, 0);
        tag.putInt(TOTAL_CORES_TAG, 0);
        tag.putInt(TIER_TAG, 1);
        return tag;
    }
    
    /**
     * Check if a player has the PLAYER_CORETRACKER_TAG in persistent NBT data
     * @param player The player whose data we're looking at
     * @return TRUE if the tracker is present, FALSE otherwise
     */
    public static boolean hasCoreTracker(Player player) {
        CompoundTag persistent = MiscUtils.getPersistent(player);
        return persistent != null && persistent.contains(PLAYER_CORETRACKER_TAG) && persistent.getInt(PLAYER_CORETRACKER_TAG) >= 0;
    }
    
    /**
     * Gets the PLAYER_CORETRACKER_TAG value from a player's persistent NBT data
     * @param player The player whose data we're looking at
     * @return The tracker value from the data of the player, or -1 if no data
     */
    public static int getPlayerCores(Player player) {
        CompoundTag persistent = MiscUtils.getPersistent(player);
        if(persistent != null && persistent.contains(PLAYER_CORETRACKER_TAG)) {
            return persistent.getInt(PLAYER_CORETRACKER_TAG);
        }
        return -1;
    }
    
    /**
     * Increase the PLAYER_CORETRACKER_TAG value of a player
     * @param player The player
     * @param amount Number the tracker value should increase by
     */
    public static void increasePlayerCores(Player player, Integer amount) {
        CompoundTag persistent = MiscUtils.getPersistent(player);
        if(persistent != null && getPlayerCores(player) >= 0) {
            persistent.putInt(PLAYER_CORETRACKER_TAG, getPlayerCores(player) + amount);
        }
    }
    
    /**
     * Directly set the PLAYER_CORETRACKER_TAG value of a player
     * @param player The player
     * @param total The number to set the tracker value to
     */
    public static void setPlayerCores(Player player, Integer total) {
        CompoundTag persistent = MiscUtils.getPersistent(player);
        if(persistent != null && getPlayerCores(player) >= 0) {
            persistent.putInt(PLAYER_CORETRACKER_TAG, total);
        }
    }
    
    /**
     * Calculates the damage dealt by the projectile mode
     * @param shooter The player shooting out the projectile
     * @return The calculated damage that the projectile should do based on the core tracker count
     */
    public static float calcProjectileDmg(Player shooter) {
        //pull damage tag from sword to set damage values
        CompoundTag tag = shooter.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof PrismaticBladeMk2 ? shooter.getItemInHand(InteractionHand.MAIN_HAND).getTag() : null;
        //if tag present with totalBonus then get damage, otherwise just get 1
        int projectileBonus = tag != null && tag.contains(TOTAL_CORES_TAG) && tag.getInt(TOTAL_CORES_TAG) > 0 ? tag.getInt(TOTAL_CORES_TAG) : 1;
        int tier = tag != null && tag.contains(TIER_TAG) ? tag.getInt(TIER_TAG) + 1 : 1;
        return (projectileBonus * ((float) tier / TOTAL_TIERS)) / 2;
    }
    
    /**
     * Calculates the damage dealt by the projectile mode
     * @param tag The {@link CompoundTag} of the weapon we are calculating the damage for
     * @return The calculated damage that the projectile should do based on the core tracker count
     */
    public static float calcProjectileDmg(CompoundTag tag) {
        //if tag present with totalBonus then get damage, otherwise just get 1
        int projectileBonus = tag != null && tag.contains(TOTAL_CORES_TAG) && tag.getInt(TOTAL_CORES_TAG) > 0 ? tag.getInt(TOTAL_CORES_TAG) : 1;
        int tier = tag != null && tag.contains(TIER_TAG) ? tag.getInt(TIER_TAG) + 1 : 1;
        return (projectileBonus * ((float) tier / TOTAL_TIERS)) / 2;
    }
    
}
