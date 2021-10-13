package fr.lastril.uhchost.bungeecord;

import fr.lastril.uhchost.tools.API.BungeeAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessage implements PluginMessageListener {
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		String subchannel = "";
		try {
			subchannel = in.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (subchannel.equals("PlayerCount")) {
			try {
				String server = in.readUTF();
				int playercount = in.readInt();
				BungeeAPI.PlayerCount.put(server, Integer.valueOf(playercount));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		if (subchannel.equals("GetServers"))
			try {
				BungeeAPI.ServerList = in.readUTF().split(", ");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
