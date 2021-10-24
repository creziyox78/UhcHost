package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class DoubleOres extends Scenario {
	
	public DoubleOres() {
		super("Double Ores", Arrays.asList(I18n.tl("scenarios.doubleores")), Material.COAL_ORE);
	}
	
	@EventHandler
	public void onBreakPlace(BlockBreakEvent e) {
		Material material = e.getBlock().getType();
		if(material == null) {
			return;
		}
		switch (material) {
		case COAL_ORE:
			e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.COAL, 1));
			break;
		case IRON_ORE:
			e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(material));
			break;
		case GOLD_ORE:
			e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(material));
			break;
		case DIAMOND_ORE:
			e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.DIAMOND, 1));
			break;
		case EMERALD_ORE:
			e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.EMERALD, 1));
			break;
		case QUARTZ_ORE:
			e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.QUARTZ, 1));
			break;
		default:
			break;
		}
	}

}
