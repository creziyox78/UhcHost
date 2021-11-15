package fr.lastril.uhchost.inventory.guis.nether;

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

public class NetherEndTimeGui extends IQuickInventory {

	private BannerCreator bc;
	private ItemsCreator ic;

	public NetherEndTimeGui() {
		super(I18n.tl("guis.netherEndTime.name"), 9*1);
	}

	@Override
	public void contents(QuickInventory inv) {


		inv.updateItem("update", taskUpdate -> {
			bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).taskManager.getNetherEndTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setNetherEndTime(value);

			}, 0);
			bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).taskManager.getNetherEndTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setNetherEndTime(value);

			}, 1);
			bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).taskManager.getNetherEndTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setNetherEndTime(value);
			}, 2);
			bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);



			ic = new ItemsCreator(Material.OBSIDIAN,
					"§e" + ((UhcHost.getInstance()).taskManager.getNetherEndTime() / 60),
					Arrays.asList(I18n.tl("guis.netherEndTime.lore")));
			inv.setItem(ic.create(), onClick-> {
				new NetherGui().open(onClick.getPlayer());
			},4);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).taskManager.getNetherEndTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setNetherEndTime(value);
			}, 6);
			bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).taskManager.getNetherEndTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setNetherEndTime(value);
			}, 7);
			bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).taskManager.getNetherEndTime() + Integer.parseInt(bannerName) * 60;
				(UhcHost.getInstance()).taskManager.setNetherEndTime(value);
			}, 8);
		});
	}
}
