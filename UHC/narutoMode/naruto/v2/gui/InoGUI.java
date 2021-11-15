package fr.lastril.uhchost.modes.naruto.v2.gui;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Ino;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;

import java.util.stream.Collectors;

public class InoGUI extends IQuickInventory {

    private final UhcHost main;
    private final Ino ino;

    public InoGUI(UhcHost main, Ino ino) {
        super(ChatColor.GOLD + "Ino", 9 * 6);
        this.main = main;
        this.ino = ino;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (PlayerManager PlayerManager : main.getPlayerManagersAlives().stream().filter(PlayerManager -> !PlayerManager.getId().equals(ino.getPlayerId())).collect(Collectors.toList())) {
            OfflinePlayer offlinePlayer = main.getServer().getOfflinePlayer(PlayerManager.getId());

            boolean inChat = ino.getInChat().contains(PlayerManager.getId());

            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal())
                    .setName(offlinePlayer.getName())
                    .setLore(inChat ? "§aPeut voir votre chat" : "§cNe peut pas voir votre chat",
                            "",
                            "§7Cliquez pour permettre à ce PlayerManager de voir/ne plus voir vos messages.")
                    .setSkullOwner(offlinePlayer.getName())
                    .toItemStack(), onClick -> {
                if (inChat) {
                    ino.getInChat().remove(PlayerManager.getId());
                } else {
                    ino.getInChat().add(PlayerManager.getId());
                }
                inv.update(onClick.getPlayer(), this);
            });
        }

    }
}
