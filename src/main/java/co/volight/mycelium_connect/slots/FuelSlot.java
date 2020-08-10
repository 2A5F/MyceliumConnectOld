package co.volight.mycelium_connect.slots;

import co.volight.mycelium_connect.api.INeedFuel;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class FuelSlot<T extends INeedFuel> extends Slot {
    private final T handler;

    public FuelSlot(T handler, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.handler = handler;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.handler.isFuel(stack) || isBucket(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}
