package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class GlassShards extends Item {
    public static final String name = "glass_shards";

    public static GlassShards setup() {
        GlassShards o = new GlassShards(new Properties().group(MCC.MainGroup));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public GlassShards(Properties properties) {
        super(properties);
    }
}
