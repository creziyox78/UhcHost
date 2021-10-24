package fr.lastril.uhchost.world;

import fr.lastril.uhchost.UhcHost;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.io.File;
import java.lang.reflect.Field;
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
}
