package fr.lastril.uhchost.modes.naruto.v2.gui.chibakutensei;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.ChibakuTenseiItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;

import java.util.List;

public class BanshoTeninGUI extends IQuickInventory {

    private final UhcHost main;
    private final List<Player> players;
    private final ChibakuTenseiItem.ChibakuTenseiUser chibakuTenseiUser;

    public BanshoTeninGUI(UhcHost main, List<Player> players, ChibakuTenseiItem.ChibakuTenseiUser chibakuTenseiUser) {
        super(ChatColor.RED+"Banshô Ten'in", 9*6);
        this.main = main;
        this.players = players;
        this.chibakuTenseiUser = chibakuTenseiUser;
    }

    @Override
    public void contents(QuickInventory inv) {
        /* CONTOUR GLASS */
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, 0).setName(" ").toItemStack(), 0, 8);
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, 0).setName(" ").toItemStack(),
                inv.getInventory().getSize() - 9, inv.getInventory().getSize() - 1);
        inv.setVerticalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, 0).setName(" ").toItemStack(), 0,
                inv.getInventory().getSize() - 9);
        inv.setVerticalLine(new QuickItem(Material.STAINED_GLASS_PANE, 1, 0).setName(" ").toItemStack(), 8,
                inv.getInventory().getSize() - 1);

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName())
                    .setLore("§7Cliquez pour téléporter ce joueur sur vous.")
                    .setSkullOwner(player.getName()).toItemStack(), onClick -> {
                Player nagatoPlayer = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(nagatoPlayer.getUniqueId());

                player.teleport(nagatoPlayer);

                joueur.setRoleCooldownBanshoTenin(5*60);

                nagatoPlayer.sendMessage(Messages.NARUTO_PREFIX.getMessage()+Messages.USED_POWER.getMessage());
                player.sendMessage(Messages.NARUTO_PREFIX.getMessage()+chibakuTenseiUser.getPlayerName()+" vous a téléporté à lui avec Banshô Ten'in.");

                if(joueur.getRole() instanceof NarutoV2Role){
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                inv.close(player);
            });
        }
    }
}
