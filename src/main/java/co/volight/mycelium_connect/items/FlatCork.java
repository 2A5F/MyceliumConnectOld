package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class FlatCork extends Item {
    public static final String name = "flat_cork";

    public FlatCork() {
        this(new Properties().group(MCC.MainGroup));
    }

    public FlatCork(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
