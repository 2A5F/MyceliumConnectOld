package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class GlassShards extends Item {
    public static final String name = "glass_shards";

    public GlassShards() {
        this(new Properties().group(MCC.MainGroup));
    }

    public GlassShards(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
