package com.uberhelixx.flatlights.item.tools;

import com.uberhelixx.flatlights.entity.BombSwingEntity;
import com.uberhelixx.flatlights.entity.ModEntityTypes;
import com.uberhelixx.flatlights.item.ModItems;
import com.uberhelixx.flatlights.network.PacketHandler;
import com.uberhelixx.flatlights.network.PacketLeftClick;
import com.uberhelixx.flatlights.network.PacketWriteNbt;
import com.uberhelixx.flatlights.util.MiscHelpers;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.List;

public class PrismaticSword extends SwordItem {

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
        if(Screen.hasShiftDown()) {
            String bombSwing = "[" + MiscHelpers.coloredText(TextFormatting.RED, "Inactive") + "] Explosive Swings: Shoots an explosive projectile when swinging this weapon.";
            assert stack.getTag() != null;
            if(stack.getTag().getBoolean("bomb")) {
                bombSwing = "[" + MiscHelpers.coloredText(TextFormatting.GREEN, "Active") + "] Explosive Swings: Shoots an explosive projectile when swinging this weapon.";
            }
            ITextComponent bombSwingTooltip = ITextComponent.getTextComponentOrEmpty(bombSwing);
            tooltip.add(bombSwingTooltip);
        }
        else {
            tooltip.add(new TranslationTextComponent("tooltip.flatlights.hold_shift"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack sword = playerIn.getHeldItem(handIn);

        if (playerIn.isCrouching()) {
            if(!worldIn.isRemote()) {
                worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), ModSoundEvents.DEV_BLADE_MODE_SWITCH.get(), SoundCategory.PLAYERS, 1.25f, (1.0f + (worldIn.rand.nextFloat() * 0.05f)));
            }
            if (sword.getTag() == null) {
                CompoundNBT newTag = new CompoundNBT();
                newTag.putBoolean("bomb", true);
                sword.setTag(newTag);
                PacketHandler.sendToServer(new PacketWriteNbt(newTag, sword));
            } else {
                CompoundNBT tag = sword.getTag();
                boolean active = tag.getBoolean("bomb");
                tag.putBoolean("bomb", !active);
                sword.setTag(tag);
                PacketHandler.sendToServer(new PacketWriteNbt(tag, sword));
            }
        }
        return ActionResult.resultPass(sword);
    }

    public static void throwBomb(PlayerEntity player, ItemStack sword) {
        World worldIn = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        CompoundNBT tag = sword.getTag();
        assert tag != null;
        if(!tag.contains("bomb") || !tag.getBoolean("bomb")) {
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
            bomb.shoot(look.getX(), look.getY(), look.getZ(), 1.5f, 0);
            worldIn.addEntity(bomb);
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1, (0.75f + (worldIn.rand.nextFloat() * 0.05f)));
        }
    }

    @SubscribeEvent
    public static void bombSwingTrigger(PlayerInteractEvent.LeftClickEmpty event) {
        if (!event.getItemStack().isEmpty() && event.getItemStack().getItem() == ModItems.PRISMATIC_SWORD.get()) {
            PacketHandler.sendToServer(new PacketLeftClick());
        }
    }
}
