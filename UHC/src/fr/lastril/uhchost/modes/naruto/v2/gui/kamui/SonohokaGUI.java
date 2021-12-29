package fr.lastril.uhchost.modes.naruto.v2.gui.kamui;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.Modes;
import fr.lastril.uhchost.modes.naruto.v2.NarutoV2Manager;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.items.KamuiItem;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
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
    private NarutoV2Manager narutoV2Manager;

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
        if(main.getGamemanager().getModes() != Modes.NARUTO_V2) return;
        narutoV2Manager = (NarutoV2Manager) main.getGamemanager().getModes().getMode().getModeManager();

    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName()).toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
                PlayerManager targetJoueur = main.getPlayerManager(player.getUniqueId());
                inv.close(playerClick);
                targetJoueur.setStunned(false);
                Location initialLocation = player.getLocation();
                kamuiUser.getInitialsLocation().put(player.getUniqueId(), initialLocation);
                if (joueur.getRole() instanceof NarutoV2Role) {
                    NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                    narutoRole.usePower(joueur);
                }
                narutoV2Manager.sendToKamui(player, initialLocation, sender);
                playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"Vous avez téléporté "+player.getName()+" dans le monde de Kamui.");

                joueur.setRoleCooldownSonohoka(kamuiUser.getSonohokaCooldown());
            });
        }
    }
}
