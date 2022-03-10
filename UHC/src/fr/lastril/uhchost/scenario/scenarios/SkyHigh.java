package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SkyHigh extends Scenario {

	private final List<UUID> damaged;

	public SkyHigh() {
		super("Sky High", Arrays.asList(I18n.tl("scenarios.skyhigh.lore"), I18n.tl("scenarios.skyhigh.lore1")),
				Material.LADDER);
		this.damaged = new ArrayList<>();
	}

	@EventHandler
	public void onGameStart(GameStartEvent event) {
		List<ItemStack> kit = Arrays.asList((new ItemsCreator(Material.SLIME_BLOCK, null, null, 64)).create(),
						(new ItemsCreator(Material.DIRT, null, null, 3)).create());
		for (Player player : event.getPlayers()) {
			kit.forEach(i -> player.getInventory().addItem(new ItemStack[] { i }));
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getTo().getY() < 150.0D && (UhcHost.getInstance()).getGamemanager().isPvp()
				&& !this.damaged.contains(event.getPlayer().getUniqueId())) {
			event.getPlayer().damage(2.0D);
			this.damaged.add(event.getPlayer().getUniqueId());
			event.getPlayer().sendMessage(I18n.tl("scenarios.skyhigh.message"));
			Bukkit.getScheduler().runTaskLater(UhcHost.getInstance(), new Runnable() {
				public void run() {
					SkyHigh.this.damaged.remove(event.getPlayer().getUniqueId());
				}
			}, 600L);
		}
	}

	@EventHandler
	public void onBlockPlace(final BlockPlaceEvent event) {
		if (event.getBlock().getType() == Material.DIRT)
			Bukkit.getScheduler().runTaskLater(UhcHost.getInstance(), new Runnable() {
				public void run() {
					event.getPlayer().getInventory().addItem(new ItemStack(Material.DIRT));
				}
			}, 1L);
	}

}
