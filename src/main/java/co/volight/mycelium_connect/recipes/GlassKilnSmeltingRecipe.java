package co.volight.mycelium_connect.recipes;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCBlocks;
import co.volight.mycelium_connect.inventorys.CraftInv;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.mojang.realmsclient.util.JsonUtils;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class GlassKilnSmeltingRecipe implements IRecipe<CraftInv>, IShapedRecipe<CraftInv> {
    public static final String name = "glass_kiln_smelting";
    public static final IRecipeType<GlassKilnSmeltingRecipe> type = new IRecipeType<GlassKilnSmeltingRecipe>() {
        public String toString() {
            return MCC.ID + ":" + name;
        }
    };

    public static final Serializer SERIALIZER = new Serializer(200 );

    private final int width;
    private final int height;
    private final NonNullList<Ingredient> inputs;
    private final ItemStack output;
    private final ResourceLocation id;
    private final String group;
    private final int cookTime;

    public GlassKilnSmeltingRecipe(ResourceLocation id, String group, int width, int height, NonNullList<Ingredient> inputs, ItemStack output, int cookTime) {
        this.width = width;
        this.height = height;
        this.inputs = inputs;
        this.output = output;
        this.id = id;
        this.group = group;
        this.cookTime = cookTime;
    }

    @Nonnull @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Nonnull @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Nonnull @Override
    public String getGroup() {
        return this.group;
    }

    @Nonnull @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Nonnull @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public boolean matches(CraftInv inv, @Nonnull World worldIn) {
        for(int i = 0; i <= inv.getWidth() - this.width; ++i) {
            for(int j = 0; j <= inv.getHeight() - this.height; ++j) {
                if (this.checkMatch(inv, i, j, true)) return true;
                if (this.checkMatch(inv, i, j, false)) return true;
            }
        }
        return false;
    }

    private boolean checkMatch(CraftInv inv, int offsetX, int offsetY, boolean bl) {
        for(int i = 0; i < inv.getWidth(); ++i) {
            for(int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (bl) {
                        ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.inputs.get(k + l * this.width);
                    }
                }

                boolean test = testIngredient(ingredient, inv.getStackInSlot(i + j * inv.getWidth()));
                if (!test) {
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean testIngredient(Ingredient ing, ItemStack item) {
        if (ing instanceof NBTIngredient) {
            NBTIngredient nbting = (NBTIngredient)ing;
            Field field = ObfuscationReflectionHelper.findField(NBTIngredient.class, "stack");
            if(!nbting.test(item)) return false;
            try {
                ItemStack stack = (ItemStack)field.get(nbting);
                if (stack.getCount() <= item.getCount()) return true;
            } catch (IllegalAccessException e) {
                MCC.Logger.error(e);
            }
            return false;
        } else return ing.test(item);
    }

    @Nonnull @Override
    public ItemStack getCraftingResult(@Nonnull CraftInv inv) {
        root: for(int i = 0; i <= inv.getWidth() - this.width; ++i) {
            for(int j = 0; j <= inv.getHeight() - this.height; ++j) {
                if (this.doShrinkCraftingResult(inv, i, j, true)) break root;
                if (this.doShrinkCraftingResult(inv, i, j, false)) break root;
            }
        }
        return this.getRecipeOutput().copy();
    }

    private boolean doShrinkCraftingResult(CraftInv inv, int offsetX, int offsetY, boolean bl) {
        for(int i = 0; i < inv.getWidth(); ++i) {
            for(int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (bl) {
                        ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = this.inputs.get(k + l * this.width);
                    }
                }

                boolean test = doShrinkCraftingResult(ingredient, inv.getStackInSlot(i + j * inv.getWidth()));
                if (!test) {
                    return false;
                }
            }
        }

        return true;
    }
    private boolean doShrinkCraftingResult(Ingredient ing, ItemStack item) {
        if (ing instanceof NBTIngredient) {
            NBTIngredient nbting = (NBTIngredient)ing;
            Field field = ObfuscationReflectionHelper.findField(NBTIngredient.class, "stack");
            if(!nbting.test(item)) return false;
            try {
                ItemStack stack = (ItemStack)field.get(nbting);
                if (stack.getCount() <= item.getCount()) {
                    item.shrink(stack.getCount());
                    return true;
                }
            } catch (IllegalAccessException e) {
                MCC.Logger.error(e);
            }
            return false;
        } else return ing.test(item);
    }

    @Nonnull @Override
    public ItemStack getIcon() {
        return new ItemStack(MCCBlocks.glassKiln);
    }

    @Nonnull @Override
    public IRecipeType<?> getType() {
        return type;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }

    public int getCookTime() {
        return cookTime;
    }

    //====================================================================================================

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GlassKilnSmeltingRecipe> {

        private final int defaultCookingTime;

        public Serializer(int cookingTime) {
            this.defaultCookingTime = cookingTime;
            this.setRegistryName(new ResourceLocation(MCC.ID, name));
        }

        @Nonnull @Override
        public GlassKilnSmeltingRecipe read(@Nonnull ResourceLocation id, @Nonnull JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Map<String, Ingredient> map = deserializeKey(JSONUtils.getJsonObject(json, "key"));
            String[] strings = combinePattern(getPattern(JSONUtils.getJsonArray(json, "pattern")));
            int w = strings[0].length();
            int h = strings.length;
            NonNullList<Ingredient> inputs = deserializeIngredients(strings, map, w, h);
            ItemStack output = deserializeItem(JSONUtils.getJsonObject(json, "result"));
            int cookingTime = JSONUtils.getInt(json, "cookingtime", this.defaultCookingTime);
            return new GlassKilnSmeltingRecipe(id, group, w, h, inputs, output, cookingTime);
        }

        @Nullable @Override
        public GlassKilnSmeltingRecipe read(@Nonnull ResourceLocation id, PacketBuffer buffer) {
            int w = buffer.readVarInt();
            int h = buffer.readVarInt();
            String group = buffer.readString(32767);
            NonNullList<Ingredient> inputs = NonNullList.withSize(w * h, Ingredient.EMPTY);
            for(int k = 0; k < inputs.size(); ++k) {
                inputs.set(k, Ingredient.read(buffer));
            }
            ItemStack output = buffer.readItemStack();
            int cookingTime = buffer.readVarInt();
            return new GlassKilnSmeltingRecipe(id, group, w, h, inputs, output, cookingTime);
        }

        @Override
        public void write(PacketBuffer buffer, GlassKilnSmeltingRecipe recipe) {
            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);
            buffer.writeString(recipe.group);
            for(Ingredient ingredient : recipe.inputs) {
                ingredient.write(buffer);
            }
            buffer.writeItemStack(recipe.output);
            buffer.writeVarInt(recipe.cookTime);
        }

        private ItemStack deserializeItem(JsonObject json) {
            String s = JSONUtils.getString(json, "item");
            // func_241873_b := getValue(ResourceLocation name)
            Item item = Registry.ITEM.func_241873_b(new ResourceLocation(s)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + s + "'"));
            if (json.has("data")) {
                throw new JsonParseException("Disallowed data tag found");
            } else {
                int i = JSONUtils.getInt(json, "count", 1);
                return new ItemStack(item, i);
            }
        }

        private static NonNullList<Ingredient> deserializeIngredients(String[] pattern, Map<String, Ingredient> keys, int width, int height) {
            NonNullList<Ingredient> list = NonNullList.withSize(width * height, Ingredient.EMPTY);
            Set<String> set = Sets.newHashSet(keys.keySet());
            set.remove(" ");

            for(int y = 0; y < pattern.length; ++y) {
                for(int w = 0; w < pattern[y].length(); ++w) {
                    String s = pattern[y].substring(w, w + 1);
                    Ingredient ingredient = keys.get(s);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    }
                    set.remove(s);
                    list.set(w + width * y, ingredient);
                }
            }

            if (!set.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            } else {
                return list;
            }
        }

        private static Map<String, Ingredient> deserializeKey(JsonObject json) {
            Map<String, Ingredient> map = Maps.newHashMap();
            for(Map.Entry<String, JsonElement> entry : json.entrySet()) {
                if (entry.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + (String)entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                }
                if (" ".equals(entry.getKey())) {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                }
                map.put(entry.getKey(), Ingredient.deserialize(entry.getValue()));
            }
            map.put(" ", Ingredient.EMPTY);
            return map;
        }

        private static String[] getPattern(JsonArray json) {
            String[] strings = new String[json.size()];
            if (strings.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            } else {
                for(int i = 0; i < strings.length; ++i) {
                    String s = JSONUtils.getString(json.get(i), "pattern[" + i + "]");
                    if (i > 0 && strings[0].length() != s.length()) {
                        throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                    }
                    strings[i] = s;
                }
            }
            return strings;
        }

        private static String[] combinePattern(String... lines) {
            int i = Integer.MAX_VALUE;
            int j = 0, k = 0, l = 0;

            for(int m = 0; m < lines.length; ++m) {
                String s = lines[m];
                i = Math.min(i, firstNonSpace(s));
                int n = lastNonSpace(s);
                j = Math.max(j, n);
                if (n < 0) {
                    if (k == m) {
                        ++k;
                    }
                    ++l;
                } else {
                    l = 0;
                }
            }

            if (lines.length == l) {
                return new String[0];
            } else {
                String[] strings = new String[lines.length - l - k];

                for(int o = 0; o < strings.length; ++o) {
                    strings[o] = lines[o + k].substring(i, j + 1);
                }

                return strings;
            }
        }

        private static int lastNonSpace(String s) {
            int i;
            for(i = s.length() - 1; i >= 0 && s.charAt(i) == ' '; --i) { }
            return i;
        }

        private static int firstNonSpace(String s) {
            int i;
            for(i = 0; i < s.length() && s.charAt(i) == ' '; ++i) { }
            return i;
        }

        public int getDefaultCookingTime() {
            return defaultCookingTime;
        }
    }
}
