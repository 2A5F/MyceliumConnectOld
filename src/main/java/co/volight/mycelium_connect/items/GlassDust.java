package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class GlassDust extends Item {
    public static final String name = "glass_dust";

    public static GlassDust setup() {
        GlassDust o = new GlassDust(new Properties().group(MCC.MainGroup));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public GlassDust(Properties properties) {
        super(properties);
    }
}
