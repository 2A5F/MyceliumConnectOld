package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCRecipeType;
import co.volight.mycelium_connect.MCCStats;
import co.volight.mycelium_connect.recipes.GlassKilnSmeltingRecipe;
import co.volight.mycelium_connect.utils.Itemization;
import co.volight.mycelium_connect.utils.RegistrySetup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import javax.annotation.Nonnull;

public class GlassKiln extends AbstractFurnaceBlock implements Itemization, RegistrySetup {
    public static final String name = "glass_kiln";

    public static final IRecipeType<GlassKilnSmeltingRecipe> recipeType = MCCRecipeType.glassKilnSmelting;

    public static GlassKiln setup() {
        return new GlassKiln(
                Properties.create(Material.IRON, MaterialColor.IRON).hardnessAndResistance(5.0F, 3.0F).sound(SoundType.METAL)
        ).regName(name);
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

    @Override
    public void onBlockPlacedBy(@Nonnull World worldIn, @Nonnull BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof GlassKilnTileEntity) {
                ((GlassKilnTileEntity)te).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.isIn(newState.getBlock())) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof GlassKilnTileEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (GlassKilnTileEntity)te);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }
}
