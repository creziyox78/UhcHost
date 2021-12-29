package fr.lastril.uhchost.modes.naruto.v2.gui.kakashi;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.pathfinder.pakkun.PakkunInvoker;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.GUIYesNo;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
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
        super("§6Pakkun", 9*6);
        this.main = main;
    }

    @Override
    public void contents(QuickInventory inv) {


        List<Player> players = new ArrayList<>();

        for (Player playersOnline : Bukkit.getOnlinePlayers()) {
            if(playersOnline.getGameMode() != GameMode.SPECTATOR){
                PlayerManager joueur = main.getPlayerManager(playersOnline.getUniqueId());
                if (joueur.isAlive()) {
                    players.add(playersOnline);
                }
            }
        }

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                new GUIYesNo(null, accept -> {
                    Player playerClick = accept.getPlayer();
                    PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());

                    joueur.setRoleCooldownPakkun(30*60);
                    PakkunInvoker.invokePakkun(playerClick.getLocation(), player.getUniqueId());
                    playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Pakkun traque désormais "+player.getName()+" !");
                    inv.close(playerClick);
                }).open(onClick.getPlayer());
            });
        }
    }
}
