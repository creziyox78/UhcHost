package fr.lastril.uhchost.modes.naruto.v2.gui.kakashi;

import fr.atlantis.api.inventory.GUIYesNo;
import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.pathfinder.pakkun.PakkunInvoker;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PakkunGUI extends IQuickInventory {

    private final UhcHost main;

    public PakkunGUI(UhcHost main) {
        super("§6Pakkun", 9 * 6);
        this.main = main;
    }

    @Override
    public void contents(QuickInventory inv) {


        List<Player> players = new ArrayList<>();

        for (Player playersOnline : Bukkit.getOnlinePlayers()) {
            if (playersOnline.getGameMode() != GameMode.SPECTATOR) {
                PlayerManager PlayerManager = main.getPlayerManager(playersOnline.getUniqueId());
                if (PlayerManager.isAlive()) {
                    players.add(playersOnline);
                }
            }
        }

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                new GUIYesNo(null, accept -> {
                    Player playerClick = accept.getPlayer();
                    PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());

                    PlayerManager.setRoleCooldownPakkun(30 * 60);
                    PakkunInvoker.invokePakkun(playerClick.getLocation(), player.getUniqueId());
                    playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Pakkun traque désormais " + player.getName() + " !");
                    inv.close(playerClick);
                }).open(onClick.getPlayer());
            });
        }
    }
}
