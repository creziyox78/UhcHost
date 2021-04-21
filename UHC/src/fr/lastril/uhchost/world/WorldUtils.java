package fr.lastril.uhchost.world;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.world.tasks.ChunksLoaderTask;
import net.minecraft.server.v1_8_R3.BiomeBase;

public class WorldUtils {
	private UhcHost pl;

	private boolean lobby;

	private List<Block> lobbyBlocks;

	private World world;

	private World nether;

	private World end;

	private Location center;

	public boolean isLobby() {
		return this.lobby;
	}

	public void setLobby(boolean lobby) {
		this.lobby = lobby;
	}

	public List<Block> getLobbyBlocks() {
		return this.lobbyBlocks;
	}

	public void setLobbyBlocks(List<Block> lobbyBlocks) {
		this.lobbyBlocks = lobbyBlocks;
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

	public void setNether(World nether) {
		this.nether = nether;
	}

	public void setEnd(World end) {
		this.end = end;
	}

	public Location getCenter() {
		return this.center;
	}

	public void setCenter(Location center) {
		this.center = center;
	}

	public WorldUtils(UhcHost pl, World world, World nether, World end) {
		this.pl = pl;
		this.lobbyBlocks = new ArrayList<>();
		if (this.pl.getConfig().getBoolean("chunk_load_status")) {
			(new ChunksLoaderTask(this.pl, this.pl.getConfig().getInt("chunks_to_load_radius")))
					.runTaskTimer((Plugin) this.pl, 0L, 1L);
		}
		else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Preloading disabled ! Change 'chunk_load_status_ in 'true' on config.yml !");
			this.pl.taskManager.lobbyTask();
		}
		this.world = world;
		this.nether = nether;
		this.end = end;
		this.center = new Location(world, 0.0D, 200.0D, 0.0D);
	}

	public static void deleteWorlds(String... worlds) {
		byte b;
		int i;
		String[] arrayOfString;
		for (i = (arrayOfString = worlds).length, b = 0; b < i;) {
			String world = arrayOfString[b];
			deleteWorld(new File(world));
			b++;
		}
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

	public void resetLobby() {
		for (int x = -1; x < 1; x++) {
			for (int z = -1; z < 1; z++)
				this.pl.worldUtils.getWorld().loadChunk(x, z);
		}
		for (Block lobbyBlock : this.lobbyBlocks)
			lobbyBlock.setType(Material.AIR);
	}

	public static void replaceBiomes() {
		Field biomesField = null;
		try {
			biomesField = BiomeBase.class.getDeclaredField("biomes");
			biomesField.setAccessible(true);
			if (biomesField.get(null) instanceof BiomeBase[]) {
				BiomeBase[] biomes = (BiomeBase[]) biomesField.get(null);
				biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
				biomes[BiomeBase.OCEAN.id] = BiomeBase.FOREST;
				biomes[BiomeBase.FROZEN_OCEAN.id] = BiomeBase.FROZEN_RIVER;
				biomes[BiomeBase.OCEAN.id] = BiomeBase.FOREST;
				biomes[BiomeBase.DEEP_OCEAN.id] = BiomeBase.PLAINS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
