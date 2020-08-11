package co.volight.mycelium_connect;

import co.volight.mycelium_connect.blocks.MyceliumDirt;
import co.volight.mycelium_connect.blocks.fungi.Fungi;
import co.volight.mycelium_connect.blocks.jar.GlassJar;
import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKiln;
import co.volight.mycelium_connect.utils.Itemization;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ObjectHolder;
import javax.annotation.Nullable;
import java.util.Objects;

@ObjectHolder(MCC.ID)
public class MCCBlocks {

    public static <T extends Block & Itemization> BlockItem makeBlockItem(T block) {
        Item.Properties properties = new Item.Properties();
        block.itemization(properties);
        BlockItem blockItem = new BlockItem(block, properties);
        blockItem.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        return blockItem;
    }

    @ObjectHolder(Fungi.name)
    public static final Fungi fungi = null;

    @ObjectHolder(MyceliumDirt.name)
    public static final MyceliumDirt myceliumDirt = null;

    @ObjectHolder(GlassKiln.name)
    public static final GlassKiln glassKiln = null;

    @ObjectHolder(GlassJar.name)
    public static final GlassJar glassJar = null;
}
