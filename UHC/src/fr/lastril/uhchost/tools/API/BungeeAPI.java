package fr.lastril.uhchost.tools.API;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class BungeeAPI {
	public static HashMap<String, Integer> PlayerCount = new HashMap<>();

	public static String[] ServerList;

	public static void ConnectBungeeServer(Player p, String ServerName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(ServerName);
		} catch (IOException e) {
			System.out.println("ERROR:" + e.getMessage());
		}
		p.sendPluginMessage(UhcHost.instance, "BungeeCord", b.toByteArray());
	}

	public static void GetPlayerCount(Player p, String ServerName) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("PlayerCount");
			out.writeUTF(ServerName);
		} catch (IOException e) {
			System.out.println("ERROR:" + e.getMessage());
		}
		p.sendPluginMessage(UhcHost.instance, "BungeeCord", b.toByteArray());
	}

	public static void GetServers(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("GetServers");
		} catch (IOException e) {
			System.out.println("ERROR:" + e.getMessage());
		}
		p.sendPluginMessage(UhcHost.instance, "BungeeCord", b.toByteArray());
	}
}
