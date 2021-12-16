package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.game.rules.enums.GameplayRulesList;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;

public class GameplayRulesGui extends IQuickInventory {

    public GameplayRulesGui() {
        super("§6Règles > Gameplay", 2*9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("gameplayrules", taskUpdate -> {
            int index = -1;
            for(GameplayRulesList gameplayRulesList : GameplayRulesList.values()){
                index++;
                inv.setItem(new QuickItem(gameplayRulesList.getMaterial())
                        .setName("§f" + gameplayRulesList.getName() + ": " + (gameplayRulesList.isEnabled() ? "§aActivé" : "§cDésactivé"))
                        .toItemStack(), onClick -> gameplayRulesList.setEnabled(!gameplayRulesList.isEnabled()), index);
            }

            inv.addRetourItem(new RulesCategoriesGui());
        });
    }
}
