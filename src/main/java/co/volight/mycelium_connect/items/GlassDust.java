package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class GlassDust extends Item {
    public static final String name = "glass_dust";

    public GlassDust() {
        this(new Properties().group(MCC.MainGroup));
    }

    public GlassDust(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
