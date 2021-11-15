package fr.lastril.uhchost.modes.naruto.v2.gui.shikamaru;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.modes.naruto.v2.roles.shinobi.Shikamaru;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SelectPlayersGUI extends IQuickInventory {

    private final UhcHost main;
    private final Shikamaru shikamaru;
    private final List<Player> players;
    private final Consumer<SelectionPlayer> onClick;

    public SelectPlayersGUI(UhcHost main, Shikamaru shikamaru, Consumer<SelectionPlayer> onClick) {
        super("§6PlayerManagers", 9 * 6);
        this.main = main;
        this.shikamaru = shikamaru;
        this.onClick = onClick;
        this.players = new ArrayList<>();

        for (Entity entity : shikamaru.getPlayer().getNearbyEntities(shikamaru.getPlayersDistance(), shikamaru.getPlayersDistance(), shikamaru.getPlayersDistance())) {
            if (entity instanceof Player) {
                Player playersNearby = (Player) entity;
                PlayerManager PlayerManager = main.getPlayerManager(playersNearby.getUniqueId());
                if (playersNearby.getGameMode() != GameMode.SPECTATOR && PlayerManager.isAlive()) {
                    players.add(playersNearby);
                }
            }
        }
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                SelectPlayersGUI.this.onClick.accept(new SelectionPlayer(playerClick, player));
            });
        }
    }

    public class SelectionPlayer {

        private final Player shikamaru, target;

        private SelectionPlayer(Player shikamaru, Player target) {
            this.shikamaru = shikamaru;
            this.target = target;
        }

        public Player getShikamaru() {
            return shikamaru;
        }

        public Player getTarget() {
            return target;
        }
    }
}
