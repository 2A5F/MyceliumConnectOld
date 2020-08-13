package co.volight.mycelium_connect.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;

public class LockableSlot extends Slot {
    private final IsLock isLock;

    public LockableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, () -> false);
    }

    public LockableSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, IsLock isLock) {
        super(inventoryIn, index, xPosition, yPosition);
        this.isLock = isLock;
    }

    public interface IsLock {
        boolean isLock();
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return !isLock.isLock();
    }

}
