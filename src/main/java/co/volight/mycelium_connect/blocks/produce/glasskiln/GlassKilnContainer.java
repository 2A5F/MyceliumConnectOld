package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.api.ICanGnite;
import co.volight.mycelium_connect.api.INeedFuel;
import co.volight.mycelium_connect.slots.FuelSlot;
import co.volight.mycelium_connect.slots.LockableSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class GlassKilnContainer extends RecipeBookContainer<IInventory> implements INeedFuel, ICanGnite {
    public static final String name = GlassKiln.name;
    public static final ContainerType<GlassKilnContainer> type = new ContainerType<>(GlassKilnContainer::new);
    static { type.setRegistryName(MCC.ID, name); }
    public static final IRecipeType<? extends AbstractCookingRecipe> recipeType = GlassKilnTileEntity.recipeType;
    public static final int width = GlassKilnTileEntity.invWidth;
    public static final int height = GlassKilnTileEntity.invHeight;
    public static final int size = GlassKilnTileEntity.invSize;

    public static final int playBagWidth = 9;
    public static final int playBagHeight = 3;
    public static final int playBagSize = playBagWidth * playBagHeight;
    public static final int playInvSize = playBagSize + playBagWidth;


    private final IInventory selfInventory;
    private final GlassKilnData data;
    protected final World world;

    public GlassKilnContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new Inventory(size), new GlassKilnData());
    }

    public GlassKilnContainer(int id, PlayerInventory playerInventory, IInventory selfInventory, GlassKilnData data) {
        super(type, id);
        assertInventorySize(selfInventory, size);
        this.selfInventory = selfInventory;
        this.data = data;
        this.world = playerInventory.player.world;

        this.addSlot(new FurnaceResultSlot(playerInventory.player, selfInventory, GlassKilnTileEntity.slotOutput, 136, 27));
        this.addSlot(new FuelSlot<>(this, selfInventory, GlassKilnTileEntity.slotFuel, 82, 27));
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                this.addSlot(new LockableSlot(selfInventory, x + y * width + GlassKilnTileEntity.invItemsOffset, 20 + x * 18, 17 + y * 18, this::isCooking));
            }
        }

        for(int i = 0; i < playBagHeight; ++i) {
            for(int j = 0; j < playBagWidth; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * playBagWidth + playBagWidth, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < playBagWidth; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(data);
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
        return GlassKilnTileEntity.slotOutput;
    }

    public int getFuelSlot() {
        return GlassKilnTileEntity.slotFuel;
    }

    public int[] getItemsSlots() {
        return GlassKilnTileEntity.slotItems;
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

    public boolean isCooking() {
        return this.data.isCooking;
    }

    public void setCooking(boolean v) {
        this.data.isCooking = v;
    }

    @Override
    public boolean getGnite() {
        return this.isCooking();
    }

    @Override
    public void setGnite(boolean v) {
        setCooking(v);
        if (selfInventory instanceof TileEntity) {
            TileEntity te = (TileEntity)selfInventory;
            te.markDirty();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int cookTime = this.data.cookTime;
        int total = this.data.cookTimeTotal;
        return total != 0 && cookTime != 0 ? cookTime * 24 / total : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int fuelTime = this.data.fuelTime;
        if (fuelTime == 0) {
            fuelTime = 200;
        }
        return this.data.burnTime * 13 / fuelTime;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isBurning() {
        return this.data.burnTime > 0;
    }

    @Nonnull @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot targetSlot = this.inventorySlots.get(index);
        if (targetSlot != null && targetSlot.getHasStack()) {
            ItemStack targetItem = targetSlot.getStack();
            result = targetItem.copy();
            int[] itemsSlots = getItemsSlots();
            if (index == getOutputSlot()) {
                if (!this.mergeItemStack(targetItem, size, size + playInvSize, true)) {
                    return ItemStack.EMPTY;
                }
                targetSlot.onSlotChange(targetItem, result);
            } else if (index != getFuelSlot() && Arrays.stream(itemsSlots).noneMatch(x -> x == index)) {
                if (this.hasRecipe(targetItem)) {
                    if (!this.mergeItemStack(targetItem, itemsSlots[0], itemsSlots[itemsSlots.length - 1], false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(targetItem)) {
                    if (!this.mergeItemStack(targetItem, getFuelSlot(), getFuelSlot() + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= size && index < size + playBagSize) {
                    if (!this.mergeItemStack(targetItem, size + playBagSize, size + playInvSize, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= size + playBagSize && index < size + playInvSize &&
                        !this.mergeItemStack(targetItem, size, size + playBagSize, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(targetItem, size, size + playInvSize, false)) {
                return ItemStack.EMPTY;
            }

            if (targetItem.isEmpty()) {
                targetSlot.putStack(ItemStack.EMPTY);
            } else {
                targetSlot.onSlotChanged();
            }

            if (targetItem.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            targetSlot.onTake(playerIn, targetItem);
        }

        return result;
    }

}
