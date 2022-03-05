package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.FurnaceBurnEvent;

import java.util.Arrays;

public class FastMelting extends Scenario {
	
	public FastMelting() {
		super("Fast Melting", Arrays.asList(I18n.tl("scenarios.fastmelting.lore", "")), Material.FURNACE);
	}
	
	@EventHandler
	public void onFurnace(FurnaceBurnEvent e) {
		final Block block = e.getBlock();
		Bukkit.getScheduler().runTaskLater(UhcHost.instance, new Runnable() {
			@Override
			public void run() {
				if(block.getType() == Material.AIR) {
					return;
				}
				Furnace furnace = (Furnace)block.getState();
				if(furnace != null){
					if(furnace.getBurnTime() <= 10) {
						return;
					}
					if(furnace.getCookTime() <= 0) {
						Bukkit.getScheduler().runTaskLater(UhcHost.instance, this, 5L);
						return;
					}
					short newCookeTime = (short)(furnace.getCookTime() + 10);
					if(newCookeTime >= 200) {
						newCookeTime = 199;
					}
					furnace.setCookTime(newCookeTime);
					furnace.update();
					Bukkit.getScheduler().runTaskLater(UhcHost.instance, this, 2L);
				}

			}
				
		}, 1L);
	}
		

}
