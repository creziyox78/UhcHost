package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.rules.world.BlocsRules;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

public class BlocsRulesGui extends IQuickInventory {

    private final UhcHost main;
    private final BlocsRules blocsRules;

    public BlocsRulesGui(UhcHost main, BlocsRules blocsRules) {
        super("§bRègles > Blocs", 2*9);
        this.main = main;
        this.blocsRules = blocsRules;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("blocsrules", taskUpdate -> {
            inv.setItem(new QuickItem(Material.STONE)
                    .setName("§fVariante Stone:§6 " + (blocsRules.isStoneVariant() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), onClick ->
                    blocsRules.setStoneVariant(!blocsRules.isStoneVariant()), 0);
            inv.setItem(new QuickItem(Material.FLINT_AND_STEEL)
                    .setName("§fBriquet:§6 " + (blocsRules.isFlint_steal() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), onClick ->
                    blocsRules.setFlint_steal(!blocsRules.isFlint_steal()), 1);
            inv.setItem(new QuickItem(Material.LAVA_BUCKET)
                    .setName("§fLave:§6 " + (blocsRules.isLava() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), onClick ->
                    blocsRules.setLava(!blocsRules.isLava()), 2);
            inv.setItem(new QuickItem(Material.EXP_BOTTLE)
                    .setName("§fBoost XP:§6 " + blocsRules.getBoostXP() + " %")
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick ->{
                if(onClick.getClickType() == ClickType.LEFT){
                    blocsRules.setBoostXP(blocsRules.getBoostXP() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    blocsRules.setBoostXP(blocsRules.getBoostXP() - 1);
                }
            }, 3);
            inv.addRetourItem(new RulesCategoriesGui());
        });

    }
}
