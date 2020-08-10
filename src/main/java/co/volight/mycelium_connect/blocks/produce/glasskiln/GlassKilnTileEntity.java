package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class GlassKilnTileEntity extends AbstractFurnaceTileEntity {
    public static final String name = GlassKiln.name;
    public static final TileEntityType<GlassKilnTileEntity> type = TileEntityType.Builder.create(GlassKilnTileEntity::new, MCCBlocks.glassKiln).build(null);
    static {
        type.setRegistryName(MCC.ID, name);
    }

    protected GlassKilnTileEntity() {
        super(type, IRecipeType.SMELTING);
    }

    @Nonnull @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + name);
    }

    @Nonnull @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return null;
    }
}
