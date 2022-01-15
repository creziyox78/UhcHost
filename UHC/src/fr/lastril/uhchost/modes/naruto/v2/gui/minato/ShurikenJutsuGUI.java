package fr.lastril.uhchost.modes.naruto.v2.gui.minato;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.roles.shinobi.Minato;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class ShurikenJutsuGUI extends IQuickInventory {

    private final UhcHost main;
    private final Player target;
    private final List<Location> balises;

    public ShurikenJutsuGUI(UhcHost main, Player target, List<Location> balises) {
        super("§6ShurikenJutsu", 9*6);
        this.main = main;
        this.target = target;
        this.balises = balises;
    }

    @Override
    public void contents(QuickInventory inv) {
        /* CONTOUR GLASS */
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(),
                inv.getInventory().getSize() - 9, inv.getInventory().getSize() - 1);
        inv.setVerticalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0,
                inv.getInventory().getSize() - 9);
        inv.setVerticalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 8,
                inv.getInventory().getSize() - 1);

        for (Location balise : this.balises) {
            inv.addItem(new QuickItem(Material.BEACON).setName("Balise #"+this.balises.indexOf(balise)+1).setLore("§7X: "+balise.getBlockX(), "§7Y: "+balise.getBlockY(), "§7Z: "+balise.getBlockZ()).toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                if (playerClick.getLocation().distance(target.getLocation()) > Minato.DISTANCE_SHURIKENJUTSU) {
                    playerClick.sendMessage(Messages.error(target.getName() + " n'est pas à " + Minato.DISTANCE_SHURIKENJUTSU + " blocs de vous !"));
                    inv.close(playerClick);
                    return;
                }
                PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());

                target.teleport(balise);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"§aVous avez téléporté "+target.getName()+" à la balise #"+this.balises.indexOf(balise)+1+" !");
                target.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Minato vous à téléporté à une de ses balises !");

                joueur.setRoleCooldownShurikenJustuTP(10 * 60);
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
            });
        }
    }
}
