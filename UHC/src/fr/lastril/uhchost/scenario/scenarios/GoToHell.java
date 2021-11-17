package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.GameState;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GoToHell extends Scenario {

	private final List<UUID> netherDamageds;

	public GoToHell() {
		super("Go To Hell", Arrays.asList(I18n.tl("scenarios.gotohell.lore"), I18n.tl("scenarios.gotohell.lore1"), I18n.tl("scenarios.gotohell.lore2")), Material.NETHERRACK);
		this.netherDamageds = new ArrayList<>();
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		GameManager gameManager = UhcHost.getInstance().getGamemanager();
		player.getWorld().getEnvironment();
		if (GameState.isState(GameState.STARTED) && event.getTo().getWorld().getEnvironment() != Environment.NETHER && gameManager.isPvp()) {
			if (this.netherDamageds.contains(player.getUniqueId()))
				return;
			player.damage(2.0D);
			player.sendMessage(I18n.tl("scenarios.gotohell.message"));
			this.netherDamageds.add(player.getUniqueId());
			Bukkit.getScheduler().runTaskLater(UhcHost.getInstance(), new Runnable() {
				public void run() {
					GoToHell.this.netherDamageds.remove(player.getUniqueId());
				}
			}, 200L);
		}
	}

}
