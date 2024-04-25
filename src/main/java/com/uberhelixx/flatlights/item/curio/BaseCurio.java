package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;

public class BaseCurio extends Item implements ICurioItem {

    public BaseCurio(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isImmuneToFire() { return true; }

    //curio tier floats for differentiating item models
    public static final float COMMON_TIER = 0.1f;
    public static final float RARE_TIER = 0.2f;
    public static final float EPIC_TIER = 0.3f;
    public static final float LEGENDARY_TIER = 0.4f;
    public static final float GROWTH_TIER = 0.5f;

    //curio tier nbt tag, used to determine buff scaling
    public static final String TIER = "flatlights.curiotier";
    //curio set nbt tag, used to determine what set the curio is a part of
    public static final String SET = "flatlights.curioset";

    //percent chances for each tier
    public static final float RARE_CHANCE = 30.0f;
    public static final float EPIC_CHANCE = 15.0f;
    public static final float LEGENDARY_CHANCE = 5.0f;

    //roll the curio tier on use
    public float rollCurioTier(World worldIn) {
        double nextRoll = worldIn.rand.nextFloat() * 100;
        //roll above 95 for legendary
        if(nextRoll >= 100 - LEGENDARY_CHANCE) {
            return LEGENDARY_TIER;
        }
        //roll above 85 for epic
        else if(nextRoll >= 100 - EPIC_CHANCE) {
            return EPIC_TIER;
        }
        //roll above 70 for rare
        else if(nextRoll >= 100 - RARE_CHANCE) {
            return RARE_TIER;
        }
        //defaults to common tier if not
        else {
            return COMMON_TIER;
        }
    }

    //sets the nbt for a curio based on input set and rolled tier
    public void setCurioNbt(PlayerEntity playerIn, Hand handIn, World worldIn, String setIn, @Nullable Float tierIn) {
        //get held itemstack, which should be the input curio
        ItemStack stack = playerIn.getHeldItem(handIn);

        //grab current nbt tag or create a new one if null
        CompoundNBT newTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();

        //check if tags are present yet, which they shouldn't be if it's a newly picked up item
        //then roll tier and apply the appropriate curio set
        if(!rollCheck(newTag)) {
            //checks for forced tier, if no forced tier then roll curio tier now
            float curioTier = tierIn != null ? tierIn : rollCurioTier(worldIn);
            newTag.putFloat(BaseCurio.TIER, curioTier);
            newTag.putString(BaseCurio.SET, setIn);
            stack.setTag(newTag);
        }
    }

    //checks the input tag to see if the curio was already rolled or not
    //returns true if rolled, false if no roll data yet
    public boolean rollCheck(CompoundNBT itemTag) {
        //no item tag means no roll
        if(itemTag == null) {
            return false;
        }

        return (!itemTag.isEmpty() || itemTag.contains(BaseCurio.TIER) || itemTag.contains(BaseCurio.SET));
    }

    //get curio tier data and format for tooltip
    public ITextComponent getTierData(ItemStack curio) {
        //reads nbt data and determines the tier tooltip based off the numbers
        CompoundNBT tag = curio.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (curio.getTag() != null && curio.getTag().contains(TIER)) {
            float tier = tag.getFloat(TIER);
            String tierName;
            if(tier == COMMON_TIER) {
                tierName = TextFormatting.GRAY + "Common";
            }
            else if(tier == RARE_TIER) {
                tierName = TextFormatting.BLUE + "Rare";
            }
            else if(tier == EPIC_TIER) {
                tierName = TextFormatting.DARK_PURPLE + "Epic";
            }
            else if(tier == LEGENDARY_TIER) {
                tierName = TextFormatting.GOLD + "Legendary";
            }
            else if(tier == GROWTH_TIER) {
                tierName = TextFormatting.GREEN + "Growth";
            }
            else {
                tierName = "" + TextFormatting.BLACK + TextFormatting.OBFUSCATED + "Unknown";
            }
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Tier: " + tierName + TextFormatting.AQUA + "]");
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Somehow we failed to put a tier on this curio???");
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Set: " + TextFormatting.RED + "Bugged Item" + TextFormatting.AQUA + "]");
        }
        return data;
    }

    //get curio set name and format for tooltip
    public ITextComponent formatSetInfo(ItemStack curio) {
        //reads nbt data and determines the set name tooltip based off the numbers
        CompoundNBT tag = curio.getTag();
        ITextComponent data = ITextComponent.getTextComponentOrEmpty("");
        if (curio.getTag() != null && curio.getTag().contains(SET)) {
            //get translation text key from nbt tag
            String setTranslationText = tag.getString(SET);
            //get translation text string from key in nbt tag
            String setName = new TranslationTextComponent(setTranslationText).getString();
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Set: " + TextFormatting.DARK_AQUA + setName + TextFormatting.AQUA + "]");
        }
        else {
            MiscHelpers.debugLogger("[Base Curio] Somehow we failed to put a set on this curio???");
            data = ITextComponent.getTextComponentOrEmpty(TextFormatting.AQUA + " [" + TextFormatting.WHITE + "Set: " + TextFormatting.RED + "Bugged Item" + TextFormatting.AQUA + "]");
        }
        return data;
    }
}
