package fr.lastril.uhchost.modes.naruto.v2.gui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Ino;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;

import java.util.stream.Collectors;

public class InoGUI extends IQuickInventory {

    private final UhcHost main;
    private final Ino ino;

    public InoGUI(UhcHost main, Ino ino) {
        super(ChatColor.GOLD+"Ino", 9*6);
        this.main = main;
        this.ino = ino;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (PlayerManager joueur : main.getPlayerManagerAlives().stream().filter(joueur -> !joueur.getUuid().equals(ino.getPlayerId())).collect(Collectors.toList())) {
            OfflinePlayer offlinePlayer = main.getServer().getOfflinePlayer(joueur.getUuid());

            boolean inChat = ino.getInChat().contains(joueur.getUuid());

            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(offlinePlayer.getName())
                    .setLore(inChat ? "§aPeut voir votre chat" : "§cNe peut pas voir votre chat",
                            "",
                            "§7Cliquez pour permettre à ce joueur de voir/ne plus voir vos messages.")
                    .setSkullOwner(offlinePlayer.getName())
                    .toItemStack(), onClick -> {
                if (inChat) {
                    ino.getInChat().remove(joueur.getUuid());
                }else{
                    ino.getInChat().add(joueur.getUuid());
                }
                inv.update(onClick.getPlayer(), this);
            });
        }

    }
}
