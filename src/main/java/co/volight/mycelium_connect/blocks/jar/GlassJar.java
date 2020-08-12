package co.volight.mycelium_connect.blocks.jar;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.utils.Itemization;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;

public class GlassJar extends AbstractGlassBlock implements Itemization {
    public static final String name = "glass_jar";

    public static final BooleanProperty CORK = BooleanProperty.create("cork");

    public GlassJar() {
        this(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid());
    }

    public GlassJar(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CORK, false));
        setRegistryName(MCC.ID, name);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CORK);
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    public static final VoxelShape shape = Block.makeCuboidShape(3.5D, 0.0D, 3.5D, 12.5D, 13D, 12.5D);
    @Nonnull @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }
}
