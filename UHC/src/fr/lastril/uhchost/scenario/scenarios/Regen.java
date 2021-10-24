package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.util.Arrays;

public class Regen extends Scenario {

	public Regen() {
		super("Regen", Arrays.asList(I18n.tl("scenarios.regen")), Material.RED_ROSE);
	}

	@EventHandler
	public void onGameStart(GameStartEvent e) {
		(UhcHost.getInstance()).worldUtils.getWorld().setGameRuleValue("naturalRegeneration", "true");
		(UhcHost.getInstance()).worldUtils.getNether().setGameRuleValue("naturalRegeneration", "true");
		(UhcHost.getInstance()).worldUtils.getEnd().setGameRuleValue("naturalRegeneration", "true");
	}

}
