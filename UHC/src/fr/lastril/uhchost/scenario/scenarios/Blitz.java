package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.events.PvpEnableEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.util.Arrays;

public class Blitz extends Scenario {

	public Blitz() {
		super("Blitz", Arrays.asList(I18n.tl("scenarios.blitz.lore"),
				I18n.tl("scenarios.blitz.lore1")), Material.STONE_SWORD);
	}

	@EventHandler
	public void onGameStart(GameStartEvent event) {
		Bukkit.broadcastMessage(I18n.tl("scenarios.blitz.message"));
		event.getPlayers().forEach(p -> p.setMaxHealth(5.0D));
	}

	@EventHandler
	public void onPvpEnable(PvpEnableEvent event) {
		Bukkit.broadcastMessage(I18n.tl("scenarios.blitz.message1"));
		event.getPlayers().forEach(p -> {
			p.setMaxHealth(20.0D);
			p.setHealth(20.0D);
		});
	}

}
