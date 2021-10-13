package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class BloodDiamond extends Scenario {
	
	public BloodDiamond() {
		super("BloodDiamond", Arrays.asList(I18n.tl("scenarios.blooddiamond.lore","")), Material.DIAMOND_ORE);
	}
	
	@EventHandler
	public void OnMineDiamond(BlockBreakEvent e) {
		Block block = e.getBlock();
		if (block.getType() == Material.DIAMOND_ORE) {
			if (e.getPlayer().getHealth() > 0.5
					&& e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
				e.getPlayer().damage(0.5D);
			} else {
				e.getPlayer().setHealth(0.5D);
			}

		}

	}

}
