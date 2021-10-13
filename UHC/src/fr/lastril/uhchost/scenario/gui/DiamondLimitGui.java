package fr.lastril.uhchost.scenario.gui;

import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.scenario.scenarios.DiamondLimit;
import fr.lastril.uhchost.scenario.scenarios.Diamondless;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class DiamondLimitGui extends TimerGui {

	public DiamondLimitGui(Player player) {
		super(player, I18n.tl("guis.diamondlimit.name", new String[0]));
		ItemsCreator ic = new ItemsCreator(Material.DIAMOND_ORE, "§e" + DiamondLimit.getMaxDiamond(),
				Arrays.asList(I18n.tl("guis.diamondlimit.lore", new String[0])));
		inventory.setItem(4, ic.create());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClickedInventory().equals(inventory)) {
			String name;
			int value;
			ItemsCreator ic;
			ItemStack is = event.getCurrentItem();
			if (is == null || is.getType() == Material.AIR)
				return;
			event.setCancelled(true);
			switch (is.getType()) {
			case DIAMOND_ORE:
				this.player.closeInventory();
				(new ScenariosGui(this.player)).show();
				break;
			case BANNER:
				name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
				value = DiamondLimit.getMaxDiamond() + Integer.parseInt(name);
				if (value < 1)
					break;
				DiamondLimit.setMaxDiamond(value);
				ic = new ItemsCreator(Material.DIAMOND, "§e" + DiamondLimit.getMaxDiamond(),
						Arrays.asList(I18n.tl("guis.diamondlimit.lore", new String[0])));
				inventory.setItem(4, ic.create());
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	public void onClick(InventoryCloseEvent event) {
		if (event.getInventory().equals(inventory))
			HandlerList.unregisterAll(this);
	}

}
