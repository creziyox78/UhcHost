package fr.lastril.uhchost.modes.naruto.v2.gui.manipulationdusable;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque.LanceAttaque;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque.SarcophageAttaque;
import fr.lastril.uhchost.modes.naruto.v2.manipulationdusable.attaque.TsunamiAttaque;
import fr.lastril.uhchost.modes.naruto.v2.roles.solo.Gaara;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AttaqueGUI extends IQuickInventory {

    private final UhcHost main;
    private final Gaara gaara;

    public AttaqueGUI(UhcHost main, Gaara gaara) {
        super("§cAttaque", 9*1);
        this.main = main;
        this.gaara = gaara;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        //2 - 4 - 6

        inv.setItem(new QuickItem(Material.WATER_BUCKET).setName("§cTsunami de Sable")
                .setLore("",
                        "§7Permet de créer un §etsunami de devant vous",
                        "§7qui §epropulse§7 les joueurs touchés.",
                        "",
                        "§eSables requis:§6 " + (gaara.isInShukaku() ? "15" : "30"))
                .toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            gaara.setSandShape(new TsunamiAttaque());
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez choisi le Tsunami de Sable. Faites des cliques gauches pour utiliser votre pouvoir.");
            inv.close(player);
        }, 2);

        inv.setItem(new QuickItem(Material.SAND).setName("§cSarcophage de Sable")
                .setLore("",
                        "§7Permet §ed'enfermer un joueur§7 sous un carré",
                        "§7de sable.",
                        "",
                        "§eSables requis:§6 " + (gaara.isInShukaku() ? "23" : "45"))
                .toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            gaara.setSandShape(new SarcophageAttaque());
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez choisi le Sarcophage de Sable. Faites des cliques gauches sur un joueur pour utiliser votre pouvoir.");
            inv.close(player);
        }, 4);

        inv.setItem(new QuickItem(Material.DIAMOND_SWORD).setName("§cLance")
                .setLore("",
                        "§7Permet de récupérer des épées en diamants",
                        "§cTranchant 4§7 avec §f25 points de durabilités.",
                        "",
                        "§eSables requis:§6 " + (gaara.isInShukaku() ? "32" : "64"))
                .toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            gaara.setSandShape(new LanceAttaque());
            player.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez choisi la Lance. Faites des cliques gauches pour récupérer des lances de sable.");
            inv.close(player);
        }, 6);

    }
}
