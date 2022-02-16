package fr.lastril.uhchost.tools.API.npc;

import fr.lastril.uhchost.tools.API.packets.PacketListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {

    private static final String LISTENER_KEY_NAME = "GROUP-UHC-PACKET";

    /* LISTENERS */
    private final List<PacketListener> listeners;

    public PacketManager() {
        this.listeners = new ArrayList<>();
    }

    public void onJoin(Player player) {
        CraftPlayer nmsPlayer = (CraftPlayer) player;
        Channel channel = nmsPlayer.getHandle().playerConnection.networkManager.channel;


        if (channel.pipeline().get(LISTENER_KEY_NAME) != null)
            return;

        channel.pipeline().addAfter("decoder", LISTENER_KEY_NAME, new MessageToMessageDecoder<Packet<?>>() {

            @Override
            protected void decode(ChannelHandlerContext channel, Packet<?> packet, List<Object> arg) throws Exception {
                arg.add(packet);
                PacketManager.this.listeners.forEach(packetListener -> packetListener.recievePacket(player, packet));
            }
        });
    }

    public void onQuit(Player player) {
        CraftPlayer nmsPlayer = (CraftPlayer) player;
        Channel channel = nmsPlayer.getHandle().playerConnection.networkManager.channel;
        if (channel.pipeline().get(LISTENER_KEY_NAME) != null) channel.pipeline().remove(LISTENER_KEY_NAME);
    }

    public void addListener(PacketListener listener) {
        this.listeners.add(listener);
    }

}
