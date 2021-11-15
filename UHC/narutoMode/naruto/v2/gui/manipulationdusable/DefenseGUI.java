package fr.lastril.uhchost.modes.naruto.v2.gui.manipulationdusable;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.manipulationdusable.defense.ArmureDefense;
import fr.maygo.uhc.modes.naruto.v2.manipulationdusable.defense.BouclierDefense;
import fr.maygo.uhc.modes.naruto.v2.manipulationdusable.defense.SuspensionDefense;
import fr.maygo.uhc.modes.naruto.v2.roles.solo.Gaara;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DefenseGUI extends IQuickInventory {

    private final UhcHost main;
    private final Gaara gaara;

    public DefenseGUI(UhcHost main, Gaara gaara) {
        super("§aDéfense", 9 * 1);
        this.main = main;
        this.gaara = gaara;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        //2 - 4 - 6

        inv.setItem(new QuickItem(Material.SANDSTONE)
                .setLore("",
                        "§7Vous serez entouré de blocs de sable",
                        "§7qui seront difficiles à casser pour les",
                        "§7autres PlayerManagers, sauf pour vous.",
                        "",
                        "§eSables requis:§6 " + (gaara.isInShukaku() ? "32" : "64"))
                .setName("§aBouclier de Sable").toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez choisi le Bouclier de Sable. Faites des cliques gauches pour utiliser votre pouvoir.");
            gaara.setSandShape(new BouclierDefense());
            inv.close(player);
        }, 2);

        inv.setItem(new QuickItem(Material.IRON_CHESTPLATE)
                .setLore("",
                        "§7Permet de vous procurer l'effet",
                        "§9Résistance I§7 tant que vous avez 5 blocs de sable",
                        "§7dans votre inventaire.",
                        "",
                        "§eSables requis:§6 " + (gaara.isInShukaku() ? "16" : "32"))
                .setName("§aArmure de Sable").toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez choisi l'Armure de Sable. Faites des cliques gauches pour utiliser votre pouvoir.");
            gaara.setSandShape(new ArmureDefense());
            inv.close(player);
        }, 4);

        inv.setItem(new QuickItem(Material.FEATHER)
                .setLore("",
                        "§7Permet de§e vous envoler§7 pendant 20 secondes",
                        "",
                        "§eSables requis:§6 " + (gaara.isInShukaku() ? "48" : "96"))
                .setName("§aSuspension du Désert").toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez choisi la Suspension du Désert. Faites des cliques gauches pour utiliser votre pouvoir.");
            gaara.setSandShape(new SuspensionDefense());
            inv.close(player);
        }, 6);

    }
}
