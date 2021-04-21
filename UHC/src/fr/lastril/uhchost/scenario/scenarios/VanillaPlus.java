package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class VanillaPlus extends Scenario {

	public VanillaPlus() {
		super("Vanilla +", Arrays.asList(I18n.tl("scenarios.vanillaplus.lore", ""), I18n.tl("scenarios.vanillaplus.lore1", "")), Material.IRON_SPADE);
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		ItemStack is = event.getEntity().getItemStack();
		if (is == null)
			return;
		if (is.getType() == Material.GRAVEL)
			event.getEntity().setItemStack(new ItemStack(Material.FLINT));
	}

	@EventHandler
	public void onLeaveDecay(LeavesDecayEvent event) {
		if ((new Random()).nextInt(100) + 1 <= 15)
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE));
	}

}
