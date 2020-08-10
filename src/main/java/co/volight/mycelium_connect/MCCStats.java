package co.volight.mycelium_connect;

import co.volight.mycelium_connect.blocks.produce.glasskiln.GlassKiln;
import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class MCCStats {

    public final static ResourceLocation interactWithGlassKiln = registerCustom("interact_with_" + GlassKiln.name, IStatFormatter.DEFAULT);

    public static ResourceLocation registerCustom(String key, IStatFormatter formatter) {
        ResourceLocation resourcelocation = new ResourceLocation(MCC.ID, key);
        Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, formatter);
        return resourcelocation;
    }
}
