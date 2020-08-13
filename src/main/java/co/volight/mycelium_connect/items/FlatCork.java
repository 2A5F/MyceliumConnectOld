package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class FlatCork extends Item {
    public static final String name = "flat_cork";

    public static FlatCork setup() {
        FlatCork o = new FlatCork(new Properties().group(MCC.MainGroup));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public FlatCork(Properties properties) {
        super(properties);
    }
}
