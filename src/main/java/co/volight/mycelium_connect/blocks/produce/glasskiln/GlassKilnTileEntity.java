package co.volight.mycelium_connect.blocks.produce.glasskiln;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCBlocks;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class GlassKilnTileEntity extends LockableTileEntity implements ISidedInventory, IRecipeHolder, IRecipeHelperPopulator, ITickableTileEntity {
    public static final String name = GlassKiln.name;
    public static final TileEntityType<GlassKilnTileEntity> type = TileEntityType.Builder.create(GlassKilnTileEntity::new, MCCBlocks.glassKiln).build(null);
    static { type.setRegistryName(MCC.ID, name); }
    public static final IRecipeType<? extends AbstractCookingRecipe> recipeType = GlassKiln.recipeType;

    public static final int invWidth = 3;
    public static final int invHeight = 3;
    public static final int invItemsSize = invWidth * invHeight;
    public static final int invSize = invItemsSize + 2;

    public static final int slotOutput = 0;
    public static final int slotFuel = 1;
    public static final int[] slotItems = new int[invItemsSize];
    static {
        for (int i = 0; i < invItemsSize; ++i) {
            slotItems[i] = i + 2;
        }
    }

    private static final int[] SLOTS_UP = slotItems;
    private static final int[] SLOTS_DOWN = new int[]{slotOutput, slotFuel};
    private static final int[] SLOTS_HORIZONTAL = new int[]{slotFuel};

    protected GlassKilnTileEntity() {
        super(type);
    }

    protected NonNullList<ItemStack> items = NonNullList.withSize(invSize, ItemStack.EMPTY);
    private int burnTime;
    private int fuelTime;
    private int cookTime;
    private int cookTimeTotal;

    protected final IIntArray furnaceData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0: return GlassKilnTileEntity.this.burnTime;
                case 1: return GlassKilnTileEntity.this.fuelTime;
                case 2: return GlassKilnTileEntity.this.cookTime;
                case 3: return GlassKilnTileEntity.this.cookTimeTotal;
                default: return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0: GlassKilnTileEntity.this.burnTime = value; break;
                case 1: GlassKilnTileEntity.this.fuelTime = value; break;
                case 2: GlassKilnTileEntity.this.cookTime = value; break;
                case 3: GlassKilnTileEntity.this.cookTimeTotal = value;
            }
        }

        public int size() {
            return 4;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    @Nonnull @Override
    public ITextComponent getDefaultName() {
        return new TranslationTextComponent("container." + name);
    }

    public int getBurnTime(ItemStack fuel) {
        return ForgeHooks.getBurnTime(fuel) / 3;
    }

    public int getCookTime() {
        return this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    @Nonnull @Override
    protected Container createMenu(int id, @Nonnull PlayerInventory player) {
        return new GlassKilnContainer(id, player, this, this.furnaceData);
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
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
        if (direction == Direction.DOWN && index == slotFuel) {
            Item item = stack.getItem();
            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        }
        return true;
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Nonnull @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Nonnull @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Nonnull @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (Arrays.stream(slotItems).anyMatch(x -> x == index) && !flag) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public void fillStackedContents(@Nonnull RecipeItemHelper helper) {
        for(ItemStack itemstack : this.items) {
            helper.accountStack(itemstack);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Nullable @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void tick() {

    }
}
