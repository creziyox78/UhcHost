package fr.lastril.uhchost.inventory.guis.rules;

import fr.lastril.uhchost.game.rules.EnchantmentRules;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

public class EnchantmentRulesGui extends IQuickInventory {

    private final EnchantmentRules enchantmentRules;

    public EnchantmentRulesGui(EnchantmentRules enchantmentRules) {
        super("§6Règles > Enchantement", 2*9);
        this.enchantmentRules = enchantmentRules;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.updateItem("rulesenchantment", taskUpdate -> {

            inv.setItem(new QuickItem(Material.DIAMOND_CHESTPLATE).setName("§fProtection Diamant: " + enchantmentRules.getDiamondProtectionLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setDiamondProtectionLimit(enchantmentRules.getDiamondProtectionLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setDiamondProtectionLimit(enchantmentRules.getDiamondProtectionLimit() - 1);
                }

            },0);

            inv.setItem(new QuickItem(Material.IRON_CHESTPLATE).setName("§fProtection Fer: " + enchantmentRules.getIronProtectionLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setIronProtectionLimit(enchantmentRules.getIronProtectionLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setIronProtectionLimit(enchantmentRules.getIronProtectionLimit() - 1);
                }

            },1);

            inv.setItem(new QuickItem(Material.WATER_BUCKET).setName("§fDepth Strider: " + enchantmentRules.getDepthStriderLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setDepthStriderLimit(enchantmentRules.getDepthStriderLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setDepthStriderLimit(enchantmentRules.getDepthStriderLimit() - 1);
                }

            },2);

            inv.setItem(new QuickItem(Material.CACTUS).setName("§fThorns: " + enchantmentRules.getThornsLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setThornsLimit(enchantmentRules.getThornsLimit()+ 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setThornsLimit(enchantmentRules.getThornsLimit() - 1);
                }

            },3);

            inv.setItem(new QuickItem(Material.DIAMOND_SWORD).setName("§fSharpness Diamant: " + enchantmentRules.getDiamondSharpnessLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setDiamondSharpnessLimit(enchantmentRules.getDiamondSharpnessLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setDiamondSharpnessLimit(enchantmentRules.getDiamondSharpnessLimit() - 1);
                }

            },4);

            inv.setItem(new QuickItem(Material.IRON_SWORD).setName("§fSharpness Fer: " + enchantmentRules.getIronSharpnessLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setIronSharpnessLimit(enchantmentRules.getIronSharpnessLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setIronSharpnessLimit(enchantmentRules.getIronSharpnessLimit() - 1);
                }

            },5);

            inv.setItem(new QuickItem(Material.STICK).setName("§fKnockback: " + enchantmentRules.getKnockbackLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setKnockbackLimit(enchantmentRules.getKnockbackLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setKnockbackLimit(enchantmentRules.getKnockbackLimit() - 1);
                }

            },6);

            inv.setItem(new QuickItem(Material.LAVA_BUCKET).setName("§fFire Aspect: " + (enchantmentRules.isAutorisedFireAspect() ? "§aActivé" : "§cDésactivé"))
                    .setLore("")
                    .toItemStack(), onClick -> {
                enchantmentRules.setAutorisedFireAspect(!enchantmentRules.isAutorisedFireAspect());
            },7);

            inv.setItem(new QuickItem(Material.BOW).setName("§fPower: " + enchantmentRules.getPowerLimit())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setPowerLimit(enchantmentRules.getPowerLimit() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setPowerLimit(enchantmentRules.getPowerLimit() - 1);
                }

            },8);

            inv.setItem(new QuickItem(Material.BLAZE_ROD).setName("§fFlame: " + (enchantmentRules.isAutorisedFlame() ? "§aActivé" : "§cDésactivé"))
                    .setLore("")
                    .toItemStack(), onClick -> {
                enchantmentRules.setAutorisedFlame(!enchantmentRules.isAutorisedFlame());
            },9);

            inv.setItem(new QuickItem(Material.ARROW).setName("§fInfinity: " + (enchantmentRules.isAutorisedInfinity() ? "§aActivé" : "§cDésactivé"))
                    .setLore("")
                    .toItemStack(), onClick -> {
                enchantmentRules.setAutorisedInfinity(!enchantmentRules.isAutorisedInfinity());
            },10);

            inv.setItem(new QuickItem(Material.FIREBALL).setName("§fPunch: " + enchantmentRules.getMaxPunch())
                    .setLore("",
                            "§aClique gauche: +1",
                            "§aClique droit: -1")
                    .toItemStack(), onClick -> {
                if(onClick.getClickType() == ClickType.LEFT){
                    enchantmentRules.setPowerLimit(enchantmentRules.getMaxPunch() + 1);
                } else if(onClick.getClickType() == ClickType.RIGHT){
                    enchantmentRules.setPowerLimit(enchantmentRules.getMaxPunch() - 1);
                }

            },11);

            inv.addRetourItem(new RulesCategoriesGui());
        });
    }
}
