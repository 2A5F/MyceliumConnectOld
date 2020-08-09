package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class GlassNugget extends Item {
    public static final String name = "glass_nugget";

    public GlassNugget() {
        this(new Properties().group(MCC.MainGroup));
    }

    public GlassNugget(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
