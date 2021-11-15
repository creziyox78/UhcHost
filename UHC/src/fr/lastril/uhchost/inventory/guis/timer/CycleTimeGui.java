package fr.lastril.uhchost.inventory.guis.timer;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;

public class CycleTimeGui extends IQuickInventory {

	private BannerCreator bc;

	public CycleTimeGui() {
		super(I18n.tl("guis.cycleTime.name"), 1*9);
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {

			bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);

			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (int) ((UhcHost.getInstance()).gameManager.getCycleTime() + Integer.parseInt(bannerName) * 60);
				(UhcHost.getInstance()).gameManager.setCycleTime(value);
			}, 0);
			bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (int) ((UhcHost.getInstance()).gameManager.getCycleTime() + Integer.parseInt(bannerName) * 60);
				(UhcHost.getInstance()).gameManager.setCycleTime(value);

			}, 1);
			bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (int) ((UhcHost.getInstance()).gameManager.getCycleTime() + Integer.parseInt(bannerName) * 60);
				(UhcHost.getInstance()).gameManager.setCycleTime(value);
			}, 2);
			bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);



			ItemsCreator ic = new ItemsCreator(Material.WATCH,
					"§e" + (UhcHost.getInstance().gameManager.getCycleTime() / 60),
					Arrays.asList(I18n.tl("guis.cycleTime.lore"), I18n.tl("guis.cycleTime.lore1")));
			inv.setItem(ic.create(), onClick -> {
				new RulesGuiHost().open(onClick.getPlayer());
			},4);

			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (int) ((UhcHost.getInstance()).gameManager.getCycleTime() + Integer.parseInt(bannerName) * 60);
				(UhcHost.getInstance()).gameManager.setCycleTime(value);
			}, 6);
			bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (int) ((UhcHost.getInstance()).gameManager.getCycleTime() + Integer.parseInt(bannerName) * 60);
				(UhcHost.getInstance()).gameManager.setCycleTime(value);
			}, 7);
			bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (int) ((UhcHost.getInstance()).gameManager.getCycleTime() + Integer.parseInt(bannerName) * 60);
				(UhcHost.getInstance()).gameManager.setCycleTime(value);
			}, 8);

		});

	}
}
