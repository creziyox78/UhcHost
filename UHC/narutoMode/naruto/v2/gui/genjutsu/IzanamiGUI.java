package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.items.GenjutsuItem;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.List;

public class IzanamiGUI extends IQuickInventory {

    private final UhcHost main;
    private final GenjutsuItem.GenjutsuUser genjutsuUser;
    private final List<Player> players;

    public IzanamiGUI(UhcHost main, GenjutsuItem.GenjutsuUser genjutsuUser, List<Player> players) {
        super("§cIzanami", 9 * 6);
        this.main = main;
        this.genjutsuUser = genjutsuUser;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName())
                    .setLore("",
                            "§7Cliquez pour infecter ou non ce PlayerManager.")
                    .toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                if (playerClick.getLocation().distance(player.getLocation()) > GenjutsuGUI.DISTANCE_IZANAMI) {
                    playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + GenjutsuGUI.DISTANCE_IZANAMI + " blocs de vous !"));
                    inv.close(playerClick);
                    return;
                }
                new IzanamiSelectGUI(main, genjutsuUser, playerClick).open(player);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Le PlayerManager ciblé doit maintenant choisir s'il reste dans son camp ou rejoint le votre.");
                genjutsuUser.useIzanami();
                inv.close(playerClick);
            });
        }
    }
}
