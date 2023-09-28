package com.uberhelixx.flatlights.event;

import com.uberhelixx.flatlights.FlatLightsClientConfig;
import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.util.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BlockEvents {

    public static void craftingTableSound (PlayerInteractEvent.RightClickBlock event) {
        //get world
        World world = event.getWorld();

        //getting the specific block that the player interacted with
        BlockPos clickedBlockRT = event.getHitVec().getPos();
        BlockState clickedBlockState = world.getBlockState(clickedBlockRT);
        Block clickedBlock = clickedBlockState.getBlock();
        PlayerEntity player = event.getPlayer();

        //sound event chance from config
        double chance = (double) FlatLightsCommonConfig.craftingJumpscareChance.get() / 100;

        //check if clientside, if the block was a crafting table, pass chance check from config value, enabled on clientside config?
        if(world.isRemote() && clickedBlock instanceof CraftingTableBlock && Math.random() <= chance && FlatLightsClientConfig.craftingTableJumpscare.get()) {
            world.playSound(player.getPosX(), player.getPosY(), player.getPosZ(), ModSoundEvents.CRAFTING_TABLE_INTERACT.get(), SoundCategory.PLAYERS, 0.5f, 1.0f, true);
        }
    }
}
