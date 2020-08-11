package co.volight.mycelium_connect.blocks.jar;

import co.volight.mycelium_connect.MCC;
import co.volight.mycelium_connect.utils.Itemization;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;

public class GlassJar extends AbstractGlassBlock implements Itemization {
    public static final String name = "glass_jar";

    public GlassJar() {
        this(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS).notSolid());
    }

    public GlassJar(Properties properties) {
        super(properties);
        setRegistryName(MCC.ID, name);
    }
}
