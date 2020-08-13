package co.volight.mycelium_connect.blocks.jar;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.utils.Itemization;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GlassJar extends AGlassJar implements Itemization {
    public static final String name = "glass_jar";

    public static GlassJar setup() {
        GlassJar o = new GlassJar(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid());
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public GlassJar(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(CORK, false).with(WATERLOGGED, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(CORK, WATERLOGGED);
    }

}
