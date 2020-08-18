package co.volight.mycelium_connect.tier;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import org.omg.CORBA.ObjectHolder;

import javax.annotation.Nonnull;

public class ObsidianTier implements IItemTier {
    public static final ObsidianTier tier = new ObsidianTier();

    @Override
    public int getMaxUses() {
        return 768;
    }

    @Override
    public float getEfficiency() {
        return 7.0f;
    }

    @Override
    public float getAttackDamage() {
        return 2.5f;
    }

    @Override
    public int getHarvestLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 12;
    }

    @Nonnull
    @Override
    public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(Items.OBSIDIAN, Items.CRYING_OBSIDIAN);
    }
}
