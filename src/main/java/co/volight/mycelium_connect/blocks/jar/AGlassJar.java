package co.volight.mycelium_connect.blocks.jar;

import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AGlassJar extends AbstractGlassBlock implements IWaterLoggable {
    public static final BooleanProperty CORK = BooleanProperty.create("cork");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AGlassJar(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    public static final VoxelShape basic_shape = Block.makeCuboidShape(3.5D, 0.0D, 3.5D, 12.5D, 13D, 12.5D);
    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return basic_shape;
    }

    @Nonnull @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidstate = context.getWorld().getFluidState(context.getPos());
        boolean isWater = fluidstate.getFluid() == Fluids.WATER;
        return super.getStateForPlacement(context).with(WATERLOGGED, isWater);
    }

    @Nonnull @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
}
