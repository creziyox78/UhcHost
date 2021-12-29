package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.List;

public class AttaqueGUI extends IQuickInventory {

    private final UhcHost main;
    private final List<Player> players;

    public AttaqueGUI(UhcHost main, List<Player> players) {
        super("§6Attaque", 9 * 6);
        this.main = main;
        this.players = players;
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName())
                    .setLore("§7",
                            "§7Cliquez pour vous téléporter dans son dos.")
                    .toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
                if (playerClick.getLocation().distance(player.getLocation()) > GenjutsuGUI.DISTANCE_ATTAQUE) {
                    playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + GenjutsuGUI.DISTANCE_ATTAQUE + " blocs de vous !"));
                    return;
                }
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                Location location = player.getLocation().clone();
                location.add(location.getDirection().clone().multiply(-1.5D));
                playerClick.teleport(location);
                joueur.setRoleCooldownAttaque(5*60);
                inv.close(playerClick);
            });
        }
    }
}
