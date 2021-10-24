package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.inventory.Gui;
import fr.lastril.uhchost.scenario.Scenario;
import fr.lastril.uhchost.tools.I18n;
import fr.lastril.uhchost.tools.creators.ItemsCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class RulesGui extends Gui {
    public RulesGui(Player player) {
        super(player, 54, I18n.tl("guis.rules.name"));
        for (Scenario scenario : (UhcHost.getInstance()).gameManager.getScenarios()) {
            if (scenario.getData() != 0) {
                inventory.addItem((new ItemsCreator(scenario.getType(), scenario.getName(), scenario.getDescritpion(), 1, scenario.getData())).create());
                continue;
            }
            inventory.addItem((new ItemsCreator(scenario.getType(), scenario.getName(), scenario.getDescritpion())).create());
        }
        inventory.setItem(53, (new ItemsCreator(Material.BARRIER, I18n.tl("guis.back"), Collections.singletonList(""))).create());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null)
            return;
        if (event.getClickedInventory().equals(inventory)) {
            ItemStack is = event.getCurrentItem();
            if (is == null || is.getType() == Material.AIR)
                return;
            event.setCancelled(true);
            if (is.getType() == Material.BARRIER)
                event.getWhoClicked().closeInventory();
        }
    }

    @EventHandler
    public void onClick(InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory))
            HandlerList.unregisterAll(this);
    }
}
