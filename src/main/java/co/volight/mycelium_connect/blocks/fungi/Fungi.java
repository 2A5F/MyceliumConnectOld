package co.volight.mycelium_connect.blocks.fungi;

import co.volight.math.VLMath;
import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.utils.Itemization;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Random;

public class Fungi extends Block implements Itemization {
    public static final String name = "fungi";

    public static final EnumProperty<FungiSide> EAST = FungiStateProperties.EAST;
    public static final EnumProperty<FungiSide> NORTH = FungiStateProperties.NORTH;
    public static final EnumProperty<FungiSide> SOUTH = FungiStateProperties.SOUTH;
    public static final EnumProperty<FungiSide> WEST = FungiStateProperties.WEST;

    public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
    public static final Map<Direction, EnumProperty<FungiSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, NORTH,
            Direction.EAST, EAST,
            Direction.SOUTH, SOUTH,
            Direction.WEST, WEST
    ));

    public static final Vector3f[] colors = new Vector3f[16];
    static {
        colors[15] = VLMath.RGBi2f(102, 165, 183);
        colors[0] = VLMath.RGBi2f(72, 82, 85);
        for(int i = 1; i <= 14; ++i) {
            float r = VLMath.ReMap(i, 0.0f, 15f, VLMath.RGBi2f(66), VLMath.RGBi2f(84));
            float g = VLMath.ReMap(i, 0.0f, 15f, VLMath.RGBi2f(90), VLMath.RGBi2f(158));
            float b = VLMath.ReMap(i, 0.0f, 15f, VLMath.RGBi2f(95), VLMath.RGBi2f(190));
            colors[i] = new Vector3f(r, g, b);
        }
    }

    public static Fungi setup() {
        Fungi o = new Fungi(Block.Properties.create(Material.MISCELLANEOUS, MaterialColor.BLUE).doesNotBlockMovement().hardnessAndResistance(0.25f));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public Fungi(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, FungiSide.NONE).with(EAST, FungiSide.NONE)
                .with(SOUTH, FungiSide.NONE).with(WEST, FungiSide.NONE)
                .with(POWER, 0)
        );

        initShapes();
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, POWER);
    }

    @Override
    public boolean isTransparent(BlockState state) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        int i = stateIn.get(POWER);
        if (i != 0) {
            for(Direction direction : Direction.Plane.HORIZONTAL) {
                FungiSide side = stateIn.get(FACING_PROPERTY_MAP.get(direction));
                switch(side) {
                    case UP:
                        this.genParticle(worldIn, rand, pos, colors[i], direction, Direction.UP, -0.5F, 0.5F);
                    case SIDE:
                        this.genParticle(worldIn, rand, pos, colors[i], Direction.DOWN, direction, 0.0F, 0.5F);
                        break;
                    case NONE:
                    default:
                        this.genParticle(worldIn, rand, pos, colors[i], Direction.DOWN, direction, 0.0F, 0.3F);
                }
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    private void genParticle(World world, Random rand, BlockPos pos, Vector3f color, Direction dirF, Direction dirT, float o1, float o2) {
        float f = o2 - o1;
        if (!(rand.nextFloat() >= 0.2F * f)) {
            float f1 = 0.4375F;
            float f2 = o1 + f * rand.nextFloat();
            double d0 = 0.5D + (double)(0.4375F * (float)dirF.getXOffset()) + (double)(f2 * (float)dirT.getXOffset());
            double d1 = 0.5D + (double)(0.4375F * (float)dirF.getYOffset()) + (double)(f2 * (float)dirT.getYOffset());
            double d2 = 0.5D + (double)(0.4375F * (float)dirF.getZOffset()) + (double)(f2 * (float)dirT.getZOffset());

            world.addParticle(
                    new RedstoneParticleData(color.getX(), color.getY(), color.getZ(), 1.0F),
                    (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2,
                    0.0D, 0.0D, 0.0D
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(int power) {
        Vector3f color = colors[power];
        return MathHelper.rgb(color.getX(), color.getY(), color.getZ());
    }

    private static final VoxelShape centerShape = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);
    private static final Map<Direction, VoxelShape> sideShape = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 1.0D, 14.0D),
            Direction.SOUTH, Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 16.0D),
            Direction.EAST, Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 16.0D, 1.0D, 14.0D),
            Direction.WEST, Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D)
    ));
    private static final Map<Direction, VoxelShape> upShape = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, VoxelShapes.or(sideShape.get(Direction.NORTH),
                    Block.makeCuboidShape(2.0D, 0.0D, 0.0D, 14.0D, 16.0D, 1.0D)),
            Direction.SOUTH, VoxelShapes.or(sideShape.get(Direction.SOUTH),
                    Block.makeCuboidShape(2.0D, 0.0D, 15.0D, 14.0D, 16.0D, 16.0D)),
            Direction.EAST, VoxelShapes.or(sideShape.get(Direction.EAST),
                    Block.makeCuboidShape(15.0D, 0.0D, 2.0D, 16.0D, 16.0D, 14.0D)),
            Direction.WEST, VoxelShapes.or(sideShape.get(Direction.WEST),
                    Block.makeCuboidShape(0.0D, 0.0D, 2.0D, 1.0D, 16.0D, 14.0D))
    ));
    private final Map<BlockState, VoxelShape> shapes = Maps.newHashMap();

    private void initShapes() {
        for(BlockState blockstate : this.getStateContainer().getValidStates()) {
            if (blockstate.get(POWER) == 0) {
                this.shapes.put(blockstate, this.genShape(blockstate));
            }
        }
    }

    private VoxelShape genShape(BlockState state) {
        VoxelShape voxelshape = centerShape;

        for(Direction direction : Direction.Plane.HORIZONTAL) {
            FungiSide side = state.get(FACING_PROPERTY_MAP.get(direction));
            if (side == FungiSide.SIDE) {
                voxelshape = VoxelShapes.or(voxelshape, sideShape.get(direction));
            } else if (side == FungiSide.UP) {
                voxelshape = VoxelShapes.or(voxelshape, upShape.get(direction));
            }
        }

        return voxelshape;
    }

    @Nonnull
    @Deprecated
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.shapes.get(state.with(POWER, 0));
    }
}
