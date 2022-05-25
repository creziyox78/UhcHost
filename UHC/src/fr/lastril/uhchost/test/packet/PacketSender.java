package fr.lastril.uhchost.test.packet;

import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketSender {

    public static void send(Player player, Packet packet) {
        if (player == null || packet == null)
            return;
        try {
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
