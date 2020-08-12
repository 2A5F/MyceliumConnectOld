package co.volight.mycelium_connect.recipes;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCRecipeType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MultiCountSmelting extends AbstractCookingRecipe {
    public static final String name = "multi_count_smelting";
    public static final IRecipeType<MultiCountSmelting> type = new IRecipeType<MultiCountSmelting>() {
        public String toString() {
            return MCC.ID + ":" + name;
        }
    };

    public static final Serializer SERIALIZER = new Serializer(200 );

    public MultiCountSmelting(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime) {
        super(MCCRecipeType.multiCountSmelting, id, group, ingredient, result, experience, cookTime);
    }

    @Nonnull @Override
    public ItemStack getIcon() {
        return new ItemStack(Blocks.FURNACE);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MultiCountSmelting> {

        private final int cookingTime;
        IFactory factory;

        public Serializer(int cookingTime) {
            this.cookingTime = cookingTime;
            this.setRegistryName(new ResourceLocation(MCC.ID, name));
            factory = MultiCountSmelting::new;
        }

        @Override
        public MultiCountSmelting read(ResourceLocation recipeId, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "ingredient"));
            Ingredient ingredient = Ingredient.deserialize(jsonelement);
            //todo
            return null;
        }

        @Nullable @Override
        public MultiCountSmelting read(ResourceLocation recipeId, PacketBuffer buffer) {
            return null;
        }

        @Override
        public void write(PacketBuffer buffer, MultiCountSmelting recipe) {

        }

        interface IFactory {
            MultiCountSmelting create(ResourceLocation id, String group, Ingredient ingredient, ItemStack result, float experience, int cookTime);
        }
    }
}
