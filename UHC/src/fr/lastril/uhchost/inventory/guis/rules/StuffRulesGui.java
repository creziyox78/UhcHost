package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.game.rules.StuffRules;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

public class StuffRulesGui extends IQuickInventory {

    private final StuffRules stuffRules;

    public StuffRulesGui(StuffRules stuffRules) {
        super("§6Règles > Stuff", 1*9);
        this.stuffRules = stuffRules;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("stuffrules", taskUpdate -> {

            inv.setItem(new QuickItem(Material.DIAMOND_SWORD)
                    .setName("§fEpée comptant comme pièce: " + (stuffRules.isCountDiamondSword() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), onClick -> stuffRules.setCountDiamondSword(!stuffRules.isCountDiamondSword()),0);

            inv.setItem(new QuickItem(Material.DIAMOND_CHESTPLATE)
                    .setName("§fPièces en Diamant:§6 " + stuffRules.getMaxDiamondArmor())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    stuffRules.setMaxDiamondArmor(stuffRules.getMaxDiamondArmor() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    stuffRules.setMaxDiamondArmor(stuffRules.getMaxDiamondArmor() -1);
                }
            },1);
            inv.addRetourItem(new RulesCategoriesGui());
        });
    }
}
