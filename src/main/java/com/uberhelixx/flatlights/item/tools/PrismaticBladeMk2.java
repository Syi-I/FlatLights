package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.damagesource.ModDamageTypes;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.entity.VoidProjectileEntity;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.uberhelixx.flatlights.util.MiscHelpers.uuidCheck;

public class PrismaticBladeMk2 extends SwordItem {

    public static final String FIRSTJOIN_TAG = "flatlights.firstJoin"; //used to check if dedicated player has been given the starter blade yet
    public static final String PLAYER_CORETRACKER_TAG = "flatlights.coreTracker"; //used for checking and remembering core count value if blade gets lost somehow
    public static final String DAMAGE_MODE_TAG = "flatlights.damage"; //bonus melee damage mode
    public static final String PROJECTILE_MODE_TAG = "flatlights.projectile"; //projectile shooting mode
    public static final String TIER_TAG = "flatlights.tier"; //current tier of the blade
    public static final String CURR_CORES_TAG = "flatlights.cores"; //current number of cores
    public static final String TOTAL_CORES_TAG = "flatlights.totalCores"; //total number of cores gained = to total damage bonus
    public static final int TIER_MULTIPLIER = 1000; //cores needed per tier to increase tier
    public static final int TOTAL_TIERS = 7; //total tiers available

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
                int tier = stack.getTag().getInt(TIER_TAG);
                //grab total damage, or leave at 1 if no bonus yet
                if(stack.getTag().getBoolean(DAMAGE_MODE_TAG)) {
                    damageBonus = Math.max(stack.getTag().getInt(TOTAL_CORES_TAG), 1);
                }
                if(stack.getTag().getBoolean(DAMAGE_MODE_TAG)) {
                    doSlash(world, target, attacker, damageBonus, tier);
                }
            }
            target.hurtResistantTime = 0;
        }
        else {
            ITextComponent fail = new StringTextComponent("This item does not belong to you.");
            attacker.sendMessage(fail, attacker.getUniqueID());
            world.playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), ModSoundEvents.SQUEAK.get(), SoundCategory.PLAYERS, 0.5f, (1.0f + (world.rand.nextFloat() * 0.3f)) * 0.99f);
            target.heal(target.getMaxHealth());
            return false;
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(Screen.hasShiftDown()) {
            //check for if this player is able to use the item in the first place, if not give the default tooltip with no information
            if(Minecraft.getInstance().player != null && !MiscHelpers.uuidCheck(Minecraft.getInstance().player.getUniqueID())) {
                tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_blademk2_default"));
            }
            else {
                tooltip.add(new TranslationTextComponent("tooltip.flatlights.prismatic_blademk2_shift"));
                if (stack.getTag() != null && stack.getTag().contains(CURR_CORES_TAG)) {
                    tooltip.add(getTierData(stack));
                    tooltip.add(getCoreData(stack));
                    if (stack.getTag().contains(DAMAGE_MODE_TAG) || stack.getTag().contains(PROJECTILE_MODE_TAG)) {
                        tooltip.add(getSwordState(stack));
                    }
                }
            }
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    //core count tooltip formatting
    private static ITextComponent getCoreData(ItemStack tool) {
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains(CURR_CORES_TAG)) {
            int cores = tag.getInt(CURR_CORES_TAG);
            int tier = tag.getInt(TIER_TAG);
            String totalCoresForLevelup = "/" + (tier * TIER_MULTIPLIER);
            String coresText = "" + TextFormatting.RED + cores;
            if(tier >= TOTAL_TIERS) {
                totalCoresForLevelup = "";
            }
            if(cores >= (tier * TIER_MULTIPLIER) && tier < TOTAL_TIERS) {
                coresText = "" + TextFormatting.GREEN + cores;
            }
            if (cores > 0) {
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Cores: " + coresText + TextFormatting.WHITE + totalCoresForLevelup + TextFormatting.AQUA + "]");
            }
        }
        return data;
    }

    //tier level tooltip formatting
    //if the amount of tiers changes have to manually add things here so that it still has names and formatting, otherwise any amount of tiers over 7 is unknown
    private static ITextComponent getTierData(ItemStack tool) {
        //reads nbt data and determines the tier tooltip based off the numbers
        CompoundNBT tag = tool.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (tool.getTag() != null && tool.getTag().contains(CURR_CORES_TAG)) {
            int cores = tag.getInt(CURR_CORES_TAG);
            int tier = tag.getInt(TIER_TAG);
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
            int tier = tag.getInt(TIER_TAG) > 0 ? tag.getInt(TIER_TAG) : 1;
            int totalDmg = tag.getInt(TOTAL_CORES_TAG) > 0 ? tag.getInt(TOTAL_CORES_TAG) : 1;
            boolean damage = tag.getBoolean(DAMAGE_MODE_TAG);
            boolean projectile = tag.getBoolean(PROJECTILE_MODE_TAG);
            DecimalFormat formatting = new DecimalFormat("#.##");
            formatting.setRoundingMode(RoundingMode.FLOOR);

            //all the tooltip formatting for each mode
            String activeState = TextFormatting.DARK_RED + "Deactivated";
            if (damage) {
                float calculatedDmg = totalDmg * ((float)tier / TOTAL_TIERS);
                activeState = TextFormatting.GREEN + "Annihilation";
                data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Mode: " + activeState + TextFormatting.WHITE + " | Increase hits by " + TextFormatting.RED + formatting.format(calculatedDmg) + TextFormatting.WHITE + " damage" + TextFormatting.AQUA + "]");
            }
            else if (projectile) {
                //black hole does half damage compared to melee since ranged would be just better
                int projectileBonus = tag != null && tag.contains(TOTAL_CORES_TAG) ? tag.getInt(TOTAL_CORES_TAG) : 1;
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
            //can't use Screen.hasShiftDown since clientside so it doesn't register
            if (playerIn.isCrouching()) {
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), ModSoundEvents.DEV_BLADE_MODE_SWITCH.get(), SoundCategory.PLAYERS, 1.25f, (1.0f + (worldIn.rand.nextFloat() * 0.05f)));

                //gets appropriate tags to check current blade mode (if any), cycles between modes on SHIFT + RCLICK by setting each tag true/false
                if (blade.getTag() == null) {
                    CompoundNBT newTag = new CompoundNBT();
                    newTag.putBoolean(DAMAGE_MODE_TAG, false);
                    newTag.putBoolean(PROJECTILE_MODE_TAG, false);
                    blade.setTag(newTag);
                    PacketHandler.sendToServer(new PacketWriteNbt(newTag, blade));
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
                    PacketHandler.sendToServer(new PacketWriteNbt(tag, blade));
                }
            }
            //shoot projectile if on projectile mode
            else {
                if(blade.getTag() != null && !playerIn.isCrouching()) {
                    if (blade.getTag().contains(PROJECTILE_MODE_TAG) && blade.getTag().getBoolean(PROJECTILE_MODE_TAG)) {
                        shootProjectile(worldIn, playerIn, playerIn.getPosition());
                    }
                }
            }
        }
        return ActionResult.resultPass(blade);
    }

    private void doSlash(World worldIn, LivingEntity targetIn, LivingEntity attackerIn, int damageBonusIn, int tierIn) {
        targetIn.hurtResistantTime = 0;
        targetIn.attackEntityFrom(ModDamageTypes.causeIndirectEntangled(attackerIn, attackerIn), damageBonusIn * ((float)tierIn / TOTAL_TIERS));
        targetIn.hurtResistantTime = 0;
        worldIn.playSound(null, targetIn.getPosX(), targetIn.getPosY(), targetIn.getPosZ(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.PLAYERS, 0.1f, (1.0f + (worldIn.rand.nextFloat() * 0.3f)) * 0.99f);
    }

    //summon and shoot projectile for the projectile mode
    private void shootProjectile(World worldIn, PlayerEntity player, BlockPos pos) {
        //get direction player is looking currently
        Vector3d look = player.getLookVec();
        MiscHelpers.debugLogger("[Void Projectile Shot] Player Look Vector: " + look);
        //spawn projectile
        if (!worldIn.isRemote()){
            VoidProjectileEntity orbProjectile = new VoidProjectileEntity(ModEntityTypes.VOID_PROJECTILE.get(), player, worldIn);
            orbProjectile.shoot(look.getX(), look.getY(), look.getZ(), 3f, 0);
            orbProjectile.setNoGravity(true);
            worldIn.addEntity(orbProjectile);
        }
        worldIn.playSound(null, pos, ModSoundEvents.VOID_PROJECTILE_SHOT.get(), SoundCategory.PLAYERS, 0.75f, (1.0f + (worldIn.rand.nextFloat() * 0.05f)));
        //do backwards dash
        double dashFactor = -2.0;
        player.setMotion(look.normalize().mul(dashFactor, dashFactor, dashFactor));
    }
}
