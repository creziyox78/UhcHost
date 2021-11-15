package fr.lastril.uhchost.modes.naruto.v2.gui.kakashi;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Kakashi;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SharinganGUI extends IQuickInventory {

    private final UhcHost main;
    private final Kakashi kakashi;

    public SharinganGUI(UhcHost main, Kakashi kakashi) {
        super(ChatColor.RED + "Sharingan", 9 * 1);
        this.main = main;
        this.kakashi = kakashi;
    }

    @Override
    public void contents(QuickInventory inv) {
        inv.setHorizontalLine(new QuickItem(Material.STAINED_GLASS_PANE).setName(" ").toItemStack(), 0, 8);

        inv.setItem(new QuickItem(Material.PAPER).setName("§cCopie")
                .setLore("",
                        "§7Permet de copier les effets de base",
                        "§7d'un PlayerManager en restant proche de lui.")
                .toItemStack(), onClick -> {
            Player player = onClick.getPlayer();
            if (kakashi.getCopying() == null) {
                List<Player> players = new ArrayList<>();

                for (Entity entity : inv.getOwner().getNearbyEntities(Kakashi.DISTANCE_COPY, Kakashi.DISTANCE_COPY, Kakashi.DISTANCE_COPY)) {
                    if (entity instanceof Player) {
                        Player playerNearby = (Player) entity;
                        if (playerNearby.getGameMode() != GameMode.SPECTATOR) {
                            players.add(playerNearby);
                        }
                    }
                }
                new CopyGUI(main, kakashi, players).open(player);
            } else {
                player.sendMessage(Messages.error("Vous copiez déjà un PlayerManager !"));
            }
        }, 3);

        inv.setItem(new QuickItem(Material.NETHER_STAR).setName("§6Techniques")
                .setLore("",
                        "§7Utilisez les effets des PlayerManagers que vous",
                        "§7avez copiés auparavant.")
                .toItemStack(), onClick -> {
            new TechniquesGUI(main, kakashi).open(onClick.getPlayer());
        }, 5);
    }
}
