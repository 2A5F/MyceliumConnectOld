package co.volight.mycelium_connect.inventorys;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import javax.annotation.Nonnull;

public class CraftInv implements IInventory, IRecipeHelperPopulator {
    private final NonNullList<ItemStack> stackList;
    private final int width;
    private final int height;
    private final OnCraftMatrixChanged eventHandler;

    public CraftInv(int width, int height) {
        this(width, height, (CraftInv inv) -> {});
    }
    public CraftInv(int width, int height, OnCraftMatrixChanged eventHandler) {
        this.stackList = NonNullList.withSize(width * height, ItemStack.EMPTY);
        this.width = width;
        this.height = height;
        this.eventHandler = eventHandler;
    }

    @Nonnull
    public CraftInv LoadFromNBT(CompoundNBT nbt) {
        ItemStackHelper.loadAllItems(nbt, this.stackList);
        return this;
    }

    @Nonnull
    public CraftInv SaveToNBT(CompoundNBT nbt) {
        ItemStackHelper.saveAllItems(nbt, this.stackList);
        return this;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return this.stackList.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.stackList) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Nonnull @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : this.stackList.get(index);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nonnull @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.stackList, index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nonnull @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);
        if (!itemstack.isEmpty()) {
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.stackList.set(index, stack);
        this.eventHandler.onCraftMatrixChanged(this);
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    @Override
    public void markDirty() { }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.stackList.clear();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public void fillStackedContents(@Nonnull RecipeItemHelper helper) {
        for(ItemStack itemstack : this.stackList) {
            helper.accountPlainStack(itemstack);
        }

    }

    interface OnCraftMatrixChanged {
        void onCraftMatrixChanged(CraftInv inv);
    }
}
