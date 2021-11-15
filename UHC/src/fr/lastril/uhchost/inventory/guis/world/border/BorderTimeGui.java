package fr.lastril.uhchost.inventory.guis.world.border;

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

public class BorderTimeGui extends IQuickInventory {

	private BannerCreator bc;
	public BorderTimeGui() {
		super(I18n.tl("guis.borderTime.name"), 1*9);
	}



	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {


			bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = UhcHost.getInstance().taskManager.getBorderTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setBorderTime(value);
			}, 0);
			bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = UhcHost.getInstance().taskManager.getBorderTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setBorderTime(value);

			}, 1);
			bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = UhcHost.getInstance().taskManager.getBorderTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setBorderTime(value);
			}, 2);
			bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);



			ItemsCreator ic = new ItemsCreator(Material.COBBLE_WALL,
					"§e" + (UhcHost.getInstance().taskManager.getBorderTime() / 60),
					Arrays.asList(I18n.tl("guis.borderTime.lore")));
			inv.setItem(ic.create(), onClick -> {
				new BorderGui().open(onClick.getPlayer());
			},4);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = UhcHost.getInstance().taskManager.getBorderTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setBorderTime(value);
			}, 6);
			bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = UhcHost.getInstance().taskManager.getBorderTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setBorderTime(value);
			}, 7);
			bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = UhcHost.getInstance().taskManager.getBorderTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setBorderTime(value);
			}, 8);
		});

	}
}
