package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.rules.world.BlocsRules;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CutClean extends Scenario {

	private final BlocsRules blocsRules = UhcHost.getInstance().getGamemanager().getBlocsRules();

	public CutClean() {
		super("Cut Clean", Arrays.asList( I18n.tl("scenarios.cutclean.lore"), I18n.tl("scenarios.cutclean.lore1")), Material.IRON_INGOT);
	}

	@EventHandler
	public void onItemSpawn(ItemSpawnEvent event) {
		ItemStack is = event.getEntity().getItemStack();
		
		if (is == null)
			return;
		ItemStack replace = null;
		switch (is.getType()) {
			case QUARTZ_ORE:
			case COAL_ORE:
			case LAPIS_ORE:
			case EMERALD_ORE:
			case DIAMOND_ORE:
			case REDSTONE_ORE:
				blocsRules.dropXP(event.getLocation(), blocsRules.getBoostXP());
				break;
			case IRON_ORE:
				blocsRules.dropXP(event.getLocation(), blocsRules.getBoostXP());
				replace = new ItemStack(Material.IRON_INGOT);
				break;
			case GOLD_ORE:
				blocsRules.dropXP(event.getLocation(), blocsRules.getBoostXP());
				replace = new ItemStack(Material.GOLD_INGOT);
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
				blocsRules.dropXP(event.getEntity().getLocation(), blocsRules.getBoostXP());
				break;
			case PORK:
				loots.remove(i);
				loots.add(new ItemStack(Material.GRILLED_PORK));
				blocsRules.dropXP(event.getEntity().getLocation(), blocsRules.getBoostXP());
				break;
			case RAW_CHICKEN:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_CHICKEN));
				blocsRules.dropXP(event.getEntity().getLocation(), blocsRules.getBoostXP());
				break;
			case MUTTON:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_MUTTON));
				blocsRules.dropXP(event.getEntity().getLocation(), blocsRules.getBoostXP());
				break;
			case RABBIT:
				loots.remove(i);
				loots.add(new ItemStack(Material.COOKED_RABBIT));
				blocsRules.dropXP(event.getEntity().getLocation(), blocsRules.getBoostXP());
				break;
			default:
				break;
			}
		}
	}

}
