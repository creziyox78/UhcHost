package fr.lastril.uhchost.modes.naruto.v2.gui.minato;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Minato;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayersGUI extends IQuickInventory {

    private final UhcHost main;
    private final List<Player> players;
    private final List<Location> balises;

    public PlayersGUI(UhcHost main, List<Player> players, List<Location> balises) {
        super("§6ShurikenJutsu", 9 * 6);
        this.main = main;
        this.players = players;
        this.balises = balises;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName())
                    .setLore("",
                            "§7Cliquez pour téléporter ce joueur",
                            "§7à l'une de vos balises.")
                    .toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                if (playerClick.getLocation().distance(player.getLocation()) > Minato.DISTANCE_SHURIKENJUTSU) {
                    playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + Minato.DISTANCE_SHURIKENJUTSU + " blocs de vous !"));
                    inv.close(playerClick);
                    return;
                }
                new ShurikenJutsuGUI(main, player, balises).open(playerClick);
            });
        }
    }
}
