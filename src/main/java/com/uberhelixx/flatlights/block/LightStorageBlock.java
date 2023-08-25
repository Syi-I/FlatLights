package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.container.LightStorageContainer;
import com.uberhelixx.flatlights.tileentity.LightStorageTile;
import com.uberhelixx.flatlights.tileentity.ModTileEntities;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class LightStorageBlock extends Block {

    //constants for block hardness (time it takes to mine the block) and resistance (what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 1f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public LightStorageBlock() {
        super(Properties.create(Material.ROCK)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .notSolid()
                .sound(SoundType.STONE));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        String indev = "" + MiscHelpers.coloredText(TextFormatting.RED, "This block is not fully functional.");
        ITextComponent indevTooltip = ITextComponent.getTextComponentOrEmpty(indev);
        tooltip.add(indevTooltip);
        if(!FlatLightsCommonConfig.indevBlocks.get()) {
            String noPlace = "" + MiscHelpers.coloredText(TextFormatting.RED, "This block is disabled and cannot be placed.");
            ITextComponent noPlaceTooltip = ITextComponent.getTextComponentOrEmpty(noPlace);
            tooltip.add(noPlaceTooltip);
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(!player.isCrouching()) {
                if(tileEntity instanceof LightStorageTile) {
                    INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
                    NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());
                    ((LightStorageTile) tileEntity).playSound(SoundEvents.BLOCK_ENDER_CHEST_OPEN);
                }
                else {
                    throw new IllegalStateException("Container provider missing :skull:");
                }
            }
        }
        return ActionResultType.SUCCESS;
    }

    private INamedContainerProvider createContainerProvider(World worldIn, BlockPos pos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen.flatlights.light_storage");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new LightStorageContainer(i, worldIn, pos, playerInventory, playerEntity);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.LIGHT_STORAGE_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            // drops everything in the inventory
            worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                for (int i = 0; i < h.getSlots(); i++) {
                    spawnAsEntity(worldIn, pos, h.getStackInSlot(i));
                }
            });
            worldIn.removeTileEntity(pos);
        }
    }
}
