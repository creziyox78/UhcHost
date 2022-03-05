package fr.lastril.uhchost.player.events.interact;

import fr.lastril.uhchost.UhcHost;
import fr.lastril.uhchost.player.events.GameStartEvent;
import fr.lastril.uhchost.tools.NotStart;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractCheckWorld implements Listener {

    private final UhcHost pl;

    public InteractCheckWorld(UhcHost pl){
        this.pl = pl;
    }

    @EventHandler
    public void onInventoryClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if(itemStack == null)
            return;

        if(pl.gameManager.getHost() == player.getUniqueId() || pl.gameManager.isCoHost(player)){
            if(pl.gameManager.isPlayerCheckingWorld()){
                if(itemStack.getType() == Material.INK_SACK){
                    if(event.getAction() == Action.RIGHT_CLICK_AIR ||event.getAction() == Action.RIGHT_CLICK_BLOCK){
                        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
                            switch (itemStack.getItemMeta().getDisplayName()){
                                case "§cSupprimer le monde":
                                    pl.gameManager.setPlayerCheckingWorld(false);
                                    for(Player players : Bukkit.getOnlinePlayers()){
                                        players.kickPlayer("§cL'host du serveur redémarre le serveur afin de trouver un nouveau centre.\n" +
                                                "§cMerci de vous reconnecter d'ici quelques secondes.");
                                    }
                                    Bukkit.getScheduler().runTaskLater(pl, Bukkit::shutdown, 10);
                                    break;
                                case "§aValider le monde":
                                    pl.gameManager.setPlayerCheckingWorld(false);
                                    pl.gameManager.setValidateWorld(true);
                                    player.teleport(UhcHost.getInstance().gameManager.spawn);
                                    NotStart.PreHosting(player);
                                    break;
                            }
                        }
                    }
                }
            }
        }


    }

    @EventHandler
    public void onGameStart(GameStartEvent event){
        HandlerList.unregisterAll(this);
    }


}
