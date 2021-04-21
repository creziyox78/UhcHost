package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.player.events.PvpEnableEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class Blitz extends Scenario {

	public Blitz() {
		super("Blitz", Arrays.asList(I18n.tl("scenarios.blitz.lore", new String[0]),
				I18n.tl("scenarios.blitz.lore1", new String[0])), Material.STONE_SWORD);
	}

	@EventHandler
	public void onGameStart(GameStartEvent event) {
		Bukkit.broadcastMessage(I18n.tl("scenarios.blitz.message", new String[0]));
		event.getPlayers().forEach(p -> p.setMaxHealth(5.0D));
	}

	@EventHandler
	public void onPvpEnable(PvpEnableEvent event) {
		Bukkit.broadcastMessage(I18n.tl("scenarios.blitz.message1", new String[0]));
		event.getPlayers().forEach(p -> {
			p.setMaxHealth(20.0D);
			p.setHealth(20.0D);
		});
	}

}
