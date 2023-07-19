package com.uberhelixx.flatlights.tileentity;

import com.uberhelixx.flatlights.block.ModBlocks;
import net.minecraft.block.BlockState;
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

    public PlatingMachineTile() {
        this(ModTileEntities.PLATING_MACHINE_TILE.get());
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
                    case 0: return stack.getItem() == ModBlocks.BLACK_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.BLUE_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.BROWN_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.CYAN_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.GRAY_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.GREEN_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.LIGHT_BLUE_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.LIGHT_GRAY_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.LIME_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.MAGENTA_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.ORANGE_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.PINK_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.PURPLE_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.RED_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.WHITE_FLATBLOCK.get().asItem() ||
                            stack.getItem() == ModBlocks.YELLOW_FLATBLOCK.get().asItem();
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
