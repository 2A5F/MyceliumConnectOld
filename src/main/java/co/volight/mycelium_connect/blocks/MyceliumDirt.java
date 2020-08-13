package co.volight.mycelium_connect.blocks;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import co.volight.mycelium_connect.blocks.fungi.FungiSide;
import co.volight.mycelium_connect.utils.Itemization;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MyceliumDirt extends Block implements Itemization {
    public static final String name = "mycelium_dirt";

    public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;

    public static final Vector3f[] colors = Fungi.colors;

    public static MyceliumDirt setup() {
        MyceliumDirt o = new MyceliumDirt(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.GROUND));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public MyceliumDirt(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(POWER, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWER);
    }

    @OnlyIn(Dist.CLIENT)
    public static int getColor(int power) {
        Vector3f color = colors[power];
        return MathHelper.rgb(color.getX(), color.getY(), color.getZ());
    }

}
