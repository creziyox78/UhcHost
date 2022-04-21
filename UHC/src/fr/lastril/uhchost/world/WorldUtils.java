package fr.lastril.uhchost.world;

import fr.lastril.uhchost.UhcHost;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldUtils {
	private final UhcHost pl;

	private boolean lobby;

	private final List<Block> lobbyBlocks;

	private World world;

	private final World nether;

	private final World end;

	private Location center;

	public boolean isLobby() {
		return this.lobby;
	}

	public void setLobby(boolean lobby) {
		this.lobby = lobby;
	}

	public World getWorld() {
		return this.world;
	}

	public World getNether() {
		return this.nether;
	}

	public World getEnd() {
		return this.end;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Location getCenter() {
		return this.center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public WorldUtils(UhcHost pl, World nether, World end) {
		this.pl = pl;
		this.lobbyBlocks = new ArrayList<>();
		this.nether = nether;
		this.end = end;
		//this.center = new Location(world, 0.0D, 0, 0.0D);
	}

	private static boolean deleteWorld(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteWorld(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}

	public static void spawnParticle(Location loc, EnumParticle particle) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			spawnParticle(online, loc, particle);
		}
	}

	public static void spawnParticle(Location loc, EnumParticle particle, float xOffset, float yOffset, float zOffset, float speed, int count) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			spawnParticle(online, loc, particle, xOffset, yOffset, zOffset, speed, count);
		}
	}

	public static void spawnColoredParticle(Location loc, EnumParticle particle, float red, float green, float blue) {
		for (Player online : Bukkit.getOnlinePlayers()) {
			spawnColoredParticle(online, loc, particle, red, green, blue);
		}
	}

	public static void spawnColoredParticle(Location loc, EnumParticle particle, Color color) {
		spawnColoredParticle(loc, particle, color.getBlue(), color.getGreen(), color.getRed());
	}

	public static void spawnParticle(Player player, Location loc, EnumParticle particle) {
		spawnParticle(player, loc, particle, 0, 0, 0, 0, 1);
	}



	public static void spawnParticle(Player player, Location loc, EnumParticle particle, float xOffset, float yOffset, float zOffset, float speed, int count) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(),
				(float) loc.getY(), (float) loc.getZ(), xOffset, yOffset, zOffset, speed, count);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void spawnColoredParticle(Player player, Location loc, EnumParticle particle, float red, float green, float blue){
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), red, green, blue, 1.0F, 0);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void spawnParticle(Location loc, Effect particle) {
		loc.getWorld().playEffect(loc, particle, 1);
	}

	public static void spawnFakeLightning(Player player, Location loc){
		spawnFakeLightning(player, loc, false);
	}

	public static void spawnFakeLightning(Player player, Location loc, boolean isEffect){
		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

		EntityLightning lightning = new EntityLightning(nmsPlayer.getWorld(), loc.getX(), loc.getY(), loc.getZ(), isEffect, false);

		nmsPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityWeather(lightning));
		player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1f, 1f);
	}

	public static double getDistanceBetweenTwoLocations(Location one, Location two){
		double minX = Math.min(one.getX(), two.getX());
		double maxX = Math.max(one.getX(), two.getX());


		double minZ = Math.min(one.getZ(), two.getZ());
		double maxZ = Math.max(one.getZ(), two.getZ());

		double xDiff = maxX-minX;
		double zDiff = maxZ-minZ;

		return Math.sqrt(xDiff*xDiff + zDiff*zDiff);
	}

	public static String getBeautyHealth(Player player){
		return UhcHost.getDecimalformater().format(player.getHealth()+((CraftPlayer) player).getHandle().getAbsorptionHearts());
	}

	public static void createBeautyExplosion(Location loc, int power){
		createBeautyExplosion(loc, power, false);
	}

	public static void createBeautyExplosion(Location loc, int power, boolean fire){
		List<Location> blocks = generateSphere(loc, power, false);

		for (Location blockLoc : blocks) {
			Block block = blockLoc.getBlock();
			if(block.getType() != Material.AIR && block.getType() != Material.BEDROCK){
				blockLoc.getBlock().setType(Material.AIR);
			}
		}
	}

	public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
		if (centerBlock == null) {
			return new ArrayList<>();
		}

		List<Location> circleBlocks = new ArrayList<>();

		int bx = centerBlock.getBlockX();
		int by = centerBlock.getBlockY();
		int bz = centerBlock.getBlockZ();

		for(int x = bx - radius; x <= bx + radius; x++) {
			for(int y = by - radius; y <= by + radius; y++) {
				for(int z = bz - radius; z <= bz + radius; z++) {

					double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

					if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

						Location l = new Location(centerBlock.getWorld(), x, y, z);

						circleBlocks.add(l);

					}

				}
			}
		}

		return circleBlocks;
	}

}
