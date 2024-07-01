package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.entity.BombSwingEntity;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.network.PacketGenericToggleMessage;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PrismaticSword extends SwordItem {
    public static final String BOMB_MODE = "flatlights.bomb_mode"; //bomb shooting mode

    public PrismaticSword(IItemTier tier, int attackDamageIn, float attackSpeedIn, Properties builderIn) {
        super(tier, attackDamageIn, attackSpeedIn, builderIn);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(0, attacker, (entity) -> {
            entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });

        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //determine if the sword is activated or deactivated
        String bombSwing = TextHelpers.labelBrackets("Explosive Swings Mode", null, "Deactivated", TextFormatting.RED).getString();
        assert stack.getTag() != null;
        if(stack.getTag().getBoolean(BOMB_MODE)) {
            bombSwing = TextHelpers.labelBrackets("Explosive Swings Mode", null, "Activated", TextFormatting.GREEN).getString();
        }
        ITextComponent bombSwingTooltip = ITextComponent.getTextComponentOrEmpty(bombSwing);

        //if shift is down, show use tooltip, otherwise show short label tooltips
        if(Screen.hasShiftDown()) {
            String bombSwingUse = TextFormatting.DARK_PURPLE + "Shoots an explosive projectile when swinging this weapon.";
            ITextComponent bombSwingUseTooltip = ITextComponent.getTextComponentOrEmpty(bombSwingUse);
            tooltip.add(bombSwingUseTooltip);
        }
        else {
            tooltip.add(bombSwingTooltip);
            tooltip.add(TextHelpers.shiftTooltip("for details"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack sword = playerIn.getHeldItem(handIn);

        if (playerIn.isCrouching()) {
            if (sword.getTag() == null) {
                CompoundNBT newTag = new CompoundNBT();
                newTag.putBoolean(BOMB_MODE, true);
                sword.setTag(newTag);
                if(playerIn.getEntityWorld().isRemote()) {
                    PacketHandler.sendToServer(new PacketWriteNbt(newTag, sword));
                }
            }
            else {
                CompoundNBT tag = sword.getTag();
                boolean active = tag.getBoolean(BOMB_MODE);
                tag.putBoolean(BOMB_MODE, !active);
                sword.setTag(tag);
                if(playerIn.getEntityWorld().isRemote()) {
                    PacketHandler.sendToServer(new PacketWriteNbt(tag, sword));
                }
                String toggleText = TextFormatting.WHITE + "Explosive Swings: " + TextHelpers.genericBrackets("Activated", TextFormatting.GREEN).getString();
                if(active) {
                    toggleText = TextFormatting.WHITE + "Explosive Swings: " + TextHelpers.genericBrackets("Deactivated", TextFormatting.RED).getString();
                }
                if(!playerIn.getEntityWorld().isRemote()) {
                    PacketHandler.sendToPlayer((ServerPlayerEntity) playerIn, new PacketGenericToggleMessage(toggleText, !active, false));
                }
            }
        }
        return ActionResult.resultPass(sword);
    }

    public static void throwBomb(PlayerEntity player, ItemStack sword) {
        World worldIn = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        CompoundNBT tag = sword.getTag();
        assert tag != null;
        if(!tag.contains(BOMB_MODE) || !tag.getBoolean(BOMB_MODE)) {
            return;
        }
        if(player.getCooledAttackStrength(0f) != 1) {
            return;
        }
        if(player.getHeldItem(Hand.MAIN_HAND) != sword) {
            return;
        }
        //get direction player is looking currently
        Vector3d look = player.getLookVec();
        //spawn projectile
        if(!worldIn.isRemote()){
            BombSwingEntity bomb = new BombSwingEntity(ModEntityTypes.BOMB_SWING_PROJECTILE.get(), player, worldIn);
            bomb.shoot(look.getX(), look.getY(), look.getZ(), 1.0f, 0);
            worldIn.addEntity(bomb);
        }
        worldIn.playSound(null, pos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1, (0.75f + (worldIn.rand.nextFloat() * 0.05f)));
    }
}
