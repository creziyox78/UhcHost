package fr.lastril.uhchost.modes.naruto.v2.gui.deidara;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.enums.Messages;
import fr.lastril.uhchost.modes.naruto.v2.crafter.NarutoV2Role;
import fr.lastril.uhchost.modes.naruto.v2.pathfinder.c2.C2Invoker;
import fr.lastril.uhchost.modes.naruto.v2.roles.akatsuki.Deidara;
import fr.lastril.uhchost.player.PlayerManager;
import fr.lastril.uhchost.tools.API.inventory.crafter.IQuickInventory;
import fr.lastril.uhchost.tools.API.inventory.crafter.QuickInventory;
import fr.lastril.uhchost.tools.API.items.crafter.QuickItem;
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
        super("§cC2", 9*6);
        this.main = main;
        this.deidara = deidara;
        this.players = new ArrayList<>();

        for (Entity entity : deidara.getPlayer().getNearbyEntities(deidara.getPlayersDistance(), deidara.getPlayersDistance(), deidara.getPlayersDistance())) {
            if(entity instanceof Player){
                Player playersNearby = (Player) entity;
                if(playersNearby.getGameMode() != GameMode.SPECTATOR){
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
                PlayerManager joueur = main.getPlayerManager(playerClick.getUniqueId());
                if (joueur.getRoleCooldownC2() == 0) {
                    joueur.setRoleCooldownC2(30*60);
                    C2Invoker.invokeC2(playerClick.getLocation(), player.getUniqueId());
                    if (joueur.getRole() instanceof NarutoV2Role) {
                        NarutoV2Role narutoRole = (NarutoV2Role) joueur.getRole();
                        narutoRole.usePower(joueur);
                    }
                    playerClick.sendMessage(Messages.NARUTO_PREFIX.getMessage()+"C2 court vers "+player.getName()+" !");
                    inv.close(playerClick);
                }else{
                    playerClick.sendMessage(Messages.cooldown(joueur.getRoleCooldownC2()));
                }
            });
        }
    }
}
