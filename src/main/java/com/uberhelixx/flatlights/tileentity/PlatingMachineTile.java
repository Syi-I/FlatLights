package com.uberhelixx.flatlights.tileentity;

import com.uberhelixx.flatlights.block.ModBlocks;
import com.uberhelixx.flatlights.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;


public class PlatingMachineTile extends TileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public PlatingMachineTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return stack.getItem() == ModBlocks.FLATLIGHT_BLACK_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_BLUE_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_BROWN_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_CYAN_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_GRAY_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_GREEN_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_LIGHT_BLUE_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_LIGHT_GRAY_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_LIME_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_MAGENTA_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_ORANGE_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_PINK_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_PURPLE_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_RED_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_WHITE_BLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.FLATLIGHT_YELLOW_BLOCK.get().asItem();
                    case 1: return stack.getItem() == Items.IRON_INGOT;
                    default:
                        return false;
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

}
