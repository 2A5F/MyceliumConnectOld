package co.volight.mycelium_connect.inventorys;

import co.volight.math.Range;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import javax.annotation.Nonnull;
import java.util.List;

public class SpanInventory implements IInventory {
    NonNullList<ItemStack> items;
    Range range;
    private List<IInventoryChangedListener> listeners;

    public SpanInventory(NonNullList<ItemStack> items, Range range) {
        this.items = items;
        this.range = range;
        assert range.size() <= items.size();
        assert range.size() > 0;
    }

    public void addListener(IInventoryChangedListener listener) {
        if (this.listeners == null) {
            this.listeners = Lists.newArrayList();
        }
        this.listeners.add(listener);
    }

    public void removeListener(IInventoryChangedListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public int getSizeInventory() {
        return range.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) return false;
        }
        return true;
    }

    protected int rawIndex(int index) {
        return index + range.from;
    }

    @Nonnull @Override
    public ItemStack getStackInSlot(int index) {
        if (index >= range.size() || index < 0) return ItemStack.EMPTY;
        return items.get(rawIndex(index));
    }

    @Nonnull @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.items, rawIndex(index), count);
        if (!itemstack.isEmpty()) {
            this.markDirty();
        }
        return itemstack;
    }

    @Nonnull @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack itemstack = this.items.get(rawIndex(index));
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(rawIndex(index), ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.items.set(rawIndex(index), stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public void markDirty() {
        if (this.listeners != null) {
            for(IInventoryChangedListener iinventorychangedlistener : this.listeners) {
                iinventorychangedlistener.onInventoryChanged(this);
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        for (int i : range) {
            items.set(i, ItemStack.EMPTY);
        }
        this.markDirty();
    }
}
