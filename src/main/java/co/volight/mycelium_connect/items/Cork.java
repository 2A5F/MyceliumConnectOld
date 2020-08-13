package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class Cork extends Item {
    public static final String name = "cork";

    public static Cork setup() {
        Cork o = new Cork(new Properties().group(MCC.MainGroup));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public Cork(Properties properties) {
        super(properties);
    }
}
