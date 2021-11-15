package fr.lastril.uhchost.modes.naruto.v2.gui.deidara;

import fr.atlantis.api.inventory.crafter.IQuickInventory;
import fr.atlantis.api.inventory.crafter.QuickInventory;
import fr.atlantis.api.item.crafter.QuickItem;
;
import fr.maygo.uhc.enums.Messages;
import fr.maygo.uhc.modes.naruto.v2.crafter.NarutoV2Role;
import fr.maygo.uhc.modes.naruto.v2.pathfinder.c2.C2Invoker;
import fr.maygo.uhc.modes.naruto.v2.roles.akatsuki.Deidara;
import fr.maygo.uhc.obj.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class C2GUI extends IQuickInventory {

    private final UhcHost main;
    private final Deidara deidara;
    private final List<Player> players;

    public C2GUI(UhcHost main, Deidara deidara) {
        super("§cC2", 9 * 6);
        this.main = main;
        this.deidara = deidara;
        this.players = new ArrayList<>();

        for (Entity entity : deidara.getPlayer().getNearbyEntities(deidara.getPlayersDistance(), deidara.getPlayersDistance(), deidara.getPlayersDistance())) {
            if (entity instanceof Player) {
                Player playersNearby = (Player) entity;
                if (playersNearby.getGameMode() != GameMode.SPECTATOR) {
                    players.add(playersNearby);
                }
            }
        }
    }

    @Override
    public void contents(QuickInventory inv) {

        for (Player player : players) {
            inv.addItem(new QuickItem(Material.SKULL_ITEM, 1, SkullType.PLAYER.ordinal()).setName(player.getName()).setSkullOwner(player.getName())
                    .setLore("",
                            "§7Cliquer pour que le chien explose à son contact.")
                    .toItemStack(), onClick -> {
                Player playerClick = onClick.getPlayer();
                PlayerManager PlayerManager = main.getPlayerManager(playerClick.getUniqueId());
                if (PlayerManager.getRoleCooldownC2() == 0) {
                    PlayerManager.setRoleCooldownC2(30 * 60);
                    C2Invoker.invokeC2(playerClick.getLocation(), player.getUniqueId());
                    if (PlayerManager.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) PlayerManager.getRole();
                        narutoRole.usePower(PlayerManager);
                    }
                    playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage() + "C2 court vers " + player.getName() + " !");
                    inv.close(playerClick);
                } else {
                    playerClick.sendMessage(Messages.cooldown(PlayerManager.getRoleCooldownC2()));
                }
            });
        }
    }
}
