package fr.lastril.uhchost.tools.API.packets;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;

public interface PacketListener {

    void recievePacket(Player player, Packet<?> packet);

}
