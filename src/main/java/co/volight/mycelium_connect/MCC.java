package co.volight.mycelium_connect;

import co.volight.mycelium_connect.group.MainItemGroup;
import co.volight.mycelium_connect.setup.ClientSetup;
import co.volight.mycelium_connect.setup.CommonSetup;
import co.volight.mycelium_connect.setup.ServerSetup;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MCC.ID)
public final class MCC {
    public static final String ID = "mycelium_connect";
    public static final Logger Logger = LogManager.getLogger();
    public static final ItemGroup MainGroup = new MainItemGroup();

    public MCC() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);
        MinecraftForge.EVENT_BUS.register(new ServerSetup());
        new CommonSetup();
    }
}
