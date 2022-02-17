package fr.lastril.uhchost.player.events.normal;

import fr.lastril.uhchost.UhcHost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class Portal implements Listener {

	private final UhcHost pl;

	public Portal(UhcHost pl) {
		this.pl = pl;
	}

	@EventHandler
	public void onPortalCreate(PortalCreateEvent event) {
		if (!this.pl.getGamemanager().isNether())
			event.setCancelled(true);
	}

	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		Player player = event.getPlayer();

		if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
			event.useTravelAgent(true);
			event.getPortalTravelAgent().setCanCreatePortal(true);
			Location location;
			if (player.getWorld() == getWorld()) {
				location = new Location(getNether(), event.getFrom().getBlockX() / 8, event.getFrom().getBlockY(), event.getFrom().getBlockZ() / 8);
			} else {
				location = new Location(getWorld(), event.getFrom().getBlockX() * 8, event.getFrom().getBlockY(), event.getFrom().getBlockZ() * 8);
			}
			event.setTo(event.getPortalTravelAgent().findOrCreate(location));
		}

		if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
			if (player.getWorld() == getWorld()) {
				Location loc = new Location(getEnd(), 100, 50, 0); // This is the vanilla location for obsidian platform.
				event.setTo(loc);
				Block block = loc.getBlock();
				for (int x = block.getX() - 2; x <= block.getX() + 2; x++) {
					for (int z = block.getZ() - 2; z <= block.getZ() + 2; z++) {
						Block platformBlock = loc.getWorld().getBlockAt(x, block.getY() - 1, z);
						if (platformBlock.getType() != Material.OBSIDIAN) {
							platformBlock.setType(Material.OBSIDIAN);
						}
						for (int yMod = 1; yMod <= 3; yMod++) {
							Block b = platformBlock.getRelative(BlockFace.UP, yMod);
							if (b.getType() != Material.AIR) {
								b.setType(Material.AIR);
							}
						}
					}
				}
			} else if (player.getWorld() == getEnd()) {
				event.setTo(getWorld().getSpawnLocation());
			}
		}
	}

	public World getWorld(){
		return Bukkit.getWorld("game");
	}

	public World getEnd(){
		return Bukkit.getWorld("game_the_end");
	}

	public World getNether(){
		return Bukkit.getWorld("game_nether");
	}

}
