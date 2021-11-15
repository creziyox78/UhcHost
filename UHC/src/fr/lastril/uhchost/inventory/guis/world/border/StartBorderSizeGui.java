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

public class StartBorderSizeGui extends IQuickInventory {

	private BannerCreator bc;

	public StartBorderSizeGui() {
		super(I18n.tl("guis.startBorderSize.name"), 1*9);
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {


			bc = new BannerCreator("§c-100", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);
			}, 0);
			bc = new BannerCreator("§c-50", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);

			}, 1);
			bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);
			}, 2);
			bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);



			ItemsCreator ic = new ItemsCreator(Material.STAINED_GLASS_PANE,
					"§a" + (UhcHost.getInstance()).worldBorderUtils.getStartSize(),
					Arrays.asList(I18n.tl("guis.startBorderSize.lore"),
							I18n.tl("guis.startBorderSize.lore1"),
							I18n.tl("guis.startBorderSize.lore2")),
					1, (byte) 13);
			inv.setItem(ic.create(), onClick -> {
				new BorderGui().open(onClick.getPlayer());
			},4);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);
			}, 6);
			bc = new BannerCreator("§a+50", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);
			}, 7);
			bc = new BannerCreator("§a+100", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getStartSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setStartSize(value);
			}, 8);

		});

	}
}
