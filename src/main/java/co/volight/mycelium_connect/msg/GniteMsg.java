package co.volight.mycelium_connect.msg;

import co.volight.mycelium_connect.api.ICanGnite;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;
import java.util.function.Supplier;

public class GniteMsg {

    private final UUID playerId;
    private final boolean gnite;

    public GniteMsg(UUID playerId, boolean gnite) {
        this.playerId = playerId;
        this.gnite = gnite;
    }

    public static void encode(GniteMsg msg, PacketBuffer buf) {
        buf.writeUniqueId(msg.playerId);
        buf.writeBoolean(msg.gnite);
    }

    public static GniteMsg decode(PacketBuffer buf) {
        return new GniteMsg(buf.readUniqueId(), buf.readBoolean());
    }

    public static void handle(GniteMsg msg, Supplier<NetworkEvent.Context> ctx) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ServerPlayerEntity player = server.getPlayerList().getPlayerByUUID(msg.playerId);
        if (player == null) return;
        if (player.openContainer instanceof ICanGnite) {
            ICanGnite g = (ICanGnite)player.openContainer;
            ctx.get().enqueueWork(() -> {
                g.setGnite(msg.gnite);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
