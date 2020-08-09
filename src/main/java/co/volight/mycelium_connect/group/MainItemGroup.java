package co.volight.mycelium_connect.group;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.MCCBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;

public class MainItemGroup extends ItemGroup {

    public MainItemGroup() {
        super(MCC.ID);
    }

    @Nonnull
    @Override
    public ItemStack createIcon() {
        return new ItemStack(MCCBlocks.fungi);
    }
}
