package fr.lastril.uhchost.tools.API.npc;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.packets.PacketListener;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NPCManager implements PacketListener {

	private final Map<String, NPC> npcRegistereds;

	public NPCManager(PacketManager packetManager) {
		this.npcRegistereds = new HashMap<>();
		packetManager.addListener(this);
		new BukkitRunnable(){

			@Override
			public void run() {
				for (NPC npc : NPCManager.this.npcRegistereds.values()) {
					npc.update();
				}
			}
		}.runTaskTimer(UhcHost.getInstance(), 0, 20);
	}


	public void recievePacket(Player player, Packet<?> packet) {
		if (packet instanceof PacketPlayInUseEntity) {
			PacketPlayInUseEntity packetUse = (PacketPlayInUseEntity) packet;
			PacketPlayInUseEntity.EnumEntityUseAction action = packetUse.a();
			if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT || action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
				int entityID = (int) getValue(packetUse, "a");
				Optional<NPC> optionalNPC = getNPCWithEntityID(entityID);

				if (optionalNPC.isPresent()) {
					NPC npc = optionalNPC.get();
					if (npc.getEvent() != null){
						npc.getEvent().onClick(player, npc);
						if(action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) npc.getEvent().onRightClick(player, npc);
						if(action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) npc.getEvent().onLeftClick(player, npc);
					}
				}
			}
		}
	}

	public NPC addNPC(String key, String name, Location loc, Property skin, NPCInteractEvent event) {
		NPC npc = new NPC(name, loc, skin, event);
		this.npcRegistereds.put(key, npc);
		return npc;
	}

	public void deleteNPC(String key) {
		this.npcRegistereds.remove(key).removeNPC();
	}

	public void deleteNPCs() {
		this.npcRegistereds.values().forEach(NPC::removeNPC);
	}

	public void onJoin(Player player) {
		for (NPC npc : this.npcRegistereds.values()) {
			npc.sendNPC(player);
		}
	}

	public void onQuit(Player player) {
		for (NPC npc : this.npcRegistereds.values()) {
			npc.removeNPC(player);
		}
	}

	public Property getPlayerTextures(Player player) {
		return new ArrayList<>(((CraftPlayer) player).getHandle().getProfile().getProperties().get("textures")).get(0);
	}

	public Property getPlayerTextures(String name) {
		try {
			URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());

			String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();
			URL url_1 = new URL(
					"https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());

			String propertyJson = new JsonParser().parse(reader_1).getAsJsonObject().get("properties")
					.getAsJsonArray().get(0).toString();
			return new Gson().fromJson(propertyJson, Property.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Object getValue(Object instance, String name) {
		Object result = null;

		try {

			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);

			result = field.get(instance);

			field.setAccessible(false);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	public Optional<NPC> getNPCWithEntityID(int id) {
		return this.npcRegistereds.values().stream().filter(npc -> npc.getEntity().getId() == id).findAny();
	}

}
