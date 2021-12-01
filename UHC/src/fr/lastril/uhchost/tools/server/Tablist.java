package fr.lastril.uhchost.tools.server;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class Tablist extends BukkitRunnable {
	public void run() {
		for (Player player : Bukkit.getOnlinePlayers())
			TabList(player);
	}

	public static void TabList(Player player) {
		double tps = Math.round(TpsServer.getTPS() * 100.0D) / 100.0D;
		int ping = (((CraftPlayer) player).getHandle()).ping;
		PlayerConnection con = (((CraftPlayer) player).getHandle()).playerConnection;
		IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\":\" " + ChatColor.AQUA
				+ "Bienvenue" + ChatColor.WHITE + " " + player.getName() + ChatColor.AQUA + " !\"}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHeader);
		IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer
				.a("{\"text\":\"" + ChatColor.AQUA + "Ton ping: " + ChatColor.WHITE + ping + "ms" + ChatColor.AQUA
						+ "  |  TPS du serveur: " + ChatColor.WHITE + tps + "\n" +
						"§cPlugin by Lastril"
						+" \"}");
		try {
			Field a = packet.getClass().getDeclaredField("b");
			a.setAccessible(true);
			a.set(packet, tabFooter);
		} catch (Exception e) {
			System.out.println("Tablist not charged !");
		} finally {
			con.sendPacket(packet);
		}
	}
}
