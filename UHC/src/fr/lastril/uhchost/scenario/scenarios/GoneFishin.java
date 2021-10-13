package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GoneFishin extends Scenario {

	public GoneFishin() {
		super("Gone Fishin",
				Arrays.asList(I18n.tl("scenarios.gonefishin.lore", new String[0]),
						I18n.tl("scenarios.gonefishin.lore1", new String[0]),
						I18n.tl("scenarios.gonefishin.lore2", new String[0]),
						I18n.tl("scenarios.gonefishin.lore3", new String[0])),
				Material.FISHING_ROD);
	}

	@EventHandler
	public void onGameStart(GameStartEvent event) {
		ItemStack rod = new ItemStack(Material.FISHING_ROD);
		rod.addUnsafeEnchantment(Enchantment.LUCK, 250);
		rod.addUnsafeEnchantment(Enchantment.DURABILITY, 250);
		rod.addUnsafeEnchantment(Enchantment.LURE, 3);
		ItemStack anvil = new ItemStack(Material.ANVIL, 10);
		event.getPlayers().forEach(p -> {
			p.getInventory().addItem(rod);
			p.getInventory().addItem(anvil);
			p.setLevel(999999);
		});
	}

}
