package fr.lastril.uhchost.game.rules;

import fr.lastril.uhchost.inventory.guis.rules.StuffRulesGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;

public class StuffRules {

    private int maxDiamondArmor;

    private boolean countDiamondSword;

    public IQuickInventory getGui() {
        return new StuffRulesGui(this);
    }

    public int getMaxDiamondArmor() {
        return maxDiamondArmor;
    }

    public void setMaxDiamondArmor(int maxDiamondArmor) {
        this.maxDiamondArmor = maxDiamondArmor;
    }

    public boolean isCountDiamondSword() {
        return countDiamondSword;
    }

    public void setCountDiamondSword(boolean countDiamondSword) {
        this.countDiamondSword = countDiamondSword;
    }
}
