package fr.lastril.uhchost.scenario;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ActiveScenariosGui extends IQuickInventory {

	public ActiveScenariosGui() {
		super(I18n.tl("guis.activescenarios.name"), 54);

	}

	@Override
	public void contents(QuickInventory inv) {
		for (Scenario scenario : UhcHost.getInstance().getGamemanager().getScenarios()) {
			if (scenario.getData() != 0) {
				inv.addItem(new ItemStack(new ItemsCreator(scenario.getType(), scenario.getName(),
						scenario.getDescritpion(), 1, scenario.getData()).create()));
				continue;
			}
			inv.addItem(new ItemStack(new ItemsCreator(scenario.getType(), scenario.getName(), scenario.getDescritpion()).create()));
		}
		if(UhcHost.getInstance().getGamemanager().getScenarios().isEmpty()) {
			inv.setItem(new ItemStack(new ItemsCreator(Material.BARRIER, I18n.tl("guis.activescenarios.noscenario", ""), Arrays.asList("")).create()), 22);
		}
	}
}
