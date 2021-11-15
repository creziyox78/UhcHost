package fr.lastril.uhchost.team;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.guis.rules.RulesGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.BannerCreator;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.DyeColor;
import org.bukkit.Material;

import java.util.Arrays;

public class ToGui extends IQuickInventory {

	private final int[] possibilities = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 25, 50 };

	public ToGui() {
		super(I18n.tl("guis.teamsOf.name"), 9);
	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {
			ItemsCreator ic = new ItemsCreator(Material.BLAZE_ROD,
					"§e" + (UhcHost.getInstance()).teamUtils.getPlayersPerTeams(),
					Arrays.asList(I18n.tl("guis.teamsOf.lore")));
			if ((UhcHost.getInstance()).teamUtils.getPlayersPerTeams() == 1) {
				(UhcHost.getInstance()).teamUtils.resetTeams();
			} else {
				(UhcHost.getInstance()).teamUtils.setupTeams();
			}
			inv.setItem(ic.create(), onClick -> {
				new RulesGui().open(onClick.getPlayer());
			},4);
			BannerCreator bc = new BannerCreator(I18n.tl("guis.timer.previous"), Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.RED);
			inv.setItem(bc.create(), onClick -> {
				for (int i = 0; i < this.possibilities.length; i++) {
					if (this.possibilities[i] == (UhcHost.getInstance()).teamUtils.getPlayersPerTeams()) {
						if (i <= 0) {
							(UhcHost.getInstance()).teamUtils
									.setPlayersPerTeams(this.possibilities[this.possibilities.length - 1]);
							break;
						}
						(UhcHost.getInstance()).teamUtils.setPlayersPerTeams(this.possibilities[i - 1]);
						break;
					}
				}
			},0);
			bc = new BannerCreator("§aSuivant", Arrays.asList(""), 1, true);
			bc.setBaseColor(DyeColor.GREEN);
			inv.setItem(bc.create(), onClick -> {
				for (int i = 0; i < this.possibilities.length; i++) {
					if (this.possibilities[i] == (UhcHost.getInstance()).teamUtils.getPlayersPerTeams()) {
						if (i + 1 >= this.possibilities.length) {
							(UhcHost.getInstance()).teamUtils.setPlayersPerTeams(this.possibilities[0]);
							break;
						}
						(UhcHost.getInstance()).teamUtils.setPlayersPerTeams(this.possibilities[i + 1]);
						break;
					}
				}
			},8);
		});
	}
}
