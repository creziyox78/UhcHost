package fr.lastril.uhchost.inventory.guis.scenarios;

import fr.lastril.uhchost.scenario.ScenariosGui;
import fr.lastril.uhchost.scenario.scenarios.Goldless;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;

public class GoldlessGui extends IQuickInventory {

	private BannerCreator bc;

	public GoldlessGui() {
		super(I18n.tl("guis.goldless.name"), 1*9);
	}


	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {


			bc = new BannerCreator("§c-10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = Goldless.getLootNumber() + Integer.parseInt(bannerName);
				Goldless.setLootNumber(value);
			}, 0);
			bc = new BannerCreator("§c-5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = Goldless.getLootNumber() + Integer.parseInt(bannerName);
				Goldless.setLootNumber(value);

			}, 1);
			bc = new BannerCreator("§c-1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = Goldless.getLootNumber() + Integer.parseInt(bannerName);
				Goldless.setLootNumber(value);
			}, 2);
			bc = new BannerCreator("§a+1", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);



			ItemsCreator ic = new ItemsCreator(Material.GOLD_ORE, "§e" + Goldless.getLootNumber(),
					Arrays.asList(I18n.tl("guis.goldless.lore")));
			inv.setItem(ic.create(), onClick -> {
				new ScenariosGui().open(onClick.getPlayer());
			},4);


			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = Goldless.getLootNumber() + Integer.parseInt(bannerName);
				Goldless.setLootNumber(value);
			}, 6);
			bc = new BannerCreator("§a+5", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = Goldless.getLootNumber() + Integer.parseInt(bannerName);
				Goldless.setLootNumber(value);
			}, 7);
			bc = new BannerCreator("§a+10", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				String bannerName = ChatColor.stripColor(onClick.getEvent().getCurrentItem().getItemMeta().getDisplayName());
				int value = Goldless.getLootNumber() + Integer.parseInt(bannerName);
				Goldless.setLootNumber(value);
			}, 8);

		});

	}
}
