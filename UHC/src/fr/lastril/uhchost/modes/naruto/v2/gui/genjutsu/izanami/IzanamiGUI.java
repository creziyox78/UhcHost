package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu.izanami;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu.GenjutsuGUI;
import fr.lastril.uhchost.modes.naruto.v2.items.GenjutsuItem;
import fr.lastril.uhchost.modes.naruto.v2.izanami.IzanamiGoal;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
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
                                "§7Cliquez pour infecter ce joueur.")
                        .toItemStack(), onClick -> {
                    Player playerClick = onClick.getPlayer();
                    if (playerClick.getLocation().distance(player.getLocation()) > GenjutsuGUI.DISTANCE_IZANAMI) {
                        playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + GenjutsuGUI.DISTANCE_IZANAMI + " blocs de vous !"));
                    } else {
                        genjutsuUser.setIzanamiGoal(new IzanamiGoal(main, main.getPlayerManager(player.getUniqueId()), main.getPlayerManager(playerClick.getUniqueId()), genjutsuUser));
                        genjutsuUser.useIzanami();
                        playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez bien ciblé " + player.getName() + ", vous et le joueur ciblé devez maintenant effectuer des objectifs. Vous pouvez les consulter dans le menu \"Izanami\" de votre item \"Genjutsu\".");
                    }
                    inv.close(playerClick);

                });
            }


    }
}
