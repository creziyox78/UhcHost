package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import fr.lastril.uhchost.player.events.PlayerKillEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class NoCleanUp extends Scenario {

	public NoCleanUp() {
		super("NoCleanUp", Arrays.asList(I18n.tl("scenarios.nocleanup.lore", "")), Material.GOLD_SWORD);
	}

	@EventHandler
	public void onKill(PlayerKillEvent e) {
		Player player = e.getPlayer();
		if(player.getHealth() > 12)
			player.setHealth(player.getHealth() + (player.getMaxHealth() - player.getHealth()));
		else
			player.setHealth(player.getHealth() + 8);
	}

}
