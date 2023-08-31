package com.uberhelixx.flatlights.block;

import com.uberhelixx.flatlights.FlatLightsCommonConfig;
import com.uberhelixx.flatlights.container.SpectrumAnvilContainer;
import com.uberhelixx.flatlights.tileentity.ModTileEntities;
import com.uberhelixx.flatlights.tileentity.SpectrumAnvilTile;
import com.uberhelixx.flatlights.util.MiscHelpers;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.ToIntFunction;

public class SpectrumAnvilBlock extends AnvilBlock {
    //light level, change the number to whatever light level value from 0-15
    public static ToIntFunction<BlockState> ANVIL_LIGHT_LEVEL = BlockState -> 7;
    //also constants for block hardness(time it takes to mine the block) and resistance(what level explosions and such can destroy the block)
    //lower hardness = lower mining time required
    static final float BLOCK_HARDNESS = 5f;
    //higher resistance = less stuff can destroy it, 36000000 is bedrock hardness? so this is currently very balanced:tm:
    static final float BLOCK_RESISTANCE = 100000000f;

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private static final VoxelShape PART_BASE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
    private static final VoxelShape PART_LOWER_X = Block.makeCuboidShape(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D);
    private static final VoxelShape PART_MID_X = Block.makeCuboidShape(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D);
    private static final VoxelShape PART_UPPER_X = Block.makeCuboidShape(0.0D, 9.5D, 3.0D, 16.0D, 16.0D, 13.0D);
    private static final VoxelShape PART_LOWER_Z = Block.makeCuboidShape(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D);
    private static final VoxelShape PART_MID_Z = Block.makeCuboidShape(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D);
    private static final VoxelShape PART_UPPER_Z = Block.makeCuboidShape(3.0D, 9.5D, 0.0D, 13.0D, 16.0D, 16.0D);
    private static final VoxelShape X_AXIS_AABB = VoxelShapes.or(PART_BASE, PART_LOWER_X, PART_MID_X, PART_UPPER_X);
    private static final VoxelShape Z_AXIS_AABB = VoxelShapes.or(PART_BASE, PART_LOWER_Z, PART_MID_Z, PART_UPPER_Z);
    //private static final ITextComponent containerName = ITextComponent.getTextComponentOrEmpty("Spectrum Anvil");

    public SpectrumAnvilBlock() {
        super(Properties.create(Material.ANVIL)
                .hardnessAndResistance(BLOCK_HARDNESS, BLOCK_RESISTANCE)
                .setLightLevel(ANVIL_LIGHT_LEVEL));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);
        return direction.getAxis() == Direction.Axis.X ? X_AXIS_AABB : Z_AXIS_AABB;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isRemote()) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if(!player.isCrouching()) {
                if(tileEntity instanceof SpectrumAnvilTile) {
                    INamedContainerProvider containerProvider = createContainerProvider(worldIn, pos);
                    NetworkHooks.openGui(((ServerPlayerEntity) player), containerProvider, tileEntity.getPos());
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
                return new TranslationTextComponent("screen.flatlights.spectrum_anvil");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new SpectrumAnvilContainer(i, playerInventory);
            }
        };
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.SPECTRUM_ANVIL_TILE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
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
}
