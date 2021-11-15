package fr.lastril.uhchost.scenario.scenarios;

import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InfiniteEnchanter extends Scenario {

	public InfiniteEnchanter() {
		super("Infinite Enchanter",
				Arrays.asList(I18n.tl("scenarios.infiniteenchanter.lore"),
						I18n.tl("scenarios.infiniteenchanter.lore1"),
						I18n.tl("scenarios.infiniteenchanter.lore2"),
						I18n.tl("scenarios.infiniteenchanter.lore3")),
				Material.ENCHANTMENT_TABLE);
	}

	@EventHandler
	public void onGameStart(GameStartEvent event) {
		List<ItemStack> kit = Arrays
				.asList((new ItemsCreator(Material.ENCHANTMENT_TABLE, null, null, 5)).create(),
						(new ItemsCreator(Material.ANVIL, null, null, 5)).create(),
						(new ItemsCreator(Material.BOOKSHELF, null, null, 128)).create(),
						(new ItemsCreator(Material.BOOK, null, null, 64)).create(),
						(new ItemsCreator(Material.LAPIS_BLOCK, null, null, 64)).create(),
						(new ItemsCreator(Material.DIAMOND_AXE, null, null)).create());
		for (Player player : event.getPlayers()) {
			player.setLevel(999999);
			kit.forEach(i -> player.getInventory().addItem(new ItemStack[] { i }));
		}
	}

}
