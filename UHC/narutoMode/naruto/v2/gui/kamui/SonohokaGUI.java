package fr.lastril.uhchost.modes.naruto.v2.gui.kamui;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.items.KamuiItem;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SonohokaGUI extends IQuickInventory {

    private final UhcHost main;
    private final KamuiItem.KamuiUser kamuiUser;
    private final List<Player> players;
    private final Player sender;

    public SonohokaGUI(UhcHost main, Player player, KamuiItem.KamuiUser kamuiUser, Player sender) {
        super("§6Sonohoka", 9 * 6);
        this.main = main;
        this.kamuiUser = kamuiUser;
        this.players = new ArrayList<>();
        this.sender = sender;
        for (Entity entity : player.getNearbyEntities(kamuiUser.getSonohokaDistance(), kamuiUser.getSonohokaDistance(), kamuiUser.getSonohokaDistance())) {
            if (entity instanceof Player) {
                Player playersNearby = (Player) entity;
                if (playersNearby.getGameMode() != GameMode.SPECTATOR && main.getPlayerManager(playersNearby.getUniqueId()).isAlive()) {
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
                PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
                PlayerManager targetPlayerManager = main.getPlayerManager(player.getUniqueId());
                inv.close(playerClick);
                targetPlayerManager.setStunned(false);
                Location initialLocation = player.getLocation();
                kamuiUser.getInitialsLocation().put(player.getUniqueId(), initialLocation);
                if (PlayerManager.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                    narutoRole.usePower(PlayerManager);
                }
                main.getNarutoV2Manager().sendToKamui(player, initialLocation, sender);
                main.getSoundUtils().playSoundDistance(player.getLocation(), 3, "atlantis.kamui");
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "Vous avez téléporté " + player.getName() + " dans le monde de Kamui.");

                PlayerManager.setRoleCooldownSonohoka(kamuiUser.getSonohokaCooldown());
            });
        }
    }
}
