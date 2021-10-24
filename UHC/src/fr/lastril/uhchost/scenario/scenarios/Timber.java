package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Timber extends Scenario {

	public Timber() {
		super("Timber", Arrays.asList(I18n.tl("scenarios.timber.lore", "")), Material.LOG);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		if (b.getType() == Material.LOG || b.getType() == Material.LOG_2) {
			final List<Block> bList = new ArrayList<>();
			checkLeaves(b);
			bList.add(b);
			(new BukkitRunnable() {
				public void run() {
					for (int i = 0; i < bList.size(); i++) {
						Block block = bList.get(i);
						if (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
							block.breakNaturally();
							Timber.this.checkLeaves(block);
						}
						for (BlockFace face : BlockFace.values()) {
							if (block.getRelative(face).getType() == Material.LOG
									|| block.getRelative(face).getType() == Material.LOG_2)
								bList.add(block.getRelative(face));
						}
						bList.remove(block);
					}
					if (bList.size() == 0)
						cancel();
				}
			}).runTaskTimer(UhcHost.getInstance(), 1L, 1L);
		}
	}

	private void checkLeaves(Block block) {
		Location loc = block.getLocation();
		World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		if (!validChunk(world, x - 5, y - 5, z - 5, x + 5, y + 5, z + 5))
			return;
		Bukkit.getServer().getScheduler().runTask(UhcHost.getInstance(), () -> {
			for (int offX = -4; offX <= 4; offX++) {
				for (int offY = -4; offY <= 4; offY++) {
					for (int offZ = -4; offZ <= 4; offZ++) {
						if (world.getBlockAt(x + offX, y + offY, z + offZ).getType() == Material.LEAVES
								|| world.getBlockAt(x + offX, y + offY, z + offZ).getType() == Material.LEAVES_2)
							breakLeaf(world, x + offX, y + offY, z + offZ);
					}
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void breakLeaf(World world, int x, int y, int z) {
		Block block = world.getBlockAt(x, y, z);
		byte data = block.getData();
		if ((data & 0x4) == 4)
			return;
		byte range = 4;
		byte max = 32;
		int[] blocks = new int[max * max * max];
		int off = range + 1;
		int mul = max * max;
		int div = max / 2;
		if (validChunk(world, x - off, y - off, z - off, x + off, y + off, z + off)) {
			int offX;
			for (offX = -range; offX <= range; offX++) {
				for (int offY = -range; offY <= range; offY++) {
					for (int offZ = -range; offZ <= range; offZ++) {
						Material mat = world.getBlockAt(x + offX, y + offY, z + offZ).getType();
						if (mat == Material.LEAVES || mat == Material.LEAVES_2) {
							return;
						} else if (mat == Material.LOG || mat == Material.LOG_2) {
							return;
						}
						blocks[(offX + div) * mul + (offY + div) * max + offZ
								+ div] = (mat == Material.LOG || mat == Material.LOG_2) ? 0
										: ((mat == Material.LEAVES || mat == Material.LEAVES_2) ? -2 : -1);
					}
				}
			}
			for (offX = 1; offX <= 4; offX++) {
				for (int offY = -range; offY <= range; offY++) {
					for (int offZ = -range; offZ <= range; offZ++) {
						for (int type = -range; type <= range; type++) {
							if (blocks[(offY + div) * mul + (offZ + div) * max + type + div] == offX - 1) {
								if (blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] == -2)
									blocks[(offY + div - 1) * mul + (offZ + div) * max + type + div] = offX;
								if (blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] == -2)
									blocks[(offY + div + 1) * mul + (offZ + div) * max + type + div] = offX;
								if (blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] == -2)
									blocks[(offY + div) * mul + (offZ + div - 1) * max + type + div] = offX;
								if (blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] == -2)
									blocks[(offY + div) * mul + (offZ + div + 1) * max + type + div] = offX;
								if (blocks[(offY + div) * mul + (offZ + div) * max + type + div - 1] == -2)
									blocks[(offY + div) * mul + (offZ + div) * max + type + div - 1] = offX;
								if (blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] == -2)
									blocks[(offY + div) * mul + (offZ + div) * max + type + div + 1] = offX;
							}
						}
					}
				}
			}
		}
		if (blocks[div * mul + div * max + div] < 0) {
			LeavesDecayEvent event = new LeavesDecayEvent(block);
			Bukkit.getServer().getPluginManager().callEvent(event);
			if (event.isCancelled())
				return;
			block.breakNaturally();
			if (10 > (new Random()).nextInt(100))
				world.playEffect(block.getLocation(), Effect.STEP_SOUND, Material.LEAVES.getId());
		}
	}

	public boolean validChunk(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		if (maxY >= 0 && minY < world.getMaxHeight()) {
			minX >>= 4;
			minZ >>= 4;
			maxX >>= 4;
			maxZ >>= 4;
			for (int x = minX; x <= maxX; x++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (!world.isChunkLoaded(x, z))
						return false;
				}
			}
			return true;
		}
		return false;
	}

}
