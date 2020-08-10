package co.volight.mycelium_connect.recipes;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.crafting.IRecipeType;

public class RecipeTypeMultiCountSmelting implements IRecipeType<MultiCountSmelting> {

    @Override
    public String toString() {
        return MCC.ID + ":" + MultiCountSmelting.name;
    }
}
