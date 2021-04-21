package fr.lastril.uhchost.scenario.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;

public class CutClean extends Scenario {

	public CutClean() {
		super("Cut Clean", Arrays.asList( I18n.tl("scenarios.cutclean.lore", new String[0]), I18n.tl("scenarios.cutclean.lore1", new String[0])), Material.IRON_INGOT);
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		ItemStack is = event.getEntity().getItemStack();
		
		if (is == null)
			return;
		ItemStack replace = null;
		switch (is.getType()) {
			
		case IRON_ORE:
			ExperienceOrb orb = event.getEntity().getLocation().getWorld().spawn(event.getEntity().getLocation().add(0.5, 0.5, 0.5), ExperienceOrb.class);
			orb.setExperience(2);
			replace = new ItemStack(Material.IRON_INGOT);
			break;
		case GOLD_ORE:
			ExperienceOrb orb1 = event.getEntity().getLocation().getWorld().spawn(event.getEntity().getLocation().add(0.5, 0.5, 0.5), ExperienceOrb.class);
			orb1.setExperience(2);
			replace = new ItemStack(Material.GOLD_INGOT);
			break;
		default:
			break;
		}
		if (replace != null)
			event.getEntity().setItemStack(replace);
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		List<ItemStack> loots = event.getDrops();
		for (int i = loots.size() - 1; i >= 0; i--) {
			ItemStack is = loots.get(i);
			if (is == null)
				return;
			switch (is.getType()) {
			case RAW_BEEF:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_BEEF));
				break;
			case PORK:
				loots.remove(i);
				loots.add(new ItemStack(Material.GRILLED_PORK));
				break;
			case RAW_CHICKEN:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_CHICKEN));
				break;
			case MUTTON:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_MUTTON));
				break;
			case RABBIT:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_RABBIT));
				break;
			default:
				break;
			}
		}
	}

}
