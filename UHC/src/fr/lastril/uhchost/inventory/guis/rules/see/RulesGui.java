package fr.lastril.uhchost.inventory.guis.rules.see;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.game.GameManager;
import fr.lastril.uhchost.game.rules.EnchantmentRules;
import fr.lastril.uhchost.game.rules.StuffRules;
import fr.lastril.uhchost.game.rules.enums.GameplayRulesList;
import fr.lastril.uhchost.game.rules.world.BlocsRules;
import fr.lastril.uhchost.inventory.guis.rules.see.items.RulesLootsGui;
import fr.lastril.uhchost.inventory.guis.rules.see.items.RulesPotionsGui;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import fr.lastril.uhchost.tools.I18n;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class RulesGui extends IQuickInventory {

    private final GameManager gameManager = UhcHost.getInstance().getGamemanager();

    private final BlocsRules blocsRules = gameManager.getBlocsRules();
    private final StuffRules stuffRules = gameManager.getStuffRules();
    private final EnchantmentRules enchantmentRules = gameManager.getEnchantmentRules();


    public RulesGui() {
        super(I18n.tl("guis.rules.name"), 2*9);
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("rules", taskUpdate -> {
            inv.setItem(new QuickItem(Material.BOOK).setName("§bScénarios").toItemStack(), onClick -> {
                new RulesScenariosGui().open(onClick.getPlayer());
            }, 1);
            inv.setItem(new QuickItem(Material.APPLE).setName("§bTaux de drop").toItemStack(), onClick ->{
                new RulesLootsGui().open(onClick.getPlayer());
            }, 3);
            inv.setItem(new QuickItem(Material.POTION).setName("§bPotions").toItemStack(), onClick -> {
                new RulesPotionsGui().open(onClick.getPlayer());
            }, 5);

            List<String> gameplayLores = new ArrayList<>();
            gameplayLores.add("");
            for(GameplayRulesList gameplayRulesList : GameplayRulesList.values()){
                gameplayLores.add("§6● §f"+gameplayRulesList.getName()+ ": " + (gameplayRulesList.isEnabled() ? "§aActivé" : "§cDésactivé"));
            }
            inv.setItem(new QuickItem(Material.ITEM_FRAME).setName("§6Gameplay")
                    .setLore(gameplayLores)
                    .toItemStack(), 7);

            inv.setItem(new QuickItem(Material.STONE).setName("§6Blocs")
                    .setLore("",
                            "§6● §fBoost XP:§6 " + blocsRules.getBoostXP() + "%",
                            "§6● §fLave:§6 " + (blocsRules.isLava() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fBriquet:§6 " + (blocsRules.isFlint_steal() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fVariante Stone:§6 " + (blocsRules.isStoneVariant() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), 11);

            inv.setItem(new QuickItem(Material.ENCHANTMENT_TABLE).setName("§6Enchantement")
                    .setLore("",
                            "§6● §fProtection Diamant:§6 " + enchantmentRules.getDiamondProtectionLimit(),
                            "§6● §fProtection Fer:§6 " + enchantmentRules.getIronProtectionLimit(),
                            "§6● §fThorns:§6 " + enchantmentRules.getThornsLimit(),
                            "§6● §fDepth Strider:§6 " + enchantmentRules.getDepthStriderLimit(),
                            "",
                            "§6● §fSharpness Diamant:§6 " + enchantmentRules.getDiamondSharpnessLimit(),
                            "§6● §fSharpness Fer:§6 " + enchantmentRules.getIronSharpnessLimit(),
                            "§6● §fKnockBack:§6 " + enchantmentRules.getKnockbackLimit(),
                            "§6● §fFire Aspect:§6 " + (enchantmentRules.isAutorisedFireAspect() ? "§aActivé" : "§cDésactivé"),
                            "",
                            "§6● §fPower:§6 " + enchantmentRules.getPowerLimit(),
                            "§6● §fFlame:§6 " + (enchantmentRules.isAutorisedFlame() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fInfinity:§6 " + (enchantmentRules.isAutorisedInfinity() ? "§aActivé" : "§cDésactivé"),
                            "§6● §fPunch:§6 " + enchantmentRules.getMaxPunch())
                    .toItemStack(), 13);

            inv.setItem(new QuickItem(Material.DIAMOND_CHESTPLATE).setName("§6Stuff")
                    .setLore("",
                            "§6● §fPièces en Diamant:§6 " + stuffRules.getMaxDiamondArmor(),
                            "§6● §fEpée comptant comme pièce:§6 " + (stuffRules.isCountDiamondSword() ? "§aActivé" : "§cDésactivé"))
                    .toItemStack(), 15);
        });
    }
}
