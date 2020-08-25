package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.MCCPackets;
import co.volight.mycelium_connect.api.ICanGnite;
import co.volight.mycelium_connect.inventorys.CraftInv;
import co.volight.mycelium_connect.msg.GniteMsg;
import co.volight.mycelium_connect.recipes.GlassKilnSmeltingRecipe;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GlassKilnTileEntity extends LockableTileEntity implements ISidedInventory, ITickableTileEntity, CraftInv.OnCraftMatrixChanged {
    public static final String name = GlassKiln.name;
    public static final TileEntityType<GlassKilnTileEntity> type = TileEntityType.Builder.create(GlassKilnTileEntity::new, MCCBlocks.glassKiln).build(null);
    static { type.setRegistryName(MCC.ID, name); }

    public static final IRecipeType<GlassKilnSmeltingRecipe> recipeType = GlassKiln.recipeType;

    public static final int invWidth = 3;
    public static final int invHeight = 3;
    public static final int invItemsSize = invWidth * invHeight;
    public static final int invItemsOffset = 2;
    public static final int invSize = invItemsSize + invItemsOffset;

    public static final int slotOutput = 0;
    public static final int slotFuel = 1;
    public static final int[] slotItems = new int[invItemsSize];
    static {
        for (int i = 0; i < invItemsSize; ++i) {
            slotItems[i] = i + invItemsOffset;
        }
    }

    private static final int[] SLOTS_UP = slotItems;
    private static final int[] SLOTS_DOWN = new int[]{slotOutput, slotFuel};
    private static final int[] SLOTS_HORIZONTAL = new int[]{slotFuel};

    public GlassKilnTileEntity() {
        super(type);
    }

    private GlassKilnData data = new GlassKilnData();

    private ItemStack output = ItemStack.EMPTY;
    private ItemStack fuel = ItemStack.EMPTY;
    private CraftInv items = new CraftInv(invWidth, invHeight, this);

    @Nonnull @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + MCC.ID + "." + name);
    }

    @Nonnull @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new GlassKilnContainer(id, player, this, data);
    }

    @Override
    public int getSizeInventory() {
        return invSize;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty() && output.isEmpty() && fuel.isEmpty();
    }

    @Nonnull @Override
    public ItemStack getStackInSlot(int index) {
        if (index == slotOutput) return output;
        if (index == slotFuel) return fuel;
        return items.getStackInSlot(index - invItemsOffset);
    }

    @Nonnull @Override
    public ItemStack decrStackSize(int index, int count) {
        if (count <= 0) return ItemStack.EMPTY;
        if (index == slotOutput) return output.split(count);
        if (index == slotFuel) return fuel.split(count);
        return items.decrStackSize(index - invItemsOffset, count);
    }

    @Nonnull @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack result;
        if (index == slotOutput) {
            result = output;
            output = ItemStack.EMPTY;
            return  result;
        }
        if (index == slotFuel) {
            result = fuel;
            fuel = ItemStack.EMPTY;
            return  result;
        }
        return items.removeStackFromSlot(index - invItemsOffset);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        if (index == slotOutput) output = stack;
        else if (index == slotFuel) fuel = stack;
        else items.setInventorySlotContents(index - invItemsOffset, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        if (world.getTileEntity(pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        output = ItemStack.EMPTY;
        fuel = ItemStack.EMPTY;
        this.items.clear();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.data.burnTime = nbt.getInt("BurnTime");
        this.data.fuelTime = nbt.getInt("FuelTime");
        this.data.cookTime = nbt.getInt("CookTime");
        this.data.cookTimeTotal = nbt.getInt("CookTimeTotal");
        this.data.isCooking = nbt.getBoolean("IsCooking");
        this.items = new CraftInv(invWidth, invHeight, this).LoadFromNBT(nbt);
        this.fuel = readItem(nbt, "Fuel");
        this.output = readItem(nbt, "Output");
    }

    // write nbt
    @Nonnull @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putShort("BurnTime", (short) this.data.burnTime);
        nbt.putShort("FuelTime", (short) this.data.fuelTime);
        nbt.putShort("CookTime", (short) this.data.cookTime);
        nbt.putShort("CookTimeTotal", (short) this.data.cookTimeTotal);
        nbt.putBoolean("IsCooking", this.data.isCooking);
        this.items.SaveToNBT(nbt);
        writeItem(nbt, "Fuel", this.fuel);
        writeItem(nbt, "Output", this.output);
        return nbt;
    }

    private ItemStack readItem(CompoundNBT nbt, String name) {
        CompoundNBT itemNbt = nbt.getCompound(name);
        return ItemStack.read(itemNbt);
    }

    private void writeItem(CompoundNBT nbt, String name, ItemStack item) {
        if (item.isEmpty()) return;
        CompoundNBT itemNbt = new CompoundNBT();
        item.write(itemNbt);
        nbt.put(name, itemNbt);
    }

    public static int getBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        return ForgeHooks.getBurnTime(fuel);
    }

    protected int getCookTime() {
        return this.world.getRecipeManager().getRecipe(recipeType, this.items, this.world).map(GlassKilnSmeltingRecipe::getCookTime).orElse(GlassKilnSmeltingRecipe.SERIALIZER.getDefaultCookingTime());
    }

    public boolean isBurning() {
        return this.data.burnTime > 0;
    }

    @Override
    public void tick() {
        boolean lastBurning = isBurning();
        boolean needUpdate = false;

        if (isBurning()) --this.data.burnTime;

        if (!this.world.isRemote) {
            IRecipe<CraftInv> recipe = this.world.getRecipeManager().getRecipe(recipeType, items, this.world).orElse(null);

            if (this.data.isCooking) {
                boolean canmade = canMade(recipe);

                if (canmade) {
                    this.data.cookTimeTotal = getCookTime();
                }

                if (canmade && !isBurning()) {
                    int fuelTime = getBurnTime(fuel);
                    this.data.burnTime = fuelTime;
                    this.data.fuelTime = fuelTime;

                    if (isBurning()) {
                        needUpdate = true;

                        if (fuel.hasContainerItem()) fuel = fuel.getContainerItem();
                        else if (!fuel.isEmpty()) {
                            fuel.shrink(1);
                            if (fuel.isEmpty()) fuel = fuel.getContainerItem();
                        }
                    }
                }

                if (canmade && isBurning()) {
                    ++this.data.cookTime;

                    if (this.data.cookTime >= this.data.cookTimeTotal) {
                        this.data.cookTime = 0;

                        made(recipe);
                        needUpdate = true;
                    }
                }

                if(!canmade) {
                    this.data.isCooking = false;
                    this.data.cookTime = 0;
                    needUpdate = true;
                }
            }

        }

        if (lastBurning != isBurning()) {
            needUpdate = true;
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(GlassKiln.LIT, isBurning()), 3);
        }

        if (needUpdate) markDirty();
    }

    @Override
    public void onCraftMatrixChanged(CraftInv inv) {
        this.data.isCooking = false;
        this.data.cookTime = 0;
        markDirty();
    }

    public boolean canMade(IRecipe<CraftInv> recipe) {
        if (items.isEmpty() || recipe == null) return false;
        ItemStack result = recipe.getRecipeOutput();
        if (result.isEmpty()) return false;
        if (output.isEmpty()) return true;
        if (!output.isItemEqual(result)) return false;
        int count = output.getCount() + result.getCount();
        if (count <= getInventoryStackLimit() && count <= output.getMaxStackSize()) return true;
        return count <= result.getMaxStackSize();
    }

    public void made(IRecipe<CraftInv> recipe) {
        if (recipe == null || !canMade(recipe)) return;
        ItemStack result = recipe.getRecipeOutput();
        if (output.isEmpty()) {
            output = result.copy();
        } else if (output.isItemEqual(result)) {
            output.grow(result.getCount());
        }

        // if (!this.world.isRemote) {
        //     this.setRecipeUsed(recipe);
        // }
        recipe.getCraftingResult(items);
    }

    @Override
    public void markDirty() {
        super.markDirty();
    }

    @Nonnull @Override
    public int[] getSlotsForFace(@Nonnull Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return side == Direction.UP ? SLOTS_UP : SLOTS_HORIZONTAL;
        }
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
        return isItemValidForSlot(index, stack);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        return direction != Direction.DOWN || index != slotFuel;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == slotOutput) {
            return false;
        } else if (index == slotFuel) {
            return isFuel(stack);
        } else {
            ItemStack s = this.items.getStackInSlot(index - invItemsOffset);
            return !this.data.isCooking && s.isItemEqual(stack);
        }
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(@Nonnull net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }
}
