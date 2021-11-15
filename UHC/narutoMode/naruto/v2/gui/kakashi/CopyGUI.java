package fr.lastril.uhchost.modes.naruto.v2.gui.kakashi;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kakashi;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.List;

public class CopyGUI extends IQuickInventory {

    private final UhcHost main;
    private final Kakashi kakashi;
    private final List<Player> players;

    public CopyGUI(UhcHost main, Kakashi kakashi, List<Player> players) {
        super(ChatColor.RED + "Copie", 9 * 6);
        this.main = main;
        this.kakashi = kakashi;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                if (playerClick.getLocation().distance(player.getLocation()) > Kakashi.DISTANCE_COPY) {
                    playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + Kakashi.DISTANCE_COPY + " blocs de vous !"));
                    inv.close(playerClick);
                    return;
                }

                if (kakashi.getCopieds().contains(player.getUniqueId())) {
                    playerClick.sendMessage(Messages.error("Vous avez déjà copié " + player.getName() + " !"));
                    inv.close(playerClick);
                    return;
                }

                kakashi.setCopying(player.getUniqueId());


            });
        }
    }
}
