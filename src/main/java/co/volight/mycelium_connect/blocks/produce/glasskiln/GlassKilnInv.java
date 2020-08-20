package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.inventorys.CraftInv;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GlassKilnInv implements IInventory {

    private ItemStack output = ItemStack.EMPTY;
    private ItemStack fuel = ItemStack.EMPTY;
    private final CraftInv items = new CraftInv(GlassKilnTileEntity.invWidth, GlassKilnTileEntity.invHeight);

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getFuel() {
        return fuel;
    }

    public CraftInv getItems() {
        return items;
    }

    @Override
    public int getSizeInventory() {
        return GlassKilnTileEntity.invSize;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty() && output.isEmpty() && fuel.isEmpty();
    }

    @Nonnull @Override
    public ItemStack getStackInSlot(int index) {
        if (index == GlassKilnTileEntity.slotOutput) return output;
        if (index == GlassKilnTileEntity.slotFuel) return fuel;
        return items.getStackInSlot(index - GlassKilnTileEntity.invItemsOffset);
    }

    @Nonnull @Override
    public ItemStack decrStackSize(int index, int count) {
        if (count <= 0) return ItemStack.EMPTY;
        if (index == GlassKilnTileEntity.slotOutput) return output.split(count);
        if (index == GlassKilnTileEntity.slotFuel) return fuel.split(count);
        return items.decrStackSize(index - GlassKilnTileEntity.invItemsOffset, count);
    }

    @Nonnull @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack result;
        if (index == GlassKilnTileEntity.slotOutput) {
            result = output;
            output = ItemStack.EMPTY;
            return  result;
        }
        if (index == GlassKilnTileEntity.slotFuel) {
            result = fuel;
            fuel = ItemStack.EMPTY;
            return  result;
        }
        return items.removeStackFromSlot(index - GlassKilnTileEntity.invItemsOffset);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        if (index == GlassKilnTileEntity.slotOutput) output = stack;
        else if (index == GlassKilnTileEntity.slotFuel) fuel = stack;
        else items.setInventorySlotContents(index - GlassKilnTileEntity.invItemsOffset, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public void markDirty() { }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        output = ItemStack.EMPTY;
        fuel = ItemStack.EMPTY;
        this.items.clear();
    }
}
