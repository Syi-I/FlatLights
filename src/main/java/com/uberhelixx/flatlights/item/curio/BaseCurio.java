package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.util.TextHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

/**
 * NOTE: for balancing purposes:tm: the curios are put into three rough categories, those being cubes, prisms, and spheres.
 * Should only ever have one curio slot of each type, prevents stacking from only using one type of curio to negate everything.
 * Cube: augments some attribute/stats of the player, such as armor/toughness/speed (likely flat stat increases and not %)
 * Prism: does something for attacking and weapons, such as increasing attack speed/damage/reach, or add some sort of attacking mechanic
 * Sphere: adds some sort of special effect, such as providing water breathing or negating suffocation damage
 * Set bonuses should be a strong buff for wearing a set, could be a basic large % stat increase or some effect that triggers upon meeting requirements.
 * Set bonus effect goes on the Cube curio only, since that will have the least moving parts as it's only stat increases
 */
public class BaseCurio extends Item implements ICurioItem {

    public BaseCurio(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isImmuneToFire() { return true; }

    //all the formatting for curio tooltips should be done here since it should be uniform for all curios we make
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        //basic info tooltip
        if(stack.getTag() != null && !stack.getTag().isEmpty()) {
            tooltip.add(CurioUtils.getSetTooltip(stack));
            if(worldIn != null && worldIn.isRemote()) {
                tooltip.add(CurioUtils.getSetEffectTooltip(stack));
            }
            tooltip.add(CurioUtils.getTierTooltip(stack));
            if(stack.getTag().getFloat(CurioUtils.TIER) == CurioTier.GROWTH.MODEL_VALUE && stack.getTag().contains(CurioUtils.GROWTH_TRACKER)) {
                tooltip.add(CurioUtils.getGrowthTooltip(stack, true) );
            }
        }
        //how to use curio when not rolled yet
        else {
            ITextComponent useTooltip = TextHelpers.genericBrackets("Right-click to roll.", TextFormatting.GRAY);
            tooltip.add(useTooltip);
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
