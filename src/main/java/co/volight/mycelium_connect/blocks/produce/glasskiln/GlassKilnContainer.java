package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.api.INeedFuel;
import co.volight.mycelium_connect.slots.FuelSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class GlassKilnContainer extends RecipeBookContainer<IInventory> implements INeedFuel {
    public static final String name = GlassKiln.name;
    public static final ContainerType<GlassKilnContainer> type = new ContainerType<>(GlassKilnContainer::new);
    static { type.setRegistryName(MCC.ID, name); }
    public static final IRecipeType<? extends AbstractCookingRecipe> recipeType = GlassKilnTileEntity.recipeType;
    public static final int width = GlassKilnTileEntity.invWidth;
    public static final int height = GlassKilnTileEntity.invHeight;
    public static final int size = GlassKilnTileEntity.invSize;

    private final IInventory selfInventory;
    private final IIntArray selfData;
    protected final World world;

    public GlassKilnContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(size), new IntArray(4));
    }

    public GlassKilnContainer(int id, PlayerInventory playerInventory, IInventory selfInventory, IIntArray selfData) {
        super(type, id);
        assertInventorySize(selfInventory, size);
        assertIntArraySize(selfData, 4);
        this.selfInventory = selfInventory;
        this.selfData = selfData;
        this.world = playerInventory.player.world;

        this.addSlot(new FurnaceResultSlot(playerInventory.player, selfInventory, GlassKilnTileEntity.slotOutput, 136, 27));
        this.addSlot(new FuelSlot<>(this, selfInventory, GlassKilnTileEntity.slotFuel, 82, 27));
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                this.addSlot(new Slot(selfInventory, x + y * width + 2, 20 + x * 18, 17 + y * 18));
            }
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(selfData);
    }

    @Override
    public void fillStackedContents(@Nonnull RecipeItemHelper itemHelperIn) {
        if (selfInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)selfInventory).fillStackedContents(itemHelperIn);
        }
    }

    @Override
    public void clear() {
        selfInventory.clear();
    }

    @Override
    public boolean matches(IRecipe<? super IInventory> recipeIn) {
        return recipeIn.matches(selfInventory, world);
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @OnlyIn(Dist.CLIENT) @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean canInteractWith(@Nonnull PlayerEntity playerIn) {
        return this.selfInventory.isUsableByPlayer(playerIn);
    }

    public boolean hasRecipe(ItemStack stack) {
        return world.getRecipeManager().getRecipe((IRecipeType)recipeType, new Inventory(stack), world).isPresent();
    }

    @Override
    public boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack) > 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.selfData.get(2);
        int j = this.selfData.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.selfData.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.selfData.get(0) * 13 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.selfData.get(0) > 0;
    }

}
