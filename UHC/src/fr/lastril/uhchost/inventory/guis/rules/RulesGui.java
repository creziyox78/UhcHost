package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.ItemsCreator;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.Collections;

public class RulesGui extends IQuickInventory {
    public RulesGui() {
        super(I18n.tl("guis.rules.name"), 54);

    }

    @Override
    public void contents(QuickInventory inv) {
        for (Scenario scenario : (UhcHost.getInstance()).gameManager.getScenarios()) {
            if (scenario.getData() != 0) {
                inv.addItem((new ItemsCreator(scenario.getType(), scenario.getName(), scenario.getDescritpion(), 1, scenario.getData())).create());
                continue;
            }
            inv.addItem((new ItemsCreator(scenario.getType(), scenario.getName(), scenario.getDescritpion())).create());
        }
        inv.setItem((new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create(), onClick -> {
            onClick.getPlayer().closeInventory();
        },53);
    }
}
