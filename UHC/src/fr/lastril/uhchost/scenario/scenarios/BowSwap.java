package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.scenario.gui.BowSwapGui;
import fr.lastril.uhchost.tools.I18n;

public class BowSwap extends Scenario {

	private static int tpPercentage = 100;

	public BowSwap() {
		super("Bow Swap",
				Arrays.asList(I18n.tl("scenarios.bowswap.lore", new String[0]),
						I18n.tl("scenarios.bowswap.lore1", String.valueOf(tpPercentage))),
				Material.SNOW_BALL, BowSwapGui.class);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow
				&& ((Arrow) event.getDamager()).getShooter() instanceof Player
				&& (new Random()).nextInt(100) + 1 <= tpPercentage) {
			Location l1 = event.getEntity().getLocation();
			Location l2 = ((Player) ((Arrow) event.getDamager()).getShooter()).getLocation();
			event.getEntity().teleport(l2);
			((Player) ((Arrow) event.getDamager()).getShooter()).teleport(l1);
			((Player) event.getEntity()).playSound(event.getEntity().getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F,
					1.0F);
			((Player) ((Arrow) event.getDamager()).getShooter()).playSound(event.getDamager().getLocation(),
					Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
	}

	public static int getTpPercentage() {
		return tpPercentage;
	}

	public static void setTpPercentage(int tpPercentage) {
		BowSwap.tpPercentage = tpPercentage;
	}

}
