package com.uberhelixx.flatlights.common.blockentity;

import com.uberhelixx.flatlights.client.screen.SpectralizerMenu;
import com.uberhelixx.flatlights.common.recipe.SpectralizerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SpectralizerBE extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private static final int INPUT_SLOT1 = 0;
    private static final int INPUT_SLOT2 = 1;
    private static final int INPUT_SLOT3 = 2;
    private static final int INPUT_SLOT4 = 3;
    private static final int INPUT_SLOT5 = 4;
    private static final int INPUT_SLOT6 = 5;
    private static final int OUTPUT_SLOT = 6;
    
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;
    public SpectralizerBE(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SPECTRALIZER_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            //get and set any data the BE has to synchronize between client and server
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> SpectralizerBE.this.progress;
                    case 1 -> SpectralizerBE.this.maxProgress;
                    default -> 0;
                };
            }
            
            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> SpectralizerBE.this.progress = pValue;
                    case 1 -> SpectralizerBE.this.maxProgress = pValue;
                }
            }
            
            //total number of values the BE is synchronizing (i.e. progress and maxProgress)
            @Override
            public int getCount() {
                return 2;
            }
        };
    }
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.flatlights.spectralizer");
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new SpectralizerMenu(i, inventory, this, this.data);
    }
    
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        
        return super.getCapability(cap, side);
    }
    
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
    
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("spectralizer.progress", progress);
        super.saveAdditional(pTag);
    }
    
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("spectralizer.progress");
    }
    
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);
            
            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        }
        else {
            resetProgress();
        }
    }
    
    private void resetProgress() {
        progress = 0;
    }
    
    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }
    
    private void increaseCraftingProgress() {
        progress++;
    }
    
    private void craftItem() {
        Optional<SpectralizerRecipe> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().getResultItem(null);
        
        this.itemHandler.extractItem(INPUT_SLOT1, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT2, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT3, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT4, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT5, 1, false);
        this.itemHandler.extractItem(INPUT_SLOT6, 1, false);
        
        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }
    
    private boolean hasRecipe() {
        Optional<SpectralizerRecipe> recipe = getCurrentRecipe();
        
        if(recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());
        
        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }
    
    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }
    
    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
    
    private Optional<SpectralizerRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }
        
        return this.level.getRecipeManager().getRecipeFor(SpectralizerRecipe.Type.INSTANCE, inventory, level);
    }
}
