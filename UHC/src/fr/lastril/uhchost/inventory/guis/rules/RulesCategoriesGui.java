package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.rules.EnchantmentRules;
import fr.lastril.uhchost.game.rules.StuffRules;
import fr.lastril.uhchost.game.rules.enums.GameplayRulesList;
import fr.lastril.uhchost.game.rules.world.BlocsRules;
import fr.lastril.uhchost.inventory.guis.HostConfig;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class RulesCategoriesGui extends IQuickInventory {

    private final UhcHost main = UhcHost.getInstance();
    private final GameManager gameManager = main.getGamemanager();

    private final BlocsRules blocsRules = gameManager.getBlocsRules();
    private final StuffRules stuffRules = gameManager.getStuffRules();
    private final EnchantmentRules enchantmentRules = gameManager.getEnchantmentRules();

    public RulesCategoriesGui() {
        super("§6Règles", 3*9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("rulescategories", taskUpdate -> {
            inv.setItem(new QuickItem(Material.STONE).setName("§6Blocs")
                    .setLore("",
                            "§6● §fBoost XP:§b " + blocsRules.getBoostXP() + "%",
                            "§6● §fLave:§b " + (blocsRules.isLava() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fBriquet:§b " + (blocsRules.isFlint_steal() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fVariante Stone:§b " + (blocsRules.isStoneVariant() ? "§aActivé" : "§cDésactivé")).toItemStack(), onClick -> {
                main.getGamemanager().getBlocsRules().getGui().open(onClick.getPlayer());
            },10);

            inv.setItem(new QuickItem(Material.ENCHANTMENT_TABLE).setName("§6Enchantement")
                    .setLore("",
                            "§6● §fProtection Diamant:§b " + enchantmentRules.getDiamondProtectionLimit(),
                            "§6● §fProtection Fer:§b " + enchantmentRules.getIronProtectionLimit(),
                            "§6● §fThorns:§b " + enchantmentRules.getThornsLimit(),
                            "§6● §fDepth Strider:§b " + enchantmentRules.getDepthStriderLimit(),
                            "",
                            "§6● §fSharpness Diamant:§b " + enchantmentRules.getDiamondSharpnessLimit(),
                            "§6● §fSharpness Fer:§b " + enchantmentRules.getIronSharpnessLimit(),
                            "§6● §fKnockBack:§b " + enchantmentRules.getKnockbackLimit(),
                            "§6● §fFire Aspect:§b " + (enchantmentRules.isAutorisedFireAspect() ? "§aActivé" : "§cDésactivé"),
                            "",
                            "§6● §fPower:§b " + enchantmentRules.getPowerLimit(),
                            "§6● §fFlame:§b " + (enchantmentRules.isAutorisedFlame() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fInfinity:§b " + (enchantmentRules.isAutorisedInfinity() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fPunch:§b " + enchantmentRules.getMaxPunch())
                    .toItemStack(), onClick -> {
                main.getGamemanager().getEnchantmentRules().getGui().open(onClick.getPlayer());
            },12);

            inv.setItem(new QuickItem(Material.DIAMOND_CHESTPLATE).setName("§6Stuff")
                    .setLore("",
                            "§6● §fPièces en Diamant: " + stuffRules.getMaxDiamondArmor(),
                            "§6● §fEpée comptant comme pièce: " + (stuffRules.isCountDiamondSword() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), onClick -> {
                main.getGamemanager().getStuffRules().getGui().open(onClick.getPlayer());
            },14);

            List<String> gameplayLores = new ArrayList<>();
            gameplayLores.add("");
            for(GameplayRulesList gameplayRulesList : GameplayRulesList.values()){
                gameplayLores.add("§6● §f"+gameplayRulesList.getName()+ ": " + (gameplayRulesList.isEnabled() ? "§aActivé" : "§cDésactivé"));
            }
            inv.setItem(new QuickItem(Material.ITEM_FRAME).setName("§6Gameplay")
                    .setLore(gameplayLores)
                    .toItemStack(), onClick -> {
                new GameplayRulesGui().open(onClick.getPlayer());
            },16);

            inv.addRetourItem(new HostConfig());
        });
    }
}
