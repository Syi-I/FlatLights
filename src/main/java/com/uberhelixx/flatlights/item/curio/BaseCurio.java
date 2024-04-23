package com.uberhelixx.flatlights.item.curio;

import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;

public class BaseCurio extends Item implements ICurioItem {

    public BaseCurio(Properties properties) {
        super(properties);
    }

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

        //create new instance of the input curio
        //ItemStack droppedItem = stack.getItem().getDefaultInstance();

        //grab current nbt tag or create a new one if null
        //CompoundNBT newTag = droppedItem.getTag() != null ? droppedItem.getTag() : new CompoundNBT();
        CompoundNBT newTag = stack.getTag() != null ? stack.getTag() : new CompoundNBT();

        //check if tags are present yet, which they shouldn't be if it's a newly picked up item
        //then roll tier and apply the appropriate curio set
        if(!rollCheck(newTag)) {
            //checks for forced tier, if no forced tier then roll curio tier now
            float curioTier = tierIn != null ? tierIn : rollCurioTier(worldIn);
            newTag.putFloat(BaseCurio.TIER, curioTier);
            newTag.putString(BaseCurio.SET, setIn);
            //droppedItem.setTag(newTag);
            stack.setTag(newTag);
        }

        //assure that the item to drop exists, then give the player the item with rolled tags/stats
        //if(droppedItem != null) {
            //playerIn.dropItem(droppedItem, false, false);
            //below method doesn't work quite as well since if you have a full inventory the new item gets deleted
            //playerIn.addItemStackToInventory(droppedItem);
        //}

        //lower the stack count by 1, lets unclaimed curio be stackable while rolled ones are differentiated by nbt
        //stack.shrink(1);
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
}
