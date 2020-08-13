package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCStats;
import co.volight.mycelium_connect.utils.Itemization;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import javax.annotation.Nonnull;

public class GlassKiln extends AbstractFurnaceBlock implements Itemization {
    public static final String name = "glass_kiln";

    public static final IRecipeType<? extends AbstractCookingRecipe> recipeType = IRecipeType.SMELTING;

    public static GlassKiln setup() {
        GlassKiln o = new GlassKiln(Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(5.0F, 3.0F).sound(SoundType.METAL));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public GlassKiln(Properties properties) {
        super(properties);
    }

    @Override
    protected void interactWith(World worldIn, @Nonnull BlockPos pos, @Nonnull PlayerEntity player) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof GlassKilnTileEntity) {
            player.openContainer((INamedContainerProvider)tileEntity);
            player.addStat(MCCStats.interactWithGlassKiln);
        }
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull IBlockReader worldIn) {
        return new GlassKilnTileEntity();
    }


}
