package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class Cork extends Item {
    public static final String name = "cork";

    public Cork() {
        this(new Properties().group(MCC.MainGroup));
    }

    public Cork(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
