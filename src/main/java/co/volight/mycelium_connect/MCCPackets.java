package co.volight.mycelium_connect;

import co.volight.mycelium_connect.msg.GniteMsg;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MCCPackets {
    private static final String protocol_version = "1";
    public static final SimpleChannel instace = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MCC.ID, "main"),
            () -> protocol_version, protocol_version::equals, protocol_version::equals
    );

    public static void Setup() {
        int id = 0;
        instace.registerMessage(id++, GniteMsg.class, GniteMsg::encode, GniteMsg::decode, GniteMsg::handle);
    }
}
