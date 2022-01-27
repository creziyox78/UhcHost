package fr.lastril.uhchost.inventory.guis;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.CustomInv;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

public class StartInventoryGui extends IQuickInventory {

	private final UhcHost main;
	int itemSlot = 9;
	int item = 0;
	
	public StartInventoryGui(UhcHost main) {
		super("§aInventaire", 9*6);
		this.main = main;
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);
		inv.setItem(CustomInv.boots, 0);
		inv.setItem(CustomInv.leggings, 1);
		inv.setItem(CustomInv.chestplate, 2);
		inv.setItem(CustomInv.helmet, 3);
		

		CustomInv.inventoryContents.values().forEach(itemStacks -> {
			inv.setItem(itemStacks[item], itemSlot);
			item++;
			itemSlot++;
		});
	}

}
