package com.uberhelixx.flatlights.common.item.tools;

import com.uberhelixx.flatlights.common.entity.BombEntity;
import com.uberhelixx.flatlights.common.entity.ModEntityTypes;
import com.uberhelixx.flatlights.common.item.IMultiModeItem;
import com.uberhelixx.flatlights.common.item.tools.basetools.BaseSword;
import com.uberhelixx.flatlights.util.MiscUtils;
import com.uberhelixx.flatlights.util.TooltipHelper;
import com.uberhelixx.flatlights.util.lib.LibTagKeys;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrismaticSword extends BaseSword implements IMultiModeItem {
    public PrismaticSword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            TooltipHelper.formatUsage(pTooltipComponents, "tooltip.flatlights.prismatic_sword_shift");
        }
        else {
            TooltipHelper.toggleText("Explosion: ", pStack, pTooltipComponents);
            TooltipHelper.shiftHint(pTooltipComponents);
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    
    public static void throwBomb(Player player, ItemStack sword) {
        Level levelIn = player.level();
        BlockPos pos = player.getOnPos();
        
        if(player.getMainHandItem() != sword) {
            return;
        }
        if(sword.getTag() == null || !sword.getTag().getBoolean(LibTagKeys.MODE_TAG)) {
            return;
        }
        if(player.getAttackStrengthScale(0f) != 1) {
            return;
        }
        Vec3 looking = player.getLookAngle();
        //spawn projectile
        if(!levelIn.isClientSide()) {
            BombEntity bomb = new BombEntity(ModEntityTypes.BOMB_PROJECTILE.get(), player, levelIn);
            bomb.shoot(looking.x(), looking.y(), looking.z(), 1.0f, 0f);
            levelIn.addFreshEntity(bomb);
        }
        levelIn.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1, (0.75f + (levelIn.random.nextFloat() * 0.05f)));
    }
    
    @Override
    public void onModeChange(Player player, ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag != null) {
            int mode = tag.getInt(LibTagKeys.MODE_TAG);
            //send player notification to indicate that the item toggle has changed
            player.displayClientMessage(mode == 1 ? Component.translatable("flatlights.enabled") : Component.translatable("flatlights.disabled"), true);
            MiscUtils.modeSwitchSound(player, mode == 1);
        }
    }
}
