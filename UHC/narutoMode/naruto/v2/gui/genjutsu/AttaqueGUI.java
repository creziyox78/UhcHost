package fr.lastril.uhchost.modes.naruto.v2.gui.genjutsu;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.obj.PlayerManager;
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
                PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
                if (playerClick.getLocation().distance(player.getLocation()) > GenjutsuGUI.DISTANCE_ATTAQUE) {
                    playerClick.sendMessage(Messages.error(player.getName() + " n'est pas à " + GenjutsuGUI.DISTANCE_ATTAQUE + " blocs de vous !"));
                    return;
                }
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                Location location = player.getLocation().clone();
                location.add(location.getDirection().clone().multiply(-1.5D));
                playerClick.teleport(location);
                PlayerManager.setRoleCooldownAttaque(5 * 60);
                inv.close(playerClick);
            });
        }
    }
}
