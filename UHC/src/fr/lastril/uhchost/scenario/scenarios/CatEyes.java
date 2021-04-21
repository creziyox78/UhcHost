package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class CatEyes extends Scenario {
	
	public CatEyes() {
		super("Cat Eyes", Arrays.asList(I18n.tl("scenarios.cateyes.lore", ""), ""), Material.EYE_OF_ENDER);
	}
	
	@EventHandler
	public void onStart(GameStartEvent e) {
		for(Player player: e.getPlayers()){
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true));
		}
	}

}
