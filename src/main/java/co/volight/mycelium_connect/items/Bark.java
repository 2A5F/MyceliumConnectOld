package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class Bark extends Item {
    public static final String name = "bark";

    public Bark() {
        this(new Properties().group(MCC.MainGroup));
    }

    public Bark(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
