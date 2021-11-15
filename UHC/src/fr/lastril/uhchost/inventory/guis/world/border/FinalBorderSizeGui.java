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

public class FinalBorderSizeGui extends IQuickInventory {

	private BannerCreator bc;
	public FinalBorderSizeGui() {
		super(I18n.tl("guis.finalBorderSize.name"), 1*9);
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {


			bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getFinalSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setFinalSize(value);
			}, 0);
			bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getFinalSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setFinalSize(value);

			}, 1);
			bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getFinalSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setFinalSize(value);
			}, 2);
			bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);



			ItemsCreator  ic = new ItemsCreator(Material.STAINED_GLASS_PANE,
					"§c" + (UhcHost.getInstance()).worldBorderUtils.getFinalSize(),
					Arrays.asList(I18n.tl("guis.finalBorderSize.lore"),
							I18n.tl("guis.finalBorderSize.lore1"),
							I18n.tl("guis.finalBorderSize.lore2")),
					1, (byte) 14);
			inv.setItem(ic.create(), onClick -> {
				new BorderGui().open(onClick.getPlayer());
			},4);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getFinalSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setFinalSize(value);
			}, 6);
			bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getFinalSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setFinalSize(value);
			}, 7);
			bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = (UhcHost.getInstance()).worldBorderUtils.getFinalSize() + Integer.parseInt(bannerName);
				(UhcHost.getInstance()).worldBorderUtils.setFinalSize(value);
			}, 8);

		});

	}
}
