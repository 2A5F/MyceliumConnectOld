package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class Bark extends Item {
    public static final String name = "bark";

    public static Bark setup() {
        Bark o = new Bark(new Properties().group(MCC.MainGroup));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public Bark(Properties properties) {
        super(properties);
    }
}
