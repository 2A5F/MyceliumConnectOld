package co.volight.mycelium_connect.items;

import co.volight.mycelium_connect.MCC;
import net.minecraft.item.Item;

public class GlassNugget extends Item {
    public static final String name = "glass_nugget";

    public static GlassNugget setup() {
        GlassNugget o = new GlassNugget(new Properties().group(MCC.MainGroup));
        o.setRegistryName(MCC.ID, name);
        return o;
    }

    public GlassNugget(Properties properties) {
        super(properties);
    }
}
