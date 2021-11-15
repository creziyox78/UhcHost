package fr.lastril.uhchost.inventory.guis.world.border;

import fr.lastril.uhchost.inventory.guis.timer.RulesGuiHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Arrays;

public class BorderGui extends IQuickInventory {

	public BorderGui() {
		super(I18n.tl("guis.border.name"), 18);

	}

	@Override
	public void contents(QuickInventory inv) {
		inv.updateItem("update", taskUpdate -> {
			ItemsCreator ic = new ItemsCreator(Material.STAINED_GLASS_PANE,
					I18n.tl("guis.border.initialSize"),
					Arrays.asList(I18n.tl("guis.border.initialSizeLore")), 1, (byte) 13);
			inv.setItem(ic.create(), onClick -> {
				new StartBorderSizeGui().open(onClick.getPlayer());
			},3);
			ic = new ItemsCreator(Material.NETHER_STAR, I18n.tl("guis.border.speed"),
					Arrays.asList(I18n.tl("guis.border.speedLore")));
			inv.setItem(ic.create(), onClick -> {
				new BorderSpeedGui().open(onClick.getPlayer());
			},4);
			ic = new ItemsCreator(Material.STAINED_GLASS_PANE, I18n.tl("guis.border.finalSize"),
					Arrays.asList(I18n.tl("guis.border.finalSizeLore")), 1, (byte) 14);
			inv.setItem(ic.create(), onClick -> {
				new FinalBorderSizeGui().open(onClick.getPlayer());
			},5);
			ic = new ItemsCreator(Material.COBBLE_WALL, I18n.tl("guis.border.time"),
					Arrays.asList(I18n.tl("guis.border.timeLore")));
			inv.setItem(ic.create(), onClick -> {
				new BorderTimeGui().open(onClick.getPlayer());
			},13);
			inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), null)).create(), onClick -> {
				new RulesGuiHost().open(onClick.getPlayer());
			},17);
		});
	}
}
