package fr.lastril.uhchost.tools.API.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.lastril.uhchost.UhcHost;
import net.minecraft.server.v1_8_R3.*;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {

	private String name;
	private final Location loc;
	private final Property textures;

	private EntityPlayer player;
	private EntityLiving vehicle;
	private final NPCInteractEvent event;

	private final List<UUID> viewers;

	public NPC(String name, Location loc, Property skin, NPCInteractEvent event) {
		this.name = name;
		this.loc = loc;
		this.textures = skin;
		this.event = event;
		this.viewers = new ArrayList<>();
		this.create();
	}
	
	public String getName() {
		return this.name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public Location getLocation() {
		return this.loc;
	}

	
	public NPCInteractEvent getEvent() {
		return this.event;
	}

	
	public EntityPlayer getEntity() {
		return this.player;
	}

	
	public NPC create() {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);


		if(this.textures != null){
			gameProfile.getProperties().removeAll("textures");
			gameProfile.getProperties().removeAll("texture");
			gameProfile.getProperties().put("textures", textures);
			gameProfile.getProperties().put("texture", textures);
		}

		this.player = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
		this.player.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		this.player.getDataWatcher().watch(10, (byte) 0xFF);

		return this;
	}

	
	public void sendNPC() {
		Bukkit.getOnlinePlayers().forEach(this::sendNPC);
	}

	
	public void sendNPC(Player player) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this.player));
		connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this.player));
		connection.sendPacket(new PacketPlayOutEntityHeadRotation(this.player, (byte) (this.player.yaw * 256 / 360)));
		if(vehicle != null) this.sitNPC(player);
		this.addViewer(player.getUniqueId());

		new BukkitRunnable() {

			
			public void run() {
				connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, NPC.this.player));
			}
		}.runTaskLater(UhcHost.getInstance(), 20);
	}

	
	public void removeNPC() {
		Bukkit.getOnlinePlayers().forEach(this::removeNPC);
	}

	
	public void removeNPC(Player player) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this.player));
		connection.sendPacket(new PacketPlayOutEntityDestroy(this.player.getId()));
		this.removeViewer(player.getUniqueId());
	}

	
	public void killNPC() {
		Bukkit.getOnlinePlayers().forEach(this::killNPC);
	}

	
	public void killNPC(Player player) {
		PacketPlayOutEntityStatus packetStatus = new PacketPlayOutEntityStatus(this.player, (byte) 3);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetStatus);
	}

	
	public void sitNPC() {
		if(this.vehicle == null){
			this.vehicle = new EntitySquid(this.getEntity().world);
			vehicle.setLocation(loc.getX(), loc.getY() - 0.9, loc.getZ(), loc.getYaw(), loc.getPitch());
			vehicle.setInvisible(true);
		}
		Bukkit.getOnlinePlayers().forEach(this::sitNPC);
	}

	
	public void sitNPC(Player player) {
		if(this.vehicle == null){
			this.vehicle = new EntitySquid(this.getEntity().world);
			vehicle.setLocation(loc.getX(), loc.getY() - 0.9, loc.getZ(), loc.getYaw(), loc.getPitch());
			vehicle.setInvisible(true);
		}

		PacketPlayOutSpawnEntityLiving spawnVehicle = new PacketPlayOutSpawnEntityLiving(this.vehicle);
		PacketPlayOutEntityHeadRotation headVehicle = new PacketPlayOutEntityHeadRotation(this.vehicle, (byte) (this.player.yaw * 256 / 360));
		PacketPlayOutAttachEntity attachEntity = new PacketPlayOutAttachEntity(0, this.getEntity(), this.vehicle);
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		connection.sendPacket(spawnVehicle);
		connection.sendPacket(headVehicle);
		connection.sendPacket(attachEntity);
	}

	
	public void unsitNPC() {
		Bukkit.getOnlinePlayers().forEach(this::unsitNPC);
	}

	
	public void unsitNPC(Player player) {
		PacketPlayOutAttachEntity dettachEntity = new PacketPlayOutAttachEntity(0, this.getEntity(), null);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(dettachEntity);
		this.vehicle = null;
	}

	
	public void update() {
		Bukkit.getOnlinePlayers().forEach(this::update);
	}

	
	public void update(Player player) {
		if(player.getLocation().distance(this.loc) > 128){
			this.removeNPC(player);
		}else{
			if(!this.viewers.contains(player.getUniqueId())){
				this.sendNPC(player);
			}
		}
	}

	public void addViewer(UUID uuid){
		if(!this.viewers.contains(uuid)) this.viewers.add(uuid);
	}

	public void removeViewer(UUID uuid){
		if(this.viewers.contains(uuid)) this.viewers.remove(uuid);
	}

}